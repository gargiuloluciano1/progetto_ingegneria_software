package it.unibo.skiscope.controller;

import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.Cliente;

/**
 * Controller per il caso d'uso "Registrazione" (RFG3).
 */
public class RegistrazioneController {

    private final DataStore db = DataStore.getInstance();

    /**
     * Registra un nuovo cliente, validando univocità di username e codice
     * fiscale e i requisiti della password.
     */
    public Cliente registraUtente(String nome, String cognome, String username, String password,
                                   String codiceFiscale) throws OperazioneException {
        if (nome == null || nome.isBlank() || cognome == null || cognome.isBlank()) {
            throw new OperazioneException("Nome e cognome sono obbligatori.");
        }
        if (username == null || username.isBlank()) {
            throw new OperazioneException("Lo username non può essere vuoto.");
        }
        if (db.usernameGiaUsato(username)) {
            throw new OperazioneException("Username già in uso. Scegline un altro.");
        }
        if (codiceFiscale == null || codiceFiscale.isBlank()) {
            throw new OperazioneException("Il codice fiscale è obbligatorio.");
        }
        if (db.codiceFiscaleGiaRegistrato(codiceFiscale)) {
            throw new OperazioneException("Codice fiscale già registrato. Effettua l'accesso.");
        }
        if (!Cliente.isPasswordValida(password)) {
            throw new OperazioneException(
                    "Password non valida: 8-16 caratteri, almeno una maiuscola e un carattere speciale.");
        }

        Cliente nuovoCliente = new Cliente(nome, cognome, username, codiceFiscale.toUpperCase(), password);
        db.getClienti().add(nuovoCliente);
        return nuovoCliente;
    }
}
