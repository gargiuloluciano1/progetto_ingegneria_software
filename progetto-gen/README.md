# SkiScope — Prototipo Java (Swing)

Prototipo desktop dell'applicazione SkiScope per la gestione del comprensorio
sciistico del Cimone, realizzato in Java standard (Swing) seguendo
l'architettura MVC e i requisiti funzionali descritti nel documento di
progetto (RFG, RFP, RFS, RFM, RFSC).

## Struttura del progetto

```
src/java/it/unibo/skiscope/
  Main.java                     Punto di ingresso
  Session.java                  Sessione dell'utente autenticato

  model/                        Dominio (Cliente, MaestroDiSci, Soccorritore,
                                 Amministratore, Skipass, Lezione, Pista,
                                 RichiestaSoccorso, enum di stato, ...)

  data/DataStore.java            Persistenza mock in memoria (singleton),
                                 con dati dimostrativi precaricati

  controller/                   Un Controller per ciascun caso d'uso
                                 (RegistrazioneController, AutenticazioneController,
                                 PrenotazioneParcheggioController,
                                 AcquistoSkipassController, PrenotaLezioneController,
                                 GestioneDisponibilitaController,
                                 RichiestaSoccorsoController, GestioneSoccorsoController,
                                 GestionePisteController, GestioneStagioneController,
                                 StatisticheController)

  ui/                            Interfaccia Swing
    AppFrame.java                Finestra principale, navigazione a CardLayout
    LandingPanel.java            Pagina pubblica (RFG1): le 5 sezioni
    AuthPanel.java                Login / Registrazione + accesso rapido demo
    UiTheme.java                  Palette colori, font, componenti di stile comuni
    CardLayoutTabs.java, FormFields.java   Componenti riutilizzabili

    cliente/                     Area Cliente: Parcheggio, Skipass, Lezioni, Soccorso
    maestro/                     Area Maestro di Sci: gestione disponibilità
    soccorritore/                Area Soccorritore: gestione richieste di soccorso
    admin/                       Area Presidente: Stagione, Piste, Statistiche
```

La sorgente è sotto `src/java/...` (non `src/...` direttamente) per essere
compatibile con setup di build tipo Docker che si aspettano quella
convenzione (vedi sotto).

## Come compilare ed eseguire (da terminale)

Serve un JDK 11 o superiore (l'app usa solo API standard: Swing, java.time,
java.util — nessuna libreria esterna, JavaFX non necessario).

Dalla cartella `skiscope-java`:

```bash
# Compilazione (find gestisce automaticamente qualunque profondità di package)
find src/java -name "*.java" > sources.txt
javac -d out @sources.txt

# Esecuzione
java -cp out it.unibo.skiscope.Main
```

In alternativa, importa la cartella `src/java` come sorgente in un IDE
(IntelliJ IDEA, Eclipse, NetBeans) e imposta `it.unibo.skiscope.Main` come
classe di avvio.

## Compilazione con Docker

Il repository include un `Dockerfile` di esempio, basato su un'immagine
`ingsof-image` con supporto JavaFX. Attenzione a due punti se lo riusi in
un contesto diverso:

- Il comando di compilazione usa `find src/java -name "*.java"` per
  raccogliere ricorsivamente tutti i sorgenti, indipendentemente dalla
  profondità dei package (il progetto usa `it.unibo.skiscope.ui.cliente`
  e simili, fino a 5 livelli). Un semplice glob `src/java/*/*.java` NON
  è sufficiente.
- La classe di avvio è impostata su `it.unibo.skiscope.Main` (non
  `HelloWorld`).
- Il progetto è scritto in **Swing**, non JavaFX: la componente JavaFX
  scaricata dal Dockerfile resta semplicemente inutilizzata a runtime,
  ma non causa errori di compilazione.

## Account demo

All'apertura, dalla schermata "Accedi / Registrati" sono disponibili
pulsanti di accesso rapido per ciascun ruolo, oppure le seguenti credenziali:

| Ruolo         | Username     | Password      |
|---------------|--------------|---------------|
| Cliente       | mrossi       | Password1!    |
| Maestro       | lbianchi     | Maestro1!     |
| Soccorritore  | averdi       | Soccorso1!    |
| Presidente    | presidente   | CimonePwd!1   |

## Cosa fa davvero (non solo messaggi di conferma)

Ogni operazione richiamata dall'interfaccia esegue la logica applicativa
reale definita nei Controller e aggiorna lo stato condiviso in `DataStore`,
poi conferma con una finestra di dialogo "Operazione completata":

- **Parcheggio**: calcola la tariffa (1,50€/h, tetto 6€/giorno), verifica la
  capacità della località scelta, registra la sosta.
- **Skipass**: valida i campi obbligatori, calcola la scadenza in base al
  tipo, genera un codice QR univoco visualizzabile in un dialog dedicato.
- **Lezioni**: mostra le fasce libere inserite dai maestri, calcola il
  prezzo (singola/gruppo), blocca lo slot una volta prenotato.
- **Disponibilità (Maestro)**: suddivide l'intervallo inserito in blocchi da
  1 ora, impedisce la rimozione di lezioni già prenotate.
- **Soccorso**: invia la richiesta, impedisce richieste duplicate attive,
  permette ai soccorritori la presa in carico con blocco della concorrenza
  (una richiesta non può essere presa in carico due volte).
- **Amministrazione**: gestisce le date di stagione (con blocco delle
  operazioni fuori stagione), apertura/chiusura piste, statistiche calcolate
  in tempo reale sui dati correnti.

## Limiti del prototipo

- Persistenza solo in memoria: i dati si perdono alla chiusura dell'app.
- Pagamenti simulati (nessuna integrazione reale con un PSP esterno).
- Nessuna autenticazione a due fattori, cifratura delle comunicazioni o
  connessione a un vero DBMS: questi aspetti, descritti nell'analisi dei
  rischi del documento di progetto, sono fuori dallo scopo del prototipo.
