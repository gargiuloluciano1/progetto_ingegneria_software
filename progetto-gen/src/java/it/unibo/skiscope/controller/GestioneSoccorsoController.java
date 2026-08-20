package it.unibo.skiscope.controller;

import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.RichiestaSoccorso;
import it.unibo.skiscope.model.StatoRichiesta;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller per il caso d'uso "PresaInCaricoSoccorso" riservato ai
 * Soccorritori (RFSC2, RFSC3, RFSC5).
 */
public class GestioneSoccorsoController {

    private final DataStore db = DataStore.getInstance();

    public List<RichiestaSoccorso> visualizzaRichiesteAperte() {
        List<RichiestaSoccorso> risultato = new ArrayList<>();
        for (RichiestaSoccorso r : db.getRichiesteSoccorso()) {
            if (r.getStato() == StatoRichiesta.APERTA) {
                risultato.add(r);
            }
        }
        return risultato;
    }

    public List<RichiestaSoccorso> visualizzaTutte() {
        return db.getRichiesteSoccorso();
    }

    /**
     * RFSC3: se la richiesta è già stata presa in carico da un altro
     * soccorritore, l'operazione fallisce (concorrenza simulata).
     * RFSC5: gli altri soccorritori vengono "notificati" tramite
     * l'aggiornamento in tempo reale della lista (in questo prototipo,
     * il semplice ricaricamento della vista).
     */
    public void presaInCarico(String idRichiesta, String soccorritoreUsername, String soccorritoreNomeCompleto)
            throws OperazioneException {
        RichiestaSoccorso richiesta = db.trovaRichiesta(idRichiesta);
        if (richiesta == null) {
            throw new OperazioneException("Richiesta non trovata.");
        }
        boolean ok = richiesta.prendiInCarico(soccorritoreUsername, soccorritoreNomeCompleto);
        if (!ok) {
            throw new OperazioneException("La richiesta è già stata presa in carico da un altro soccorritore.");
        }
    }
}
