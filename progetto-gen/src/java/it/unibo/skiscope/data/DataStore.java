package it.unibo.skiscope.data;

import it.unibo.skiscope.model.Amministratore;
import it.unibo.skiscope.model.Cliente;
import it.unibo.skiscope.model.Difficolta;
import it.unibo.skiscope.model.Lezione;
import it.unibo.skiscope.model.MaestroDiSci;
import it.unibo.skiscope.model.Parcheggio;
import it.unibo.skiscope.model.Pista;
import it.unibo.skiscope.model.RichiestaSoccorso;
import it.unibo.skiscope.model.Skipass;
import it.unibo.skiscope.model.Soccorritore;
import it.unibo.skiscope.model.Sosta;
import it.unibo.skiscope.model.Stagione;
import it.unibo.skiscope.model.StatoPista;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simula il "Sistema informativo esterno" (DBMS) descritto nel documento di
 * progetto: mantiene in memoria tutte le informazioni persistenti
 * dell'applicazione. In un sistema reale queste liste sarebbero sostituite
 * da query verso un vero DBMS esterno (vedi Architettura Logica, Package
 * "Persistenza").
 *
 * Implementata come singleton per essere condivisa da tutti i Controller.
 */
public final class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    private final List<Cliente> clienti = new ArrayList<>();
    private final List<MaestroDiSci> maestri = new ArrayList<>();
    private final List<Soccorritore> soccorritori = new ArrayList<>();
    private Amministratore amministratore;

    // Stagione demo centrata sulla data odierna, cosi' isAttiva() risulta vera
    // (in base al controllo reale sulle date) indipendentemente da quando
    // l'applicazione viene effettivamente eseguita.
    private final Stagione stagione = new Stagione(LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(5));

    private final List<Parcheggio> parcheggi = new ArrayList<>();
    private final List<Sosta> soste = new ArrayList<>();

    private final List<Pista> piste = new ArrayList<>();

    private final List<Skipass> skipassVenduti = new ArrayList<>();

    private final List<Lezione> lezioni = new ArrayList<>();

    private final List<RichiestaSoccorso> richiesteSoccorso = new ArrayList<>();

    private final AtomicInteger contatoreId = new AtomicInteger(1000);

    private DataStore() {
        seed();
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    /** Genera un identificativo univoco per le nuove entità create a runtime. */
    public String nuovoId(String prefisso) {
        return prefisso + "-" + contatoreId.incrementAndGet();
    }

    private void seed() {
        // Utenti demo, coerenti con i test JUnit del documento di progetto.
        clienti.add(new Cliente("Mario", "Rossi", "mrossi", "RSSMRA90A01H501X", "Password1!"));
        maestri.add(new MaestroDiSci("Luca", "Bianchi", "lbianchi", "Maestro1!"));
        soccorritori.add(new Soccorritore("Anna", "Verdi", "averdi", "Soccorso1!"));
        amministratore = new Amministratore("Presidente", "Cimone", "presidente", "CimonePwd!1");

        // Le quattro località di accesso al comprensorio (RFP2).
        parcheggi.add(new Parcheggio("lupo", "Passo del Lupo", 50, 32));
        parcheggi.add(new Parcheggio("ninfa", "Lago della Ninfa", 40, 40));
        parcheggi.add(new Parcheggio("cimoncino", "Cimoncino", 60, 18));
        parcheggi.add(new Parcheggio("polle", "Le Polle", 30, 9));

        // Piste: inizialmente tutte chiuse (RFG2), qui alcune aperte per la demo.
        piste.add(new Pista(1, "Cima Tauffi", Difficolta.ROSSA, StatoPista.CHIUSA));
        piste.add(new Pista(2, "Pian del Falco", Difficolta.AZZURRA, StatoPista.APERTA));
        piste.add(new Pista(3, "Cimoncino", Difficolta.VERDE, StatoPista.APERTA));
        piste.add(new Pista(4, "Canalone", Difficolta.NERA, StatoPista.CHIUSA));
        piste.add(new Pista(5, "Le Polle", Difficolta.AZZURRA, StatoPista.CHIUSA));

        // Disponibilità dimostrativa del maestro Luca Bianchi (RFM1).
        LocalDate data = LocalDate.of(2027, 1, 10);
        for (int h = 9; h < 13; h++) {
            lezioni.add(new Lezione(nuovoId("lz"), "lbianchi", "Luca Bianchi",
                    data, LocalTime.of(h, 0), LocalTime.of(h + 1, 0)));
        }
    }

    public List<Cliente> getClienti() {
        return clienti;
    }

    public List<MaestroDiSci> getMaestri() {
        return maestri;
    }

    public List<Soccorritore> getSoccorritori() {
        return soccorritori;
    }

    public Amministratore getAmministratore() {
        return amministratore;
    }

    public Stagione getStagione() {
        return stagione;
    }

    public List<Parcheggio> getParcheggi() {
        return parcheggi;
    }

    public List<Sosta> getSoste() {
        return soste;
    }

    public List<Pista> getPiste() {
        return piste;
    }

    public List<Skipass> getSkipassVenduti() {
        return skipassVenduti;
    }

    public List<Lezione> getLezioni() {
        return lezioni;
    }

    public List<RichiestaSoccorso> getRichiesteSoccorso() {
        return richiesteSoccorso;
    }

    public Cliente trovaClientePerUsername(String username) {
        for (Cliente c : clienti) {
            if (c.getUsername().equals(username)) {
                return c;
            }
        }
        return null;
    }

    public boolean usernameGiaUsato(String username) {
        if (trovaClientePerUsername(username) != null) {
            return true;
        }
        for (MaestroDiSci m : maestri) {
            if (m.getUsername().equals(username)) {
                return true;
            }
        }
        for (Soccorritore s : soccorritori) {
            if (s.getUsername().equals(username)) {
                return true;
            }
        }
        return amministratore.getUsername().equals(username);
    }

    public boolean codiceFiscaleGiaRegistrato(String codiceFiscale) {
        for (Cliente c : clienti) {
            if (c.getCodiceFiscale().equalsIgnoreCase(codiceFiscale)) {
                return true;
            }
        }
        return false;
    }

    public Parcheggio trovaParcheggio(String id) {
        for (Parcheggio p : parcheggi) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public Pista trovaPista(int id) {
        for (Pista p : piste) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public Lezione trovaLezione(String id) {
        for (Lezione l : lezioni) {
            if (l.getId().equals(id)) {
                return l;
            }
        }
        return null;
    }

    public RichiestaSoccorso trovaRichiesta(String id) {
        for (RichiestaSoccorso r : richiesteSoccorso) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }
}
