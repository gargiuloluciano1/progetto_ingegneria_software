package it.unibo.skiscope.model;

/** Partecipante (o referente) iscritto a una lezione. */
public class Partecipante {

    private final String nome;
    private final String cognome;
    private final String clienteUsername;

    public Partecipante(String nome, String cognome, String clienteUsername) {
        this.nome = nome;
        this.cognome = cognome;
        this.clienteUsername = clienteUsername;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getClienteUsername() {
        return clienteUsername;
    }

    public String getNomeCompleto() {
        return nome + " " + cognome;
    }
}
