package it.unibo.skiscope.ui.admin;

import it.unibo.skiscope.controller.StatisticheController;
import it.unibo.skiscope.ui.UiTheme;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

/**
 * RFG2: statistiche generali del comprensorio (parcheggi, skipass venduti,
 * lezioni svolte, richieste di soccorso correnti), filtrabili per giorno o
 * per periodo (RNF2: i dati sono aggiornati in tempo reale).
 */
public class StatistichePanel extends JPanel {

    private final StatisticheController controller = new StatisticheController();
    private final JPanel pillRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    private final JPanel statsGrid = new JPanel(new GridLayout(1, 4, 16, 0));
    private String filtroSelezionato = "oggi";

    public StatistichePanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        pillRow.setOpaque(false);
        pillRow.setAlignmentX(0f);
        statsGrid.setOpaque(false);
        statsGrid.setAlignmentX(0f);
        statsGrid.setMaximumSize(new Dimension(900, 140));


        wrapper.add(pillRow);
        wrapper.add(javax.swing.Box.createVerticalStrut(16));
        wrapper.add(statsGrid);


        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    public void refresh() {
        pillRow.removeAll();
        pillRow.add(pill("Oggi", "oggi"));
        pillRow.add(pill("Questa settimana", "settimana"));
        pillRow.add(pill("Intera stagione", "stagione"));

        StatisticheController.Statistiche s = controller.generaStatistiche(filtroSelezionato);
        statsGrid.removeAll();
        statsGrid.add(statCard("PARCHEGGIO", s.postiOccupati + "/" + s.postiTotali, "Posti occupati sul totale",
                UiTheme.NIGHT));
        statsGrid.add(statCard("SKIPASS VENDUTI", String.valueOf(s.skipassVenduti), "Nel periodo selezionato",
                UiTheme.NIGHT));
        statsGrid.add(statCard("LEZIONI SVOLTE", s.lezioniSvolte + "/" + s.lezioniTotali, "Su slot totali",
                UiTheme.NIGHT));
        statsGrid.add(statCard("SOCCORSI CORRENTI", String.valueOf(s.richiesteSoccorsoAttive),
                "Richieste in attesa", s.richiesteSoccorsoAttive > 0 ? UiTheme.SLOPE_RED : UiTheme.NIGHT));

        revalidate();
        repaint();
    }

    private JButton pill(String etichetta, String valore) {
        boolean attivo = valore.equals(filtroSelezionato);
        JButton button = attivo ? UiTheme.nightButton(etichetta) : UiTheme.outlineButton(etichetta);
        button.addActionListener(e -> {
            filtroSelezionato = valore;
            refresh();
        });
        return button;
    }

    private JPanel statCard(String label, String numero, String sottotitolo, java.awt.Color numeroColore) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UiTheme.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(UiTheme.LINE, 1, true), new EmptyBorder(18, 18, 18, 18)));

        JLabel lbl = new JLabel(label);
        lbl.setFont(UiTheme.body(11, true));
        lbl.setForeground(UiTheme.SLATE);
        lbl.setAlignmentX(0f);

        JLabel num = new JLabel(numero);
        num.setFont(UiTheme.display(30));
        num.setForeground(numeroColore);
        num.setBorder(new EmptyBorder(6, 0, 4, 0));
        num.setAlignmentX(0f);

        JLabel sub = new JLabel(sottotitolo);
        sub.setFont(UiTheme.body(11, false));
        sub.setForeground(UiTheme.SLATE);
        sub.setAlignmentX(0f);

        card.add(lbl);
        card.add(num);
        card.add(sub);
        return card;
    }
}
