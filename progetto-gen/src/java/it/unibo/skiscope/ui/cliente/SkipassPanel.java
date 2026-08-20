package it.unibo.skiscope.ui.cliente;

import it.unibo.skiscope.Session;
import it.unibo.skiscope.controller.AcquistoSkipassController;
import it.unibo.skiscope.controller.GestioneStagioneController;
import it.unibo.skiscope.controller.OperazioneException;
import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.Skipass;
import it.unibo.skiscope.model.TipoSkipass;
import it.unibo.skiscope.ui.FormFields;
import it.unibo.skiscope.ui.UiTheme;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
 * RFS1-RFS7: acquisto dello skipass e generazione del codice QR.
 */
public class SkipassPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final AcquistoSkipassController controller = new AcquistoSkipassController();
    private final JPanel content = new JPanel(new GridLayout(1, 2, 24, 0));
    private TipoSkipass selectedTipo;

    public SkipassPanel() {
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

    private JLabel stepLabel(String testo) {
        JLabel label = new JLabel(testo);
        label.setFont(UiTheme.body(12, true));
        label.setForeground(UiTheme.SLATE);
        label.setAlignmentX(0f);
        return label;
    }

    private JPanel buildLeftColumn() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(stepLabel("1. SCEGLI IL TIPO"));
        col.add(javax.swing.Box.createVerticalStrut(10));

        JPanel grid = new JPanel(new GridLayout(2, 3, 10, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(0f);
        grid.setMaximumSize(new Dimension(520, 160));
        for (TipoSkipass tipo : TipoSkipass.values()) {
            grid.add(buildTipoCard(tipo));
        }
        col.add(grid);
        col.add(javax.swing.Box.createVerticalStrut(24));

        boolean stagioneAttiva = new GestioneStagioneController().isStagioneAttiva();
        if (selectedTipo != null) {
            col.add(stepLabel("2. DATI BENEFICIARIO"));
            col.add(javax.swing.Box.createVerticalStrut(10));
            col.add(buildForm(stagioneAttiva));
        } else {
            JLabel hint = new JLabel("Seleziona un tipo di skipass per procedere.");
            hint.setForeground(UiTheme.SLATE);
            hint.setAlignmentX(0f);
            col.add(hint);
        }
        return col;
    }

    private JPanel buildTipoCard(TipoSkipass tipo) {
        boolean selezionato = tipo == selectedTipo;
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(selezionato ? new java.awt.Color(0xEA, 0xF3, 0xFB) : UiTheme.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(selezionato ? UiTheme.ICE : UiTheme.LINE, 1, true),
                new EmptyBorder(12, 10, 12, 10)));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel nome = new JLabel(tipo.getEtichetta());
        nome.setFont(UiTheme.body(12, true));
        nome.setForeground(UiTheme.NIGHT);
        nome.setAlignmentX(0f);
        JLabel prezzo = new JLabel(String.format(Locale.ITALY, "%.0f EUR", tipo.getPrezzo()));
        prezzo.setFont(UiTheme.display(20));
        prezzo.setForeground(UiTheme.NIGHT);
        prezzo.setAlignmentX(0f);
        prezzo.setBorder(new EmptyBorder(4, 0, 0, 0));

        card.add(nome);
        card.add(prezzo);
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedTipo = tipo;
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
        form.setMaximumSize(new Dimension(480, 320));

        JTextField nomeField = new JTextField();
        JTextField cognomeField = new JTextField();
        JTextField cfField = new JTextField();
        nomeField.setEnabled(stagioneAttiva);
        cognomeField.setEnabled(stagioneAttiva);
        cfField.setEnabled(stagioneAttiva);

        JLabel totale = new JLabel(String.format(Locale.ITALY, "Totale da pagare: %.2f EUR", selectedTipo.getPrezzo()));
        totale.setFont(UiTheme.body(13, true));
        totale.setForeground(UiTheme.ICE_DARK);
        totale.setAlignmentX(0f);

        JLabel errore = new JLabel(" ");
        errore.setForeground(UiTheme.SLOPE_RED);
        errore.setFont(UiTheme.body(12, true));
        errore.setAlignmentX(0f);

        JButton paga = UiTheme.sunButton("Paga e genera QR");
        paga.setAlignmentX(0f);
        paga.setEnabled(stagioneAttiva);
        paga.addActionListener(e -> {
            try {
                Skipass skipass = controller.acquistaSkipass(Session.getCurrent().getUsername(),
                        nomeField.getText().trim(), cognomeField.getText().trim(), cfField.getText().trim(),
                        selectedTipo);
                selectedTipo = null;
                UiTheme.mostraSuccesso(this, "Skipass " + skipass.getTipo().getEtichetta() + " acquistato: "
                        + String.format(Locale.ITALY, "%.2f", skipass.getTipo().getPrezzo()) + " EUR. QR generato.");
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

        form.add(FormFields.labeled("Nome", nomeField));
        form.add(javax.swing.Box.createVerticalStrut(10));
        form.add(FormFields.labeled("Cognome", cognomeField));
        form.add(javax.swing.Box.createVerticalStrut(10));
        form.add(FormFields.labeled("Codice Fiscale", cfField));
        form.add(javax.swing.Box.createVerticalStrut(10));
        form.add(totale);
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
        col.add(stepLabel("I MIEI SKIPASS"));
        col.add(javax.swing.Box.createVerticalStrut(10));

        String username = Session.getCurrent().getUsername();
        List<Skipass> tutti = DataStore.getInstance().getSkipassVenduti();

        boolean trovato = false;
        for (int i = tutti.size() - 1; i >= 0; i--) {
            Skipass s = tutti.get(i);
            if (!s.getClienteUsername().equals(username)) {
                continue;
            }
            trovato = true;
            col.add(buildSkipassCard(s));
            col.add(javax.swing.Box.createVerticalStrut(8));
        }
        if (!trovato) {
            JLabel empty = new JLabel("Nessuno skipass acquistato.");
            empty.setForeground(UiTheme.SLATE);
            empty.setAlignmentX(0f);
            col.add(empty);
        }
        return col;
    }

    private JPanel buildSkipassCard(Skipass s) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UiTheme.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(UiTheme.LINE, 1, true), new EmptyBorder(12, 14, 12, 14)));
        card.setAlignmentX(0f);
        card.setMaximumSize(new Dimension(420, 130));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel nome = new JLabel(s.getNomeUtilizzatore() + " " + s.getCognomeUtilizzatore());
        nome.setFont(UiTheme.body(13, true));
        nome.setForeground(UiTheme.NIGHT);
        top.add(nome, BorderLayout.WEST);
        top.add(UiTheme.badgeInfo(s.getTipo().getEtichetta()), BorderLayout.EAST);

        JLabel scadenza = new JLabel("Scade: " + s.getScadenza().format(FMT));
        scadenza.setFont(UiTheme.body(11, false));
        scadenza.setForeground(UiTheme.SLATE);
        scadenza.setBorder(new EmptyBorder(6, 0, 0, 0));
        scadenza.setAlignmentX(0f);

        JLabel apri = new JLabel("Tocca per vedere il QR Code");
        apri.setFont(UiTheme.body(11, true));
        apri.setForeground(UiTheme.ICE_DARK);
        apri.setBorder(new EmptyBorder(8, 0, 0, 0));
        apri.setAlignmentX(0f);

        card.add(top);
        card.add(scadenza);
        card.add(apri);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostraQr(s);
            }
        });
        return card;
    }

    private void mostraQr(Skipass s) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Skipass " + s.getTipo().getEtichetta());
        dialog.setModal(true);
        dialog.setSize(340, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        panel.setBackground(UiTheme.WHITE);

        JLabel titolo = new JLabel(s.getNomeUtilizzatore() + " " + s.getCognomeUtilizzatore());
        titolo.setFont(UiTheme.display(20));
        titolo.setForeground(UiTheme.NIGHT);
        titolo.setAlignmentX(0.5f);

        JPanel qr = buildQrGrid(s.getCodiceQR());
        qr.setAlignmentX(0.5f);

        JLabel codice = new JLabel(s.getCodiceQR());
        codice.setFont(UiTheme.mono(12));
        codice.setForeground(UiTheme.SLATE);
        codice.setAlignmentX(0.5f);
        codice.setBorder(new EmptyBorder(10, 0, 16, 0));

        JLabel cf = new JLabel("Codice fiscale: " + s.getCodiceFiscaleUtilizzatore());
        cf.setFont(UiTheme.body(12, false));
        cf.setForeground(UiTheme.NIGHT);
        cf.setAlignmentX(0.5f);
        JLabel scad = new JLabel("Scadenza: " + s.getScadenza().format(FMT));
        scad.setFont(UiTheme.body(12, false));
        scad.setForeground(UiTheme.NIGHT);
        scad.setAlignmentX(0.5f);
        scad.setBorder(new EmptyBorder(4, 0, 0, 0));

        JButton chiudi = UiTheme.outlineButton("Chiudi");
        chiudi.setAlignmentX(0.5f);
        chiudi.setBorder(new EmptyBorder(20, 20, 8, 20));
        chiudi.addActionListener(e -> dialog.dispose());

        panel.add(titolo);
        panel.add(javax.swing.Box.createVerticalStrut(16));
        panel.add(qr);
        panel.add(codice);
        panel.add(cf);
        panel.add(scad);
        panel.add(chiudi);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    /** Griglia 7x7 pseudo-casuale (deterministica sul codice) con l'aspetto di un QR code. */
    private JPanel buildQrGrid(String seed) {
        JPanel grid = new JPanel(new GridLayout(7, 7, 2, 2));
        grid.setPreferredSize(new Dimension(160, 160));
        grid.setMaximumSize(new Dimension(160, 160));
        grid.setBackground(UiTheme.WHITE);
        grid.setBorder(new LineBorder(UiTheme.NIGHT, 2));

        int h = 0;
        for (int i = 0; i < seed.length(); i++) {
            h = h * 31 + seed.charAt(i);
        }
        for (int i = 0; i < 49; i++) {
            h = h * 1103515245 + 12345;
            boolean on = ((h >> 16) % 3) == 0;
            JPanel cell = new JPanel();
            cell.setBackground(on ? UiTheme.NIGHT : UiTheme.WHITE);
            grid.add(cell);
        }
        return grid;
    }
}
