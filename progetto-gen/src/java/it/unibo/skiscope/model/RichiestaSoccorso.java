package it.unibo.skiscope.model;

import java.time.LocalDateTime;

/**
 * RFSC1, RFSC4: richiesta di soccorso inviata da un cliente, con luogo e
 * descrizione dell'infortunio. RFSC3: una volta presa in carico da un
 * soccorritore non può essere presa in carico da un altro.
 */
public class RichiestaSoccorso {

    private final String id;
    private final String clienteUsername;
    private final String clienteNomeCompleto;
    private final String luogo;
    private final String descrizione;
    private final LocalDateTime dataOraInvio;
    private StatoRichiesta stato;
    private String soccorritoreUsername;
    private String soccorritoreNomeCompleto;

    public RichiestaSoccorso(String id, String clienteUsername, String clienteNomeCompleto,
                              String luogo, String descrizione, LocalDateTime dataOraInvio) {
        this.id = id;
        this.clienteUsername = clienteUsername;
        this.clienteNomeCompleto = clienteNomeCompleto;
        this.luogo = luogo;
        this.descrizione = descrizione;
        this.dataOraInvio = dataOraInvio;
        this.stato = StatoRichiesta.APERTA;
    }

    public String getId() {
        return id;
    }

    public String getClienteUsername() {
        return clienteUsername;
    }

    public String getClienteNomeCompleto() {
        return clienteNomeCompleto;
    }

    public String getLuogo() {
        return luogo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public LocalDateTime getDataOraInvio() {
        return dataOraInvio;
    }

    public StatoRichiesta getStato() {
        return stato;
    }

    public String getSoccorritoreUsername() {
        return soccorritoreUsername;
    }

    public String getSoccorritoreNomeCompleto() {
        return soccorritoreNomeCompleto;
    }

    /** @return true se la presa in carico è avvenuta con successo, false se già assegnata. */
    public boolean prendiInCarico(String soccorritoreUsername, String soccorritoreNomeCompleto) {
        if (stato == StatoRichiesta.PRESA_IN_CARICO) {
            return false;
        }
        this.stato = StatoRichiesta.PRESA_IN_CARICO;
        this.soccorritoreUsername = soccorritoreUsername;
        this.soccorritoreNomeCompleto = soccorritoreNomeCompleto;
        return true;
    }
}
