package it.unibo.skiscope.ui.soccorritore;

import it.unibo.skiscope.Session;
import it.unibo.skiscope.controller.GestioneSoccorsoController;
import it.unibo.skiscope.controller.OperazioneException;
import it.unibo.skiscope.model.RichiestaSoccorso;
import it.unibo.skiscope.model.StatoRichiesta;
import it.unibo.skiscope.ui.AppFrame;
import it.unibo.skiscope.ui.UiTheme;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Area riservata al Soccorritore (RFSC2, RFSC3, RFSC5): visualizzazione
 * delle richieste di soccorso attive e presa in carico.
 */
public class SoccorritoreHomePanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final GestioneSoccorsoController controller = new GestioneSoccorsoController();
    private final JLabel sottotitolo = new JLabel();
    private final JPanel content = new JPanel(new GridLayout(1, 2, 24, 0));

    public SoccorritoreHomePanel(AppFrame appFrame) {
        setLayout(new BorderLayout());
        setBackground(UiTheme.SNOW);
        setBorder(new EmptyBorder(30, 32, 20, 32));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel titolo = new JLabel("Area Soccorritore");
        titolo.setFont(UiTheme.display(26));
        titolo.setForeground(UiTheme.NIGHT);
        titolo.setAlignmentX(0f);
        sottotitolo.setFont(UiTheme.body(13, false));
        sottotitolo.setForeground(UiTheme.SLATE);
        sottotitolo.setAlignmentX(0f);
        sottotitolo.setBorder(new EmptyBorder(2, 0, 14, 0));
        header.add(titolo);
        header.add(sottotitolo);

        content.setOpaque(false);
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    public void refresh() {
        Session session = Session.getCurrent();
        if (session != null) {
            sottotitolo.setText("Benvenuto, " + session.getNomeCompleto());
        }
        content.removeAll();
        content.add(buildAperteColumn());
        content.add(buildIncaricoColumn());
        revalidate();
        repaint();
    }

    private JLabel stepLabel(String testo) {
        JLabel label = new JLabel(testo);
        label.setFont(UiTheme.body(12, true));
        label.setForeground(UiTheme.SLATE);
        label.setAlignmentX(0f);
        return label;
    }

    private JPanel buildAperteColumn() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(stepLabel("RICHIESTE ATTIVE (NON ASSEGNATE)"));
        col.add(javax.swing.Box.createVerticalStrut(10));

        List<RichiestaSoccorso> aperte = controller.visualizzaRichiesteAperte();
        if (aperte.isEmpty()) {
            JLabel empty = new JLabel("Nessuna richiesta in attesa.");
            empty.setForeground(UiTheme.SLATE);
            empty.setAlignmentX(0f);
            col.add(empty);
        }
        for (RichiestaSoccorso r : aperte) {
            col.add(buildRichiestaApertaCard(r));
            col.add(javax.swing.Box.createVerticalStrut(8));
        }
        return col;
    }

    private JPanel buildRichiestaApertaCard(RichiestaSoccorso r) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UiTheme.WHITE);
        card.setBorder(new CompoundBorder(
                new MatteBorder(0, 4, 0, 0, UiTheme.SLOPE_RED),
                new EmptyBorder(12, 14, 12, 14)));
        card.setAlignmentX(0f);
        card.setMaximumSize(new Dimension(420, 170));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel luogo = new JLabel(r.getLuogo());
        luogo.setFont(UiTheme.body(13, true));
        luogo.setForeground(UiTheme.NIGHT);
        top.add(luogo, BorderLayout.WEST);
        top.add(UiTheme.badgeAttesa(), BorderLayout.EAST);

        JLabel descr = new JLabel("<html><div style='width:280px;'>" + r.getDescrizione() + "</div></html>");
        descr.setFont(UiTheme.body(11, false));
        descr.setForeground(UiTheme.SLATE);
        descr.setBorder(new EmptyBorder(6, 0, 0, 0));
        descr.setAlignmentX(0f);

        JLabel meta = new JLabel("Cliente: " + r.getClienteNomeCompleto() + "  -  " + r.getDataOraInvio().format(FMT));
        meta.setFont(UiTheme.body(11, false));
        meta.setForeground(UiTheme.SLATE);
        meta.setBorder(new EmptyBorder(4, 0, 8, 0));
        meta.setAlignmentX(0f);

        JButton prendi = UiTheme.dangerButton("Prendi in carico");
        prendi.setAlignmentX(0f);
        prendi.addActionListener(e -> {
            try {
                Session session = Session.getCurrent();
                controller.presaInCarico(r.getId(), session.getUsername(), session.getNomeCompleto());
                UiTheme.mostraSuccesso(this, "Richiesta presa in carico. Gli altri soccorritori sono stati notificati.");
                refresh();
            } catch (OperazioneException ex) {
                UiTheme.mostraErrore(this, ex.getMessage());
                refresh();
            }
        });

        card.add(top);
        card.add(descr);
        card.add(meta);
        card.add(prendi);
        return card;
    }

    private JPanel buildIncaricoColumn() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(stepLabel("PRESE IN CARICO DA ME"));
        col.add(javax.swing.Box.createVerticalStrut(10));

        String username = Session.getCurrent().getUsername();
        boolean trovatoMie = false;
        boolean trovatoAltri = false;
        JPanel altriSection = new JPanel();
        altriSection.setOpaque(false);
        altriSection.setLayout(new BoxLayout(altriSection, BoxLayout.Y_AXIS));

        for (RichiestaSoccorso r : controller.visualizzaTutte()) {
            if (r.getStato() != StatoRichiesta.PRESA_IN_CARICO) {
                continue;
            }
            if (username.equals(r.getSoccorritoreUsername())) {
                trovatoMie = true;
                col.add(buildRichiestaAssegnataCard(r, true));
                col.add(javax.swing.Box.createVerticalStrut(8));
            } else {
                trovatoAltri = true;
                altriSection.add(buildRichiestaAssegnataCard(r, false));
                altriSection.add(javax.swing.Box.createVerticalStrut(8));
            }
        }
        if (!trovatoMie) {
            JLabel empty = new JLabel("Nessun intervento in corso.");
            empty.setForeground(UiTheme.SLATE);
            empty.setAlignmentX(0f);
            col.add(empty);
        }
        if (trovatoAltri) {
            col.add(javax.swing.Box.createVerticalStrut(14));
            JLabel altriTitolo = new JLabel("PRESE IN CARICO DA ALTRI");
            altriTitolo.setFont(UiTheme.body(12, true));
            altriTitolo.setForeground(UiTheme.SLATE);
            altriTitolo.setAlignmentX(0f);
            col.add(altriTitolo);
            col.add(javax.swing.Box.createVerticalStrut(8));
            col.add(altriSection);
        }
        return col;
    }

    private JPanel buildRichiestaAssegnataCard(RichiestaSoccorso r, boolean mia) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UiTheme.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(UiTheme.LINE, 1, true), new EmptyBorder(12, 14, 12, 14)));
        card.setAlignmentX(0f);
        card.setMaximumSize(new Dimension(420, 100));
        if (!mia) {
            card.setBackground(UiTheme.SNOW_2);
        }

        JLabel luogo = new JLabel(r.getLuogo());
        luogo.setFont(UiTheme.body(13, true));
        luogo.setForeground(UiTheme.NIGHT);
        luogo.setAlignmentX(0f);

        JLabel stato = new JLabel(mia ? "Assegnata a te" : "Assegnata a " + r.getSoccorritoreNomeCompleto());
        stato.setFont(UiTheme.body(11, mia));
        stato.setForeground(mia ? UiTheme.PINE : UiTheme.SLATE);
        stato.setBorder(new EmptyBorder(6, 0, 0, 0));
        stato.setAlignmentX(0f);

        card.add(luogo);
        card.add(stato);
        return card;
    }
}
