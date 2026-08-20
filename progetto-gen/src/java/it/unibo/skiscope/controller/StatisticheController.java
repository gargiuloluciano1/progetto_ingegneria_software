package it.unibo.skiscope.controller;

import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.Lezione;
import it.unibo.skiscope.model.Parcheggio;
import it.unibo.skiscope.model.RichiestaSoccorso;
import it.unibo.skiscope.model.StatoLezione;
import it.unibo.skiscope.model.StatoRichiesta;

/**
 * Controller per il caso d'uso "VisualizzaStatistiche" (RFG2), riservato al
 * Presidente. Le statistiche sono filtrabili per giorno o per periodo
 * (il filtro è dimostrativo in questo prototipo: i contatori sono
 * comunque calcolati in tempo reale sui dati correnti, RNF2).
 */
public class StatisticheController {

    private final DataStore db = DataStore.getInstance();

    public static final class Statistiche {
        public final int postiOccupati;
        public final int postiTotali;
        public final int skipassVenduti;
        public final int lezioniSvolte;
        public final int lezioniTotali;
        public final int richiesteSoccorsoAttive;

        Statistiche(int postiOccupati, int postiTotali, int skipassVenduti, int lezioniSvolte,
                    int lezioniTotali, int richiesteSoccorsoAttive) {
            this.postiOccupati = postiOccupati;
            this.postiTotali = postiTotali;
            this.skipassVenduti = skipassVenduti;
            this.lezioniSvolte = lezioniSvolte;
            this.lezioniTotali = lezioniTotali;
            this.richiesteSoccorsoAttive = richiesteSoccorsoAttive;
        }
    }

    public Statistiche generaStatistiche(String filtroPeriodo) {
        int occupati = 0;
        int totali = 0;
        for (Parcheggio p : db.getParcheggi()) {
            occupati += p.getOccupati();
            totali += p.getCapacita();
        }

        int lezioniSvolte = 0;
        for (Lezione l : db.getLezioni()) {
            if (l.getStato() == StatoLezione.PRENOTATA) {
                lezioniSvolte++;
            }
        }

        int richiesteAttive = 0;
        for (RichiestaSoccorso r : db.getRichiesteSoccorso()) {
            if (r.getStato() == StatoRichiesta.APERTA) {
                richiesteAttive++;
            }
        }

        return new Statistiche(occupati, totali, db.getSkipassVenduti().size(), lezioniSvolte,
                db.getLezioni().size(), richiesteAttive);
    }
}
