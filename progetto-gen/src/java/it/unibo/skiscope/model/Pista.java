package it.unibo.skiscope.model;

/**
 * Pista del comprensorio. RFG2: il presidente decide quali piste aprire e
 * quali chiudere; inizialmente sono tutte chiuse.
 */
public class Pista {

    private final int id;
    private final String nome;
    private final Difficolta difficolta;
    private StatoPista stato;

    public Pista(int id, String nome, Difficolta difficolta, StatoPista statoIniziale) {
        this.id = id;
        this.nome = nome;
        this.difficolta = difficolta;
        this.stato = statoIniziale;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Difficolta getDifficolta() {
        return difficolta;
    }

    public StatoPista getStato() {
        return stato;
    }

    public void apri() {
        this.stato = StatoPista.APERTA;
    }

    public void chiudi() {
        this.stato = StatoPista.CHIUSA;
    }
}
