package it.unibo.skiscope.controller;

import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.EsitoPagamento;
import it.unibo.skiscope.model.Parcheggio;
import it.unibo.skiscope.model.Sosta;

import java.time.LocalDateTime;

/**
 * Controller per il caso d'uso "PagamentoParcheggio" (RFP1-RFP5).
 */
public class PrenotazioneParcheggioController {

    private static final double TARIFFA_ORARIA = 1.50;
    private static final double TARIFFA_MASSIMA_GIORNALIERA = 6.0;

    private final DataStore db = DataStore.getInstance();

    /** RFP4, RFP5: 1,50€/ora con tetto massimo di 6€/giorno. */
    public double calcolaCosto(int oreSosta) {
        return Math.min(oreSosta * TARIFFA_ORARIA, TARIFFA_MASSIMA_GIORNALIERA);
    }

    /**
     * Convalida i dati, verifica la disponibilità del parcheggio e simula il
     * pagamento (delegato in un sistema reale a un PSP esterno, RS3NF).
     */
    public Sosta prenotaParcheggio(String clienteUsername, String targa, String idParcheggio, int oreSosta)
            throws OperazioneException {
        if (targa == null || targa.isBlank()) {
            throw new OperazioneException("La targa del veicolo è obbligatoria.");
        }
        if (oreSosta < 1) {
            throw new OperazioneException("Inserisci un numero di ore di sosta valido.");
        }
        Parcheggio parcheggio = db.trovaParcheggio(idParcheggio);
        if (parcheggio == null) {
            throw new OperazioneException("Località di parcheggio non valida.");
        }
        if (parcheggio.isCompleto()) {
            throw new OperazioneException("Il parcheggio selezionato è completo. Scegli un'altra località.");
        }

        double costo = calcolaCosto(oreSosta);
        EsitoPagamento esito = effettuaPagamento(costo);
        if (esito != EsitoPagamento.APPROVATO) {
            throw new OperazioneException("Pagamento rifiutato. Riprova.");
        }

        parcheggio.occupaPosto();
        Sosta sosta = new Sosta(targa.toUpperCase(), parcheggio.getNome(), oreSosta, costo,
                LocalDateTime.now(), clienteUsername);
        db.getSoste().add(sosta);
        return sosta;
    }

    /** Simula l'esito del pagamento (in un sistema reale: chiamata al PSP esterno). */
    private EsitoPagamento effettuaPagamento(double importo) {
        return EsitoPagamento.APPROVATO;
    }
}
