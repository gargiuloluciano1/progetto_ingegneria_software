package it.unibo.skiscope.model;

import java.time.LocalDateTime;

/**
 * Skipass acquistato da un cliente. RFS3: ogni skipass ha un codice QR
 * univoco. RFS5: non è rimborsabile.
 */
public class Skipass {

    private final String id;
    private final TipoSkipass tipo;
    private final String nomeUtilizzatore;
    private final String cognomeUtilizzatore;
    private final String codiceFiscaleUtilizzatore;
    private final LocalDateTime scadenza;
    private final String codiceQR;
    private final String clienteUsername;

    public Skipass(String id, TipoSkipass tipo, String nomeUtilizzatore, String cognomeUtilizzatore,
                    String codiceFiscaleUtilizzatore, LocalDateTime scadenza, String codiceQR,
                    String clienteUsername) {
        this.id = id;
        this.tipo = tipo;
        this.nomeUtilizzatore = nomeUtilizzatore;
        this.cognomeUtilizzatore = cognomeUtilizzatore;
        this.codiceFiscaleUtilizzatore = codiceFiscaleUtilizzatore;
        this.scadenza = scadenza;
        this.codiceQR = codiceQR;
        this.clienteUsername = clienteUsername;
    }

    public String getId() {
        return id;
    }

    public TipoSkipass getTipo() {
        return tipo;
    }

    public String getNomeUtilizzatore() {
        return nomeUtilizzatore;
    }

    public String getCognomeUtilizzatore() {
        return cognomeUtilizzatore;
    }

    public String getCodiceFiscaleUtilizzatore() {
        return codiceFiscaleUtilizzatore;
    }

    public LocalDateTime getScadenza() {
        return scadenza;
    }

    public String getCodiceQR() {
        return codiceQR;
    }

    public String getClienteUsername() {
        return clienteUsername;
    }
}
