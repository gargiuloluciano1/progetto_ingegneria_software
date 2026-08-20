package it.unibo.skiscope.model;

/**
 * Soccorritore: visualizza e prende in carico le richieste di soccorso
 * (RFSC2, RFSC3).
 */
public class Soccorritore extends Operatore {

    public Soccorritore(String nome, String cognome, String username, String password) {
        super(username, nome, cognome, password);
    }
}
