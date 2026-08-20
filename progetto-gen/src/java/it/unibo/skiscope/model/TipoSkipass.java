package it.unibo.skiscope.model;

/** RFS7: i cinque tipi di skipass disponibili, con relativo prezzo e durata. */
public enum TipoSkipass {
    GIORNALIERO("Giornaliero", 35.0, 0),
    DUE_GIORNI("2 Giorni", 65.0, 1),
    TRE_GIORNI("3 Giorni", 90.0, 2),
    SETTIMANALE("Settimanale", 180.0, 6),
    STAGIONALE("Stagionale", 450.0, -1);

    private final String etichetta;
    private final double prezzo;
    private final int giorniAggiuntivi;

    TipoSkipass(String etichetta, double prezzo, int giorniAggiuntivi) {
        this.etichetta = etichetta;
        this.prezzo = prezzo;
        this.giorniAggiuntivi = giorniAggiuntivi;
    }

    public String getEtichetta() {
        return etichetta;
    }

    public double getPrezzo() {
        return prezzo;
    }

    /** -1 indica scadenza legata alla fine stagione, non a un numero di giorni. */
    public int getGiorniAggiuntivi() {
        return giorniAggiuntivi;
    }
}
