package it.unibo.skiscope.ui;

import it.unibo.skiscope.controller.GestioneStagioneController;
import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.Difficolta;
import it.unibo.skiscope.model.Lezione;
import it.unibo.skiscope.model.Parcheggio;
import it.unibo.skiscope.model.Pista;
import it.unibo.skiscope.model.StatoLezione;
import it.unibo.skiscope.model.StatoPista;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * RFG1: al primo accesso, qualunque utente visualizza le sezioni Parcheggio,
 * Skipass, Lezioni, Soccorso e Autenticazione.
 */
public class LandingPanel extends JPanel {

    private final AppFrame appFrame;
    private final JPanel heroStatsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
    private final JPanel pisteRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
    private final JLabel stagioneStato = new JLabel();

    public LandingPanel(AppFrame appFrame) {
        this.appFrame = appFrame;
        setLayout(new BorderLayout());
        setBackground(UiTheme.SNOW);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UiTheme.SNOW);
        content.setBorder(new EmptyBorder(0, 0, 40, 0));

        content.add(buildHero());
        content.add(buildSections());
        content.add(buildPisteBanner());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildHero() {
        JPanel hero = new JPanel(new BorderLayout());
        hero.setBackground(UiTheme.NIGHT);
        hero.setBorder(new EmptyBorder(48, 32, 40, 32));

        JPanel textCol = new JPanel();
        textCol.setOpaque(false);
        textCol.setLayout(new BoxLayout(textCol, BoxLayout.Y_AXIS));

        JLabel eyebrow = new JLabel("COMPRENSORIO SCIISTICO - SESTOLA");
        eyebrow.setForeground(UiTheme.ICE);
        eyebrow.setFont(UiTheme.body(12, true));
        eyebrow.setAlignmentX(0f);

        JLabel h1a = new JLabel("Ogni funzionalità,");
        h1a.setForeground(Color.WHITE);
        h1a.setFont(UiTheme.display(42));
        h1a.setAlignmentX(0f);
        JLabel h1b = new JLabel("a portata di mano.");
        h1b.setForeground(UiTheme.ICE);
        h1b.setFont(UiTheme.display(42));
        h1b.setAlignmentX(0f);

        JLabel p = new JLabel("<html><div style='width:420px;'>Parcheggio, skipass, lezioni con i maestri "
                + "e soccorso in pista: tutto il comprensorio del Cimone in un'unica applicazione.</div></html>");
        p.setForeground(new Color(0xC9, 0xD6, 0xE3));
        p.setFont(UiTheme.body(14, false));
        p.setBorder(new EmptyBorder(14, 0, 20, 0));
        p.setAlignmentX(0f);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(0f);
        javax.swing.JButton start = UiTheme.sunButton("Inizia ora");
        start.addActionListener(e -> appFrame.showAuth());
        actions.add(start);

        textCol.add(eyebrow);
        textCol.add(javax.swing.Box.createVerticalStrut(8));
        textCol.add(h1a);
        textCol.add(h1b);
        textCol.add(p);
        textCol.add(actions);

        heroStatsRow.setOpaque(false);
        heroStatsRow.setBorder(new EmptyBorder(30, 0, 0, 0));

        hero.add(textCol, BorderLayout.WEST);
        hero.add(heroStatsRow, BorderLayout.SOUTH);
        return hero;
    }

    private JPanel statBadge(String numero, String label) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setOpaque(true);
        box.setBackground(UiTheme.NIGHT_2);
        box.setBorder(new EmptyBorder(10, 16, 10, 16));
        JLabel num = new JLabel(numero);
        num.setFont(UiTheme.display(22));
        num.setForeground(Color.WHITE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(UiTheme.body(10, false));
        lbl.setForeground(UiTheme.SLATE_LIGHT);
        box.add(num);
        box.add(lbl);
        return box;
    }

    private JPanel buildSections() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(UiTheme.SNOW);
        wrap.setBorder(new EmptyBorder(36, 32, 20, 32));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        JLabel titolo = UiTheme.titolo("Cosa vuoi fare oggi?", 26);
        stagioneStato.setFont(UiTheme.body(12, true));
        titleRow.add(titolo, BorderLayout.WEST);
        titleRow.add(stagioneStato, BorderLayout.EAST);
        titleRow.setBorder(new EmptyBorder(0, 0, 16, 0));

        JPanel grid = new JPanel(new GridLayout(1, 5, 14, 0));
        grid.setOpaque(false);
        grid.add(trailCard("Parcheggio", "Prenota e paga la sosta in una delle 4 aree di accesso.",
                UiTheme.PINE, "parcheggio"));
        grid.add(trailCard("Skipass", "Acquista lo Skipass e ricevi il codice QR.",
                UiTheme.SUN, "skipass"));
        grid.add(trailCard("Lezioni", "Prenota una lezione singola o di gruppo con un maestro.",
                UiTheme.ICE_DARK, "lezioni"));
        grid.add(trailCard("Soccorso", "Richiedi assistenza in pista in caso di infortunio.",
                UiTheme.SLOPE_RED, "soccorso"));
        grid.add(accessoCard());

