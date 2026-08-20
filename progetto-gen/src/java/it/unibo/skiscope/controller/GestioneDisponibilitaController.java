package it.unibo.skiscope.controller;

import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.Lezione;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller per i casi d'uso "AggiungiDisponibilita" e "RimuoviDisponibilita"
 * riservati al Maestro di Sci (RFM1, RFM2, RFM5).
 */
public class GestioneDisponibilitaController {

    private final DataStore db = DataStore.getInstance();

    /**
     * RFM2: gli orari devono rispettare la granularità dell'ora. La
     * disponibilità viene suddivisa automaticamente in blocchi da 1 ora,
     * ciascuno prenotabile indipendentemente.
     */
    public List<Lezione> aggiungiDisponibilita(String maestroUsername, String maestroNomeCompleto,
                                                LocalDate data, int oraInizio, int oraFine)
            throws OperazioneException {
        if (data == null) {
            throw new OperazioneException("Seleziona una data valida.");
        }
        if (oraFine <= oraInizio) {
            throw new OperazioneException("L'ora di fine deve essere successiva all'ora di inizio.");
        }

        List<Lezione> nuoveLezioni = new ArrayList<>();
        for (int h = oraInizio; h < oraFine; h++) {
            Lezione l = new Lezione(db.nuovoId("lz"), maestroUsername, maestroNomeCompleto,
                    data, LocalTime.of(h, 0), LocalTime.of(h + 1, 0));
            db.getLezioni().add(l);
            nuoveLezioni.add(l);
        }
        return nuoveLezioni;
    }

    /** RFM5: una lezione già prenotata non può essere rimossa. */
    public void rimuoviDisponibilita(String idLezione) throws OperazioneException {
        Lezione lezione = db.trovaLezione(idLezione);
        if (lezione == null) {
            throw new OperazioneException("Disponibilità non trovata.");
        }
        if (!lezione.isLibera()) {
            throw new OperazioneException("Impossibile rimuovere: la lezione è già stata prenotata.");
        }
        db.getLezioni().remove(lezione);
    }

    public List<Lezione> getDisponibilitaMaestro(String maestroUsername) {
        List<Lezione> risultato = new ArrayList<>();
        for (Lezione l : db.getLezioni()) {
            if (l.getMaestroUsername().equals(maestroUsername)) {
                risultato.add(l);
            }
        }
        return risultato;
    }
}
