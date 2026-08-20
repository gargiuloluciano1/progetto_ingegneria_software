package it.unibo.skiscope.ui.admin;

import it.unibo.skiscope.controller.GestioneStagioneController;
import it.unibo.skiscope.controller.OperazioneException;
import it.unibo.skiscope.model.Stagione;
import it.unibo.skiscope.ui.FormFields;
import it.unibo.skiscope.ui.UiTheme;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * RFG2, RFG5: il Presidente stabilisce le date di inizio e fine della
 * stagione sciistica. Al di fuori dell'intervallo, l'unica operazione
 * consentita ai clienti e' la registrazione.
 */
public class StagionePanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final GestioneStagioneController controller = new GestioneStagioneController();
    private final JPanel content = new JPanel(new GridLayout(1, 2, 24, 0));

    public StagionePanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        content.setOpaque(false);
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    public void refresh() {
        content.removeAll();
        content.add(buildFormColumn());
        content.add(buildStatoColumn());
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

    private JPanel buildFormColumn() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(stepLabel("DATE DELLA STAGIONE SCIISTICA"));
        col.add(javax.swing.Box.createVerticalStrut(10));

        Stagione stagione = controller.getStagione();
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(UiTheme.WHITE);
        form.setBorder(new CompoundBorder(new LineBorder(UiTheme.LINE, 1, true), new EmptyBorder(16, 16, 16, 16)));
        form.setAlignmentX(0f);
        form.setMaximumSize(new Dimension(460, 220));

        JTextField inizioField = new JTextField(stagione.getDataInizio() != null
                ? stagione.getDataInizio().format(FMT) : "");
        JTextField fineField = new JTextField(stagione.getDataFine() != null
                ? stagione.getDataFine().format(FMT) : "");

        JLabel errore = new JLabel(" ");
        errore.setForeground(UiTheme.SLOPE_RED);
        errore.setFont(UiTheme.body(12, true));
        errore.setAlignmentX(0f);

        JButton salva = UiTheme.primaryButton("Salva date stagione");
        salva.setAlignmentX(0f);
        salva.addActionListener(e -> {
            try {
                LocalDate inizio = parseData(inizioField.getText().trim());
                LocalDate fine = parseData(fineField.getText().trim());
                controller.apriStagione(inizio, fine);
                UiTheme.mostraSuccesso(this, "Date della stagione aggiornate.");
                refresh();
            } catch (OperazioneException ex) {
                errore.setText(ex.getMessage());
            }
        });

        JPanel row = new JPanel(new GridLayout(1, 2, 10, 0));
        row.setOpaque(false);
        row.setAlignmentX(0f);
        row.add(FormFields.labeled("Data inizio (gg/mm/aaaa)", inizioField));
        row.add(FormFields.labeled("Data fine (gg/mm/aaaa)", fineField));

        form.add(row);
        form.add(javax.swing.Box.createVerticalStrut(8));
        form.add(errore);
        form.add(javax.swing.Box.createVerticalStrut(8));
        form.add(salva);

        JLabel hint = new JLabel("<html><div style='width:400px;'>Fuori da questo intervallo, l'unica "
                + "operazione consentita ai clienti e' la registrazione.</div></html>");
        hint.setFont(UiTheme.body(11, false));
        hint.setForeground(UiTheme.SLATE);
        hint.setAlignmentX(0f);
        hint.setBorder(new EmptyBorder(10, 0, 0, 0));

        col.add(form);
        col.add(hint);
        return col;
    }

    private LocalDate parseData(String testo) throws OperazioneException {
        try {
            return LocalDate.parse(testo, FMT);
        } catch (DateTimeException ex) {
            throw new OperazioneException("Data non valida. Usa il formato gg/mm/aaaa.");
        }
    }

    private JPanel buildStatoColumn() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(stepLabel("STATO CORRENTE"));
        col.add(javax.swing.Box.createVerticalStrut(10));

        Stagione stagione = controller.getStagione();
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UiTheme.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(UiTheme.LINE, 1, true), new EmptyBorder(16, 16, 16, 16)));
        card.setAlignmentX(0f);
        card.setMaximumSize(new Dimension(400, 300));

        boolean attiva = controller.isStagioneAttiva();
        JPanel statoRow = new JPanel(new BorderLayout());
        statoRow.setOpaque(false);
        statoRow.setAlignmentX(0f);
        statoRow.add(attiva ? UiTheme.badgeAperta() : UiTheme.badgeChiusa(), BorderLayout.WEST);

        String periodo = (stagione.getDataInizio() != null ? stagione.getDataInizio().format(FMT) : "-")
                + "  ->  " + (stagione.getDataFine() != null ? stagione.getDataFine().format(FMT) : "-");
        JLabel periodoLabel = new JLabel(periodo);
        periodoLabel.setFont(UiTheme.body(12, false));
        periodoLabel.setForeground(UiTheme.SLATE);
        periodoLabel.setBorder(new EmptyBorder(10, 0, 14, 0));
        periodoLabel.setAlignmentX(0f);

        JLabel oggiLabel = new JLabel("Data odierna: " + LocalDate.now().format(FMT));
        oggiLabel.setFont(UiTheme.body(12, false));
        oggiLabel.setForeground(UiTheme.SLATE);
        oggiLabel.setAlignmentX(0f);

        JLabel spiegazione = new JLabel("<html><div style='width:340px;'>" + spiegaStato(stagione, attiva)
                + "</div></html>");
        spiegazione.setFont(UiTheme.body(11, false));
        spiegazione.setForeground(UiTheme.SLATE);
        spiegazione.setBorder(new EmptyBorder(8, 0, 0, 0));
        spiegazione.setAlignmentX(0f);

        card.add(statoRow);
        card.add(periodoLabel);
        card.add(oggiLabel);
        card.add(spiegazione);

        col.add(card);
        return col;
    }

    /** Spiega perche' la stagione risulta (non) attiva, in base alle date reali impostate. */
    private String spiegaStato(Stagione stagione, boolean attiva) {
        if (stagione.getDataInizio() == null || stagione.getDataFine() == null) {
            return "Nessuna data impostata: la stagione non e' attiva finche' non vengono definite "
                    + "data di inizio e fine.";
        }
        if (attiva) {
            return "La data odierna rientra nell'intervallo impostato: tutte le funzionalita' sono "
                    + "disponibili ai clienti.";
        }
        LocalDate oggi = LocalDate.now();
        if (oggi.isBefore(stagione.getDataInizio())) {
            return "La stagione non e' ancora iniziata: fino alla data di inizio, ai clienti e' "
                    + "consentita solo la registrazione.";
        }
        return "La stagione e' terminata: dalla data di fine in poi, ai clienti e' consentita solo "
                + "la registrazione.";
    }
}
