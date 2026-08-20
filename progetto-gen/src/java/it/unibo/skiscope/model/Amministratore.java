package it.unibo.skiscope.model;

/**
 * Amministratore (Presidente del comprensorio): gestisce stagione, piste
 * e visualizza le statistiche generali (RFG2). Le credenziali sono
 * concordate direttamente con gli sviluppatori.
 */
public class Amministratore extends UtenteRegistrato {

    private final String password;

    public Amministratore(String nome, String cognome, String username, String password) {
        super(username, nome, cognome);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
