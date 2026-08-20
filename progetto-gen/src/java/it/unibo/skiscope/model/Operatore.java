package it.unibo.skiscope.model;

/**
 * Operatore: specializzazione di UtenteRegistrato per il personale del
 * comprensorio (Maestro di sci, Soccorritore). La creazione delle credenziali
 * è delegata al Presidente/amministratori di sistema, esterna al dominio.
 */
public abstract class Operatore extends UtenteRegistrato {

    private final String password;

    protected Operatore(String username, String nome, String cognome, String password) {
        super(username, nome, cognome);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
