package it.unibo.skiscope.ui.admin;

import it.unibo.skiscope.controller.GestioneStagioneController;
import it.unibo.skiscope.controller.GestionePisteController;
import it.unibo.skiscope.controller.OperazioneException;
import it.unibo.skiscope.model.Difficolta;
import it.unibo.skiscope.model.Pista;
import it.unibo.skiscope.model.StatoPista;
import it.unibo.skiscope.ui.UiTheme;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.List;

/**
 * RFG2: il Presidente decide quali piste aprire e quali chiudere.
 * L'operazione e' consentita solo durante la stagione attiva.
 */
public class PistePanel extends JPanel {

    private final GestionePisteController controller = new GestionePisteController();
    private final JPanel content = new JPanel(new GridLayout(0, 3, 16, 16));
    private final JLabel avviso = new JLabel();
    private final JPanel riepilogoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

    public PistePanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JLabel titolo = new JLabel("GESTIONE PISTE");
        titolo.setFont(UiTheme.body(12, true));
        titolo.setForeground(UiTheme.SLATE);
        titolo.setAlignmentX(0f);

        riepilogoRow.setOpaque(false);
        riepilogoRow.setAlignmentX(0f);
        riepilogoRow.setBorder(new EmptyBorder(10, 0, 4, 0));

        avviso.setFont(UiTheme.body(12, true));
        avviso.setForeground(UiTheme.SUN);
        avviso.setAlignmentX(0f);
        avviso.setBorder(new EmptyBorder(4, 0, 14, 0));

        content.setOpaque(false);
        content.setAlignmentX(0f);

        wrapper.add(titolo);
        wrapper.add(riepilogoRow);
        wrapper.add(avviso);
        wrapper.add(content);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    public void refresh() {
        boolean attiva = new GestioneStagioneController().isStagioneAttiva();
        avviso.setVisible(!attiva);
        avviso.setText("Apertura/chiusura piste possibile solo durante la stagione attiva.");

        List<Pista> piste = controller.getPiste();
        int aperte = 0;
        for (Pista p : piste) {
            if (p.getStato() == StatoPista.APERTA) {
                aperte++;
            }
        }
        riepilogoRow.removeAll();
        riepilogoRow.add(riepilogoChip(aperte + " / " + piste.size() + " piste aperte",
                aperte > 0 ? UiTheme.PINE_LIGHT : UiTheme.SNOW_2, aperte > 0 ? UiTheme.PINE : UiTheme.SLATE));

        content.removeAll();
        for (Pista p : piste) {
            content.add(buildPistaCard(p, attiva));
        }
        revalidate();
        repaint();
    }

    private JLabel riepilogoChip(String testo, Color bg, Color fg) {
        JLabel chip = new JLabel(testo);
        chip.setOpaque(true);
        chip.setBackground(bg);
        chip.setForeground(fg);
        chip.setFont(UiTheme.body(12, true));
        chip.setBorder(new EmptyBorder(6, 14, 6, 14));
        return chip;
    }

