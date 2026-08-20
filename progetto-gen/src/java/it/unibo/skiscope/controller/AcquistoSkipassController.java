package it.unibo.skiscope.controller;

import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.EsitoPagamento;
import it.unibo.skiscope.model.Skipass;
import it.unibo.skiscope.model.Stagione;
import it.unibo.skiscope.model.TipoSkipass;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Controller per il caso d'uso "AcquistoSkipass" (RFS1-RFS7).
 */
public class AcquistoSkipassController {

    private final DataStore db = DataStore.getInstance();

    /**
     * RFS4: tutti i campi sono obbligatori prima di consentire il pagamento.
     * RFS3: genera un codice QR univoco per ogni skipass acquistato.
     */
    public Skipass acquistaSkipass(String clienteUsername, String nome, String cognome, String codiceFiscale,
                                    TipoSkipass tipo) throws OperazioneException {
        if (nome == null || nome.isBlank() || cognome == null || cognome.isBlank()
                || codiceFiscale == null || codiceFiscale.isBlank()) {
            throw new OperazioneException("Nome, cognome e codice fiscale sono tutti obbligatori.");
        }
        if (tipo == null) {
            throw new OperazioneException("Seleziona un tipo di skipass.");
        }

        EsitoPagamento esito = effettuaPagamento(tipo.getPrezzo());
        if (esito != EsitoPagamento.APPROVATO) {
            throw new OperazioneException("Pagamento rifiutato. Riprova.");
        }

        LocalDateTime scadenza = calcolaScadenza(tipo);
        String codiceQR = "SKS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Skipass skipass = new Skipass(db.nuovoId("sk"), tipo, nome, cognome, codiceFiscale.toUpperCase(),
                scadenza, codiceQR, clienteUsername);
        db.getSkipassVenduti().add(skipass);
        return skipass;
    }

    /** RFS6, RFS7: calcola la scadenza in base al tipo di skipass acquistato. */
    private LocalDateTime calcolaScadenza(TipoSkipass tipo) {
        Stagione stagione = db.getStagione();
        if (tipo == TipoSkipass.STAGIONALE) {
            return LocalDateTime.of(stagione.getDataFine(), LocalTime.of(16, 0));
        }
        LocalDateTime ora = LocalDateTime.now();
        return ora.toLocalDate().plusDays(tipo.getGiorniAggiuntivi()).atTime(16, 0);
    }

    private EsitoPagamento effettuaPagamento(double importo) {
        return EsitoPagamento.APPROVATO;
    }
}
