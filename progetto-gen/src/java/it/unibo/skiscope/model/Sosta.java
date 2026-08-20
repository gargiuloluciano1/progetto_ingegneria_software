package it.unibo.skiscope.model;

import java.time.LocalDateTime;

/** Sosta pagata da un cliente in un dato parcheggio (RFP3). */
public class Sosta {

    private final String targa;
    private final String localita;
    private final int oreSosta;
    private final double costo;
    private final LocalDateTime dataOra;
    private final String clienteUsername;

    public Sosta(String targa, String localita, int oreSosta, double costo, LocalDateTime dataOra,
                 String clienteUsername) {
        this.targa = targa;
        this.localita = localita;
        this.oreSosta = oreSosta;
        this.costo = costo;
        this.dataOra = dataOra;
        this.clienteUsername = clienteUsername;
    }

    public String getTarga() {
        return targa;
    }

    public String getLocalita() {
        return localita;
    }

    public int getOreSosta() {
        return oreSosta;
    }

    public double getCosto() {
        return costo;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public String getClienteUsername() {
        return clienteUsername;
    }
}
