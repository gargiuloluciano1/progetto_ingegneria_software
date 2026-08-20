package it.unibo.skiscope.model;

/**
 * RFP2: parcheggio di una delle quattro località di accesso al comprensorio,
 * con una capacità massima di veicoli.
 */
public class Parcheggio {

    private final String id;
    private final String nome;
    private final int capacita;
    private int occupati;

    public Parcheggio(String id, String nome, int capacita, int occupatiIniziali) {
        this.id = id;
        this.nome = nome;
        this.capacita = capacita;
        this.occupati = occupatiIniziali;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getCapacita() {
        return capacita;
    }

    public int getOccupati() {
        return occupati;
    }

    public boolean isCompleto() {
        return occupati >= capacita;
    }

    /** Registra l'ingresso di un nuovo veicolo, se c'è posto disponibile. */
    public boolean occupaPosto() {
        if (isCompleto()) {
            return false;
        }
        occupati++;
        return true;
    }
}
