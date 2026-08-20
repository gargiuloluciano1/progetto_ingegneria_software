package it.unibo.skiscope;

/**
 * Rappresenta la sessione dell'utente attualmente autenticato nel client.
 * Nel diagramma dei componenti del progetto, l'equivalente lato server
 * sarebbe il contesto associato alla richiesta autenticata.
 */
public final class Session {

    /** Ruoli possibili per un utente autenticato (vedi Analisi dei Ruoli). */
    public enum Ruolo {
        CLIENTE, MAESTRO, SOCCORRITORE, AMMINISTRATORE
    }

    private static Session current;

    private final String username;
    private final String nome;
    private final String cognome;
    private final Ruolo ruolo;

    public Session(String username, String nome, String cognome, Ruolo ruolo) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;
    }

    public static Session getCurrent() {
        return current;
    }

    public static void setCurrent(Session session) {
        current = session;
    }

    public static void clear() {
        current = null;
    }

    public String getUsername() {
        return username;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getNomeCompleto() {
        return nome + " " + cognome;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public String getRuoloLabel() {
        switch (ruolo) {
            case CLIENTE: return "Cliente";
            case MAESTRO: return "Maestro di Sci";
            case SOCCORRITORE: return "Soccorritore";
            case AMMINISTRATORE: return "Presidente";
            default: return ruolo.name();
        }
    }
}