        wrap.add(titleRow, BorderLayout.NORTH);
        wrap.add(grid, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel trailCard(String titolo, String descrizione, Color accento, String tabTarget) {
        JPanel card = UiTheme.card();
        card.setLayout(new BorderLayout());
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.MatteBorder(0, 4, 0, 0, accento),
                new EmptyBorder(16, 14, 16, 14)));

        JLabel t = new JLabel(titolo);
        t.setFont(UiTheme.display(18));
        t.setForeground(UiTheme.NIGHT);
        JLabel d = new JLabel("<html><div style='width:150px;'>" + descrizione + "</div></html>");
        d.setFont(UiTheme.body(11, false));
        d.setForeground(UiTheme.SLATE);
        d.setBorder(new EmptyBorder(8, 0, 0, 0));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.add(t);
        inner.add(d);
        card.add(inner, BorderLayout.NORTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                appFrame.showAuthOrSectionForCliente(tabTarget);
            }
        });
        return card;
    }

    private JPanel accessoCard() {
        JPanel card = UiTheme.card();
        card.setLayout(new BorderLayout());
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.MatteBorder(0, 4, 0, 0, UiTheme.ICE),
                new EmptyBorder(16, 14, 16, 14)));

        JLabel t = new JLabel("Accesso");
        t.setFont(UiTheme.display(18));
        t.setForeground(UiTheme.NIGHT);
        JLabel d = new JLabel("<html><div style='width:150px;'>Accedi come cliente, maestro, "
                + "soccorritore o presidente.</div></html>");
        d.setFont(UiTheme.body(11, false));
        d.setForeground(UiTheme.SLATE);
        d.setBorder(new EmptyBorder(8, 0, 0, 0));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.add(t);
        inner.add(d);
        card.add(inner, BorderLayout.NORTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                appFrame.showAuth();
            }
        });
        return card;
    }

    private JPanel buildPisteBanner() {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(UiTheme.NIGHT);
        banner.setBorder(new EmptyBorder(20, 32, 30, 32));

        JLabel titolo = new JLabel("STATO PISTE IN TEMPO REALE");
        titolo.setForeground(UiTheme.ICE);
        titolo.setFont(UiTheme.body(12, true));

        pisteRow.setOpaque(false);

        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(titolo);
        col.add(pisteRow);

        banner.add(col, BorderLayout.CENTER);
        return banner;
    }

    /** Ricarica dati aggiornati dal DataStore (parcheggi, piste, lezioni, stagione). */
    public void refresh() {
        DataStore db = DataStore.getInstance();

        heroStatsRow.removeAll();
        int occ = 0;
        int cap = 0;
        for (Parcheggio p : db.getParcheggi()) {
            occ += p.getOccupati();
            cap += p.getCapacita();
        }
        int pisteAperte = 0;
        for (Pista p : db.getPiste()) {
            if (p.getStato() == StatoPista.APERTA) {
                pisteAperte++;
            }
        }
        int lezioniLibere = 0;
        for (Lezione l : db.getLezioni()) {
            if (l.getStato() == StatoLezione.LIBERA) {
                lezioniLibere++;
            }
        }
        heroStatsRow.add(statBadge(occ + "/" + cap, "POSTI AUTO OCCUPATI"));
        heroStatsRow.add(statBadge(pisteAperte + "/" + db.getPiste().size(), "PISTE APERTE OGGI"));
        heroStatsRow.add(statBadge(String.valueOf(lezioniLibere), "LEZIONI DISPONIBILI"));

        pisteRow.removeAll();
        for (Pista p : db.getPiste()) {
            JPanel chip = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
            chip.setOpaque(false);
            JLabel nome = new JLabel(p.getNome());
            nome.setForeground(Color.WHITE);
            nome.setFont(UiTheme.body(12, false));
            chip.add(nome);
            chip.add(UiTheme.badgeDifficolta(p.getDifficolta()));
            chip.add(p.getStato() == StatoPista.APERTA ? UiTheme.badgeAperta() : UiTheme.badgeChiusa());
            pisteRow.add(chip);
        }

        boolean attiva = new GestioneStagioneController().isStagioneAttiva();
        stagioneStato.setText(attiva ? "STAGIONE ATTIVA" : "FUORI STAGIONE");
        stagioneStato.setForeground(attiva ? UiTheme.PINE : UiTheme.SLOPE_RED);

        revalidate();
        repaint();
    }
}
