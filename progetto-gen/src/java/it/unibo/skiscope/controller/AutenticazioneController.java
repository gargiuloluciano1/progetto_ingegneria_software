package it.unibo.skiscope.controller;

import it.unibo.skiscope.Session;
import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.Amministratore;
import it.unibo.skiscope.model.Cliente;
import it.unibo.skiscope.model.MaestroDiSci;
import it.unibo.skiscope.model.Soccorritore;

/**
 * Controller per il caso d'uso "Autenticazione" (RFG4). In caso di successo
 * crea la Session dell'utente, che verrà utilizzata per instradarlo verso
 * l'area a lui dedicata in base al ruolo.
 */
public class AutenticazioneController {

    private final DataStore db = DataStore.getInstance();

    public Session autentica(String username, String password) throws OperazioneException {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new OperazioneException("Inserisci username e password.");
        }

        Cliente cliente = db.trovaClientePerUsername(username);
        if (cliente != null && cliente.getPassword().equals(password)) {
            Session s = new Session(cliente.getUsername(), cliente.getNome(), cliente.getCognome(),
                    Session.Ruolo.CLIENTE);
            Session.setCurrent(s);
            return s;
        }

        for (MaestroDiSci m : db.getMaestri()) {
            if (m.getUsername().equals(username) && m.getPassword().equals(password)) {
                Session s = new Session(m.getUsername(), m.getNome(), m.getCognome(), Session.Ruolo.MAESTRO);
                Session.setCurrent(s);
                return s;
            }
        }

        for (Soccorritore so : db.getSoccorritori()) {
            if (so.getUsername().equals(username) && so.getPassword().equals(password)) {
                Session s = new Session(so.getUsername(), so.getNome(), so.getCognome(),
                        Session.Ruolo.SOCCORRITORE);
                Session.setCurrent(s);
                return s;
            }
        }

        Amministratore admin = db.getAmministratore();
        if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
            Session s = new Session(admin.getUsername(), admin.getNome(), admin.getCognome(),
                    Session.Ruolo.AMMINISTRATORE);
            Session.setCurrent(s);
            return s;
        }

        throw new OperazioneException("Credenziali non valide. Riprova oppure registrati.");
    }

    public void logout() {
        Session.clear();
    }
}
