package it.unibo.skiscope.controller;

import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.RichiestaSoccorso;
import it.unibo.skiscope.model.StatoRichiesta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller per il caso d'uso "RichiestaSoccorso" (RFSC1, RFSC4).
 */
public class RichiestaSoccorsoController {

    private final DataStore db = DataStore.getInstance();

    /**
     * RFSC4: luogo e descrizione sono obbligatori. Un cliente con una
     * richiesta già attiva non può inviarne un'altra (evita duplicati).
     */
    public RichiestaSoccorso inviaRichiesta(String clienteUsername, String clienteNomeCompleto,
                                             String luogo, String descrizione) throws OperazioneException {
        if (luogo == null || luogo.isBlank() || descrizione == null || descrizione.isBlank()) {
            throw new OperazioneException("Luogo e descrizione dell'infortunio sono obbligatori.");
        }
        if (haRichiestaAttiva(clienteUsername)) {
            throw new OperazioneException("Hai già una richiesta di soccorso attiva.");
        }

        RichiestaSoccorso richiesta = new RichiestaSoccorso(db.nuovoId("rs"), clienteUsername,
                clienteNomeCompleto, luogo, descrizione, LocalDateTime.now());
        db.getRichiesteSoccorso().add(richiesta);
        return richiesta;
    }

    public boolean haRichiestaAttiva(String clienteUsername) {
        for (RichiestaSoccorso r : db.getRichiesteSoccorso()) {
            if (r.getClienteUsername().equals(clienteUsername) && r.getStato() == StatoRichiesta.APERTA) {
                return true;
            }
        }
        return false;
    }

    public List<RichiestaSoccorso> getRichiesteCliente(String clienteUsername) {
        List<RichiestaSoccorso> risultato = new ArrayList<>();
        for (RichiestaSoccorso r : db.getRichiesteSoccorso()) {
            if (r.getClienteUsername().equals(clienteUsername)) {
                risultato.add(r);
            }
        }
        return risultato;
    }
}
