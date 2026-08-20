package it.unibo.skiscope.controller;

/**
 * Eccezione applicativa lanciata dai Controller quando un'operazione non può
 * essere completata (dati non validi, risorsa non disponibile, ecc.).
 * Le View la intercettano per mostrare un messaggio d'errore all'utente.
 */
public class OperazioneException extends Exception {

    public OperazioneException(String messaggio) {
        super(messaggio);
    }
}
