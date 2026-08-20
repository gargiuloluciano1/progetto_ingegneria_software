package it.unibo.skiscope.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Fascia oraria di lezione (RFM1, RFM2): granularità minima di un'ora,
 * inizia e termina esclusivamente all'ora esatta. Una volta prenotata
 * (singola o di gruppo) diventa PRENOTATA e non può più essere rimossa
 * dal maestro (RFM5).
 */
public class Lezione {

    private final String id;
    private final String maestroUsername;
    private final String maestroNomeCompleto;
    private final LocalDate data;
    private final LocalTime oraInizio;
    private final LocalTime oraFine;
    private StatoLezione stato;
    private TipoLezione tipo;
    private int numPartecipanti;
    private double prezzo;
    private final List<Partecipante> iscritti = new ArrayList<>();

    public Lezione(String id, String maestroUsername, String maestroNomeCompleto,
                    LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        this.id = id;
        this.maestroUsername = maestroUsername;
        this.maestroNomeCompleto = maestroNomeCompleto;
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.stato = StatoLezione.LIBERA;
    }

    public String getId() {
        return id;
    }

    public String getMaestroUsername() {
        return maestroUsername;
    }

    public String getMaestroNomeCompleto() {
        return maestroNomeCompleto;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

    public StatoLezione getStato() {
        return stato;
    }

    public TipoLezione getTipo() {
        return tipo;
    }

    public int getNumPartecipanti() {
        return numPartecipanti;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public List<Partecipante> getIscritti() {
        return iscritti;
    }

    /** RFM6: 50€/ora per la lezione singola. */
    public void prenotaSingola(Partecipante partecipante) {
        this.tipo = TipoLezione.SINGOLA;
        this.numPartecipanti = 1;
        this.prezzo = 50.0;
        this.iscritti.add(partecipante);
        this.stato = StatoLezione.PRENOTATA;
    }

    /** RFM7: 60€/ora per due persone, +10€/ora per ogni persona aggiuntiva, max 10. */
    public void prenotaGruppo(Partecipante referente, int numPartecipanti) {
        this.tipo = TipoLezione.GRUPPO;
        this.numPartecipanti = numPartecipanti;
        this.prezzo = 60.0 + Math.max(0, numPartecipanti - 2) * 10.0;
        this.iscritti.add(referente);
        this.stato = StatoLezione.PRENOTATA;
    }

    public boolean isLibera() {
        return stato == StatoLezione.LIBERA;
    }
}
