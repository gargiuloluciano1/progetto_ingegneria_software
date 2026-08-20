package it.unibo.skiscope.controller;

import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.EsitoPagamento;
import it.unibo.skiscope.model.Lezione;
import it.unibo.skiscope.model.Partecipante;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller per il caso d'uso "PrenotaLezione" (RFM3-RFM7).
 */
public class PrenotaLezioneController {

    private final DataStore db = DataStore.getInstance();

    public List<Lezione> ricercaDisponibilita() {
        List<Lezione> disponibili = new ArrayList<>();
        for (Lezione l : db.getLezioni()) {
            if (l.isLibera()) {
                disponibili.add(l);
            }
        }
        return disponibili;
    }

    /** RFM6: prenotazione di una lezione singola, 50€/ora. */
    public Lezione prenotaLezioneSingola(String idLezione, String clienteUsername, String nome, String cognome)
            throws OperazioneException {
        if (nome == null || nome.isBlank() || cognome == null || cognome.isBlank()) {
            throw new OperazioneException("Inserisci nome e cognome del partecipante.");
        }
        Lezione lezione = getLezioneLiberaOFail(idLezione);

        EsitoPagamento esito = effettuaPagamento(50.0);
        if (esito != EsitoPagamento.APPROVATO) {
            throw new OperazioneException("Pagamento rifiutato. Riprova.");
        }
        lezione.prenotaSingola(new Partecipante(nome, cognome, clienteUsername));
        return lezione;
    }

    /** RFM3, RFM7: prenotazione di gruppo, da 2 a 10 persone. */
    public Lezione prenotaLezioneGruppo(String idLezione, String clienteUsername, String nomeReferente,
                                         String cognomeReferente, int numPartecipanti) throws OperazioneException {
        if (nomeReferente == null || nomeReferente.isBlank()
                || cognomeReferente == null || cognomeReferente.isBlank()) {
            throw new OperazioneException("Inserisci nome e cognome del referente del gruppo.");
        }
        if (numPartecipanti < 2 || numPartecipanti > 10) {
            throw new OperazioneException("Il numero di partecipanti deve essere compreso tra 2 e 10.");
        }
        Lezione lezione = getLezioneLiberaOFail(idLezione);

        double prezzo = 60.0 + Math.max(0, numPartecipanti - 2) * 10.0;
        EsitoPagamento esito = effettuaPagamento(prezzo);
        if (esito != EsitoPagamento.APPROVATO) {
            throw new OperazioneException("Pagamento rifiutato. Riprova.");
        }
        lezione.prenotaGruppo(new Partecipante(nomeReferente, cognomeReferente, clienteUsername), numPartecipanti);
        return lezione;
    }

    private Lezione getLezioneLiberaOFail(String idLezione) throws OperazioneException {
        Lezione lezione = db.trovaLezione(idLezione);
        if (lezione == null || !lezione.isLibera()) {
            throw new OperazioneException("Questa fascia oraria non è più disponibile.");
        }
        return lezione;
    }

    private EsitoPagamento effettuaPagamento(double importo) {
        return EsitoPagamento.APPROVATO;
    }
}
