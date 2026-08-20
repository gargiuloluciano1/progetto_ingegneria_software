package it.unibo.skiscope.model;

/**
 * Classe astratta base per qualunque utente registrato in SkiScope
 * (Cliente oppure Operatore: Maestro di Sci, Soccorritore, Amministratore).
 */
public abstract class UtenteRegistrato {

    private final String username;
    private final String nome;
    private final String cognome;

    protected UtenteRegistrato(String username, String nome, String cognome) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
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
}
