package it.unibo.skiscope.ui.cliente;

import it.unibo.skiscope.Session;
import it.unibo.skiscope.controller.GestioneStagioneController;
import it.unibo.skiscope.controller.OperazioneException;
import it.unibo.skiscope.controller.PrenotazioneParcheggioController;
import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.Parcheggio;
import it.unibo.skiscope.model.Sosta;
import it.unibo.skiscope.ui.FormFields;
import it.unibo.skiscope.ui.UiTheme;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * RFP1-RFP5: prenotazione e pagamento della sosta in una delle quattro
 * località di accesso al comprensorio.
 */
public class ParcheggioPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final PrenotazioneParcheggioController controller = new PrenotazioneParcheggioController();
    private final JPanel content = new JPanel(new GridLayout(1, 2, 24, 0));
    private String selectedParcheggioId;

    public ParcheggioPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        content.setOpaque(false);
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    public void refresh() {
        content.removeAll();
        content.add(buildLeftColumn());
        content.add(buildRightColumn());
        revalidate();
        repaint();
    }

    private JPanel buildLeftColumn() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));

        col.add(stepLabel("1. SCEGLI LA LOCALITA'"));
        col.add(javax.swing.Box.createVerticalStrut(10));

        JPanel grid = new JPanel(new GridLayout(2, 2, 10, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(0f);
        grid.setMaximumSize(new Dimension(520, 200));
        for (Parcheggio p : DataStore.getInstance().getParcheggi()) {
            grid.add(buildLocationCard(p));
        }
        col.add(grid);
        col.add(javax.swing.Box.createVerticalStrut(24));

        boolean stagioneAttiva = new GestioneStagioneController().isStagioneAttiva();
        if (selectedParcheggioId != null) {
            col.add(stepLabel("2. DETTAGLI SOSTA"));
            col.add(javax.swing.Box.createVerticalStrut(10));
            col.add(buildForm(stagioneAttiva));
        } else {
            JLabel hint = new JLabel("Seleziona una localita' per procedere.");
            hint.setForeground(UiTheme.SLATE);
            hint.setAlignmentX(0f);
            col.add(hint);
        }
        return col;
    }

    private JLabel stepLabel(String testo) {
        JLabel label = new JLabel(testo);
        label.setFont(UiTheme.body(12, true));
        label.setForeground(UiTheme.SLATE);
        label.setAlignmentX(0f);
        return label;
    }

    private JPanel buildLocationCard(Parcheggio p) {
        boolean completo = p.isCompleto();
        boolean selezionato = p.getId().equals(selectedParcheggioId);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(selezionato ? new java.awt.Color(0xEA, 0xF3, 0xFB) : UiTheme.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(selezionato ? UiTheme.ICE : UiTheme.LINE, 1, true),
                new EmptyBorder(12, 14, 12, 14)));
        card.setCursor(Cursor.getPredefinedCursor(completo ? Cursor.DEFAULT_CURSOR : Cursor.HAND_CURSOR));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel nome = new JLabel(p.getNome());
        nome.setFont(UiTheme.body(13, true));
        nome.setForeground(UiTheme.NIGHT);
        JLabel occ = new JLabel(p.getOccupati() + "/" + p.getCapacita());
        occ.setFont(UiTheme.body(12, false));
        occ.setForeground(UiTheme.SLATE);
        header.add(nome, BorderLayout.WEST);
        header.add(occ, BorderLayout.EAST);
        header.setAlignmentX(0f);

        JProgressBar bar = new JProgressBar(0, p.getCapacita());
        bar.setValue(p.getOccupati());
        bar.setForeground(completo ? UiTheme.SLOPE_RED : UiTheme.ICE);
        bar.setBorderPainted(false);
        bar.setAlignmentX(0f);
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 6));
        bar.setBorder(new EmptyBorder(6, 0, 0, 0));

        card.add(header);
        card.add(bar);

        if (completo) {
            JLabel full = new JLabel("Completo");
            full.setForeground(UiTheme.SLOPE_RED);
            full.setFont(UiTheme.body(11, true));
            full.setBorder(new EmptyBorder(6, 0, 0, 0));
            full.setAlignmentX(0f);
            card.add(full);
        }

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (completo) {
                    UiTheme.mostraErrore(ParcheggioPanel.this, "Parcheggio completo, scegli un'altra localita'.");
                    return;
                }
                selectedParcheggioId = p.getId();
                refresh();
            }
        });
        return card;
    }

    private JPanel buildForm(boolean stagioneAttiva) {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(UiTheme.WHITE);
        form.setBorder(new CompoundBorder(new LineBorder(UiTheme.LINE, 1, true), new EmptyBorder(16, 16, 16, 16)));
        form.setAlignmentX(0f);
        form.setMaximumSize(new Dimension(480, 260));

        JTextField targaField = new JTextField();
        JSpinner oreSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 24, 1));
        oreSpinner.setEnabled(stagioneAttiva);
        targaField.setEnabled(stagioneAttiva);

        JLabel costoLabel = new JLabel();
        costoLabel.setFont(UiTheme.body(13, true));
        costoLabel.setForeground(UiTheme.ICE_DARK);
        costoLabel.setAlignmentX(0f);
        Runnable aggiornaCosto = () -> {
            int ore = (Integer) oreSpinner.getValue();
            double costo = controller.calcolaCosto(ore);
            costoLabel.setText(String.format(Locale.ITALY,
                    "Costo stimato: %.2f EUR  (1,50 EUR/h, tetto massimo 6 EUR/giorno)", costo));
        };
        oreSpinner.addChangeListener(e -> aggiornaCosto.run());
        aggiornaCosto.run();

        JLabel errore = new JLabel(" ");
        errore.setForeground(UiTheme.SLOPE_RED);
        errore.setFont(UiTheme.body(12, true));
        errore.setAlignmentX(0f);

        JButton paga = UiTheme.sunButton("Paga e conferma sosta");
        paga.setAlignmentX(0f);
        paga.setEnabled(stagioneAttiva);
        paga.addActionListener(e -> {
            try {
                Sosta sosta = controller.prenotaParcheggio(Session.getCurrent().getUsername(),
                        targaField.getText().trim(), selectedParcheggioId, (Integer) oreSpinner.getValue());
                selectedParcheggioId = null;
                UiTheme.mostraSuccesso(this, "Sosta confermata: " + String.format(Locale.ITALY, "%.2f", sosta.getCosto())
                        + " EUR pagati con successo.");
                refresh();
            } catch (OperazioneException ex) {
                errore.setText(ex.getMessage());
            }
        });

        if (!stagioneAttiva) {
            JLabel avviso = new JLabel("Fuori stagione: operazione non disponibile.");
            avviso.setForeground(UiTheme.SUN);
            avviso.setFont(UiTheme.body(12, true));
            avviso.setAlignmentX(0f);
            form.add(avviso);
            form.add(javax.swing.Box.createVerticalStrut(10));
        }

        form.add(FormFields.labeled("Targa veicolo", targaField));
        form.add(javax.swing.Box.createVerticalStrut(12));
        form.add(FormFields.labeled("Ore di sosta", oreSpinner));
        form.add(javax.swing.Box.createVerticalStrut(10));
        form.add(costoLabel);
        form.add(javax.swing.Box.createVerticalStrut(6));
        form.add(errore);
        form.add(javax.swing.Box.createVerticalStrut(10));
        form.add(paga);
        return form;
    }

    private JPanel buildRightColumn() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(stepLabel("LE MIE SOSTE"));
        col.add(javax.swing.Box.createVerticalStrut(10));

        String username = Session.getCurrent().getUsername();
        List<Sosta> mie = DataStore.getInstance().getSoste();

        boolean trovato = false;
        for (int i = mie.size() - 1; i >= 0; i--) {
            Sosta s = mie.get(i);
            if (!s.getClienteUsername().equals(username)) {
                continue;
            }
            trovato = true;
            col.add(buildSostaCard(s));
            col.add(javax.swing.Box.createVerticalStrut(8));
        }
        if (!trovato) {
            JLabel empty = new JLabel("Nessuna sosta registrata.");
            empty.setForeground(UiTheme.SLATE);
            empty.setAlignmentX(0f);
            col.add(empty);
        }
        return col;
    }

    private JPanel buildSostaCard(Sosta s) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UiTheme.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(UiTheme.LINE, 1, true), new EmptyBorder(12, 14, 12, 14)));
        card.setAlignmentX(0f);
        card.setMaximumSize(new Dimension(420, 120));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel targa = new JLabel(s.getTarga());
        targa.setFont(UiTheme.mono(13));
        targa.setForeground(UiTheme.NIGHT);
        top.add(targa, BorderLayout.WEST);
        top.add(UiTheme.badgeAperta(), BorderLayout.EAST);
        top.setAlignmentX(0f);

        JLabel dettagli = new JLabel(s.getLocalita() + "  -  " + s.getOreSosta() + "h  -  " + s.getDataOra().format(FMT));
        dettagli.setFont(UiTheme.body(11, false));
        dettagli.setForeground(UiTheme.SLATE);
        dettagli.setBorder(new EmptyBorder(6, 0, 0, 0));
        dettagli.setAlignmentX(0f);

        JLabel costo = new JLabel(String.format(Locale.ITALY, "%.2f EUR", s.getCosto()));
        costo.setFont(UiTheme.display(16));
        costo.setForeground(UiTheme.NIGHT);
        costo.setBorder(new EmptyBorder(6, 0, 0, 0));
        costo.setAlignmentX(0f);

        card.add(top);
        card.add(dettagli);
        card.add(costo);
        return card;
    }
}
