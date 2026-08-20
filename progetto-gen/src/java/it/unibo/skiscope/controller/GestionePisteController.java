package it.unibo.skiscope.controller;

import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.Pista;

import java.util.List;

/**
 * Controller per il caso d'uso "GestionePiste" (RFG2), riservato al
 * Presidente. L'operazione è consentita solo durante la stagione attiva.
 */
public class GestionePisteController {

    private final DataStore db = DataStore.getInstance();

    public List<Pista> getPiste() {
        return db.getPiste();
    }

    public void apriPista(int idPista) throws OperazioneException {
        verificaStagioneAttiva();
        Pista pista = getPistaOFail(idPista);
        pista.apri();
    }

    public void chiudiPista(int idPista) throws OperazioneException {
        verificaStagioneAttiva();
        Pista pista = getPistaOFail(idPista);
        pista.chiudi();
    }

    private Pista getPistaOFail(int idPista) throws OperazioneException {
        Pista pista = db.trovaPista(idPista);
        if (pista == null) {
            throw new OperazioneException("Pista non trovata.");
        }
        return pista;
    }

    private void verificaStagioneAttiva() throws OperazioneException {
        if (!db.getStagione().isAttiva()) {
            throw new OperazioneException("Operazione non consentita fuori dal periodo di stagione.");
        }
    }
}
