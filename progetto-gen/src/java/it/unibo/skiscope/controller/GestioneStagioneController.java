package it.unibo.skiscope.controller;

import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.Stagione;

import java.time.LocalDate;

/**
 * Controller per il caso d'uso "GestioneStagione" (RFG2, RFG5), riservato
 * al Presidente.
 */
public class GestioneStagioneController {

    private final DataStore db = DataStore.getInstance();

    public Stagione getStagione() {
        return db.getStagione();
    }

    public void apriStagione(LocalDate dataInizio, LocalDate dataFine) throws OperazioneException {
        if (dataInizio == null || dataFine == null) {
            throw new OperazioneException("Entrambe le date sono obbligatorie.");
        }
        if (!dataFine.isAfter(dataInizio)) {
            throw new OperazioneException("La data di fine deve essere successiva alla data di inizio.");
        }
        db.getStagione().setDate(dataInizio, dataFine);
    }

    public boolean isStagioneAttiva() {
        return db.getStagione().isAttiva();
    }
}