    private JPanel buildPistaCard(Pista p, boolean stagioneAttiva) {
        boolean aperta = p.getStato() == StatoPista.APERTA;
        Color coloreDifficolta = coloreDifficolta(p.getDifficolta());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UiTheme.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(UiTheme.LINE, 1, true), new EmptyBorder(16, 16, 16, 16)));
        card.setPreferredSize(new Dimension(240, 200));
        if (!aperta) {
            card.setBackground(UiTheme.SNOW_2);
        }

        JPanel top = new JPanel(new BorderLayout(10, 0));
        top.setOpaque(false);
        top.setAlignmentX(0f);

        SlopeIcon icona = new SlopeIcon(coloreDifficolta, aperta);
        top.add(icona, BorderLayout.WEST);

        JPanel testi = new JPanel();
        testi.setOpaque(false);
        testi.setLayout(new BoxLayout(testi, BoxLayout.Y_AXIS));
        JLabel nome = new JLabel(p.getNome());
        nome.setFont(UiTheme.body(14, true));
        nome.setForeground(UiTheme.NIGHT);
        nome.setAlignmentX(0f);
        JLabel difficoltaLabel = new JLabel(etichettaDifficolta(p.getDifficolta()));
        difficoltaLabel.setFont(UiTheme.body(11, false));
        difficoltaLabel.setForeground(coloreDifficolta);
        difficoltaLabel.setAlignmentX(0f);
        testi.add(nome);
        testi.add(difficoltaLabel);
        top.add(testi, BorderLayout.CENTER);

        JPanel statoRow = new JPanel(new BorderLayout());
        statoRow.setOpaque(false);
        statoRow.setBorder(new EmptyBorder(14, 0, 12, 0));
        statoRow.add(aperta ? UiTheme.badgeAperta() : UiTheme.badgeChiusa(), BorderLayout.WEST);
        statoRow.setAlignmentX(0f);

        JButton toggle = aperta ? UiTheme.dangerButton("Chiudi pista") : UiTheme.primaryButton("Apri pista");
        toggle.setAlignmentX(0f);
        toggle.setEnabled(stagioneAttiva);
        toggle.addActionListener(e -> {
            try {
                if (aperta) {
                    controller.chiudiPista(p.getId());
                } else {
                    controller.apriPista(p.getId());
                }
                UiTheme.mostraSuccesso(this, p.getNome() + ": ora "
                        + (p.getStato() == StatoPista.APERTA ? "aperta" : "chiusa") + ".");
                refresh();
            } catch (OperazioneException ex) {
                UiTheme.mostraErrore(this, ex.getMessage());
            }
        });

        card.add(top);
        card.add(statoRow);
        card.add(toggle);
        return card;
    }

    private Color coloreDifficolta(Difficolta difficolta) {
        switch (difficolta) {
            case VERDE: return UiTheme.VERDE_PISTA;
            case AZZURRA: return UiTheme.AZZURRA;
            case ROSSA: return UiTheme.SLOPE_RED;
            case NERA: return UiTheme.NERA_PISTA;
            default: return UiTheme.SLATE;
        }
    }

    private String etichettaDifficolta(Difficolta difficolta) {
        switch (difficolta) {
            case VERDE: return "Pista verde - facile";
            case AZZURRA: return "Pista azzurra - media";
            case ROSSA: return "Pista rossa - impegnativa";
            case NERA: return "Pista nera - esperti";
            default: return difficolta.name();
        }
    }

    /**
     * Piccola icona decorativa che rappresenta una montagna innevata,
     * colorata in base alla difficolta' della pista. Se la pista e' chiusa
     * viene disegnata in scala grigia per rinforzare visivamente lo stato.
     */
    private static final class SlopeIcon extends JPanel {

        private final Color colore;
        private final boolean attiva;

        SlopeIcon(Color colore, boolean attiva) {
            this.colore = colore;
            this.attiva = attiva;
            setOpaque(false);
            setPreferredSize(new Dimension(48, 48));
            setMinimumSize(new Dimension(48, 48));
            setMaximumSize(new Dimension(48, 48));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            Color base = attiva ? colore : UiTheme.SLATE_LIGHT;

            g2.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), 28));
            g2.fillRoundRect(0, 0, w - 1, h - 1, 14, 14);
            g2.setColor(base);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 14, 14);

            // Montagna principale
            Polygon monte = new Polygon();
            monte.addPoint(8, h - 12);
            monte.addPoint(w / 2, 12);
            monte.addPoint(w - 8, h - 12);
            g2.setColor(base);
            g2.fillPolygon(monte);

            // Cima innevata
            Polygon neve = new Polygon();
            neve.addPoint(w / 2, 12);
            neve.addPoint(w / 2 - 7, 24);
            neve.addPoint(w / 2 + 7, 24);
            g2.setColor(UiTheme.WHITE);
            g2.fillPolygon(neve);

            g2.dispose();
        }
    }
}
