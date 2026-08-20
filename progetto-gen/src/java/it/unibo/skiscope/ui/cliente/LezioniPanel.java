package it.unibo.skiscope.ui.cliente;

import it.unibo.skiscope.Session;
import it.unibo.skiscope.controller.GestioneStagioneController;
import it.unibo.skiscope.controller.OperazioneException;
import it.unibo.skiscope.controller.PrenotaLezioneController;
import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.Lezione;
import it.unibo.skiscope.model.Partecipante;
import it.unibo.skiscope.model.TipoLezione;
import it.unibo.skiscope.ui.FormFields;
import it.unibo.skiscope.ui.UiTheme;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * RFM3-RFM7: ricerca delle fasce orarie disponibili e prenotazione di
 * lezioni singole o di gruppo.
 */
public class LezioniPanel extends JPanel {

    private static final DateTimeFormatter DATA_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final PrenotaLezioneController controller = new PrenotaLezioneController();
    private final JPanel content = new JPanel(new GridLayout(1, 2, 24, 0));

    public LezioniPanel() {
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
        content.add(buildDisponibiliColumn());
        content.add(buildMieColumn());
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

    private JPanel buildDisponibiliColumn() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(stepLabel("FASCE ORARIE DISPONIBILI"));
        col.add(javax.swing.Box.createVerticalStrut(10));

        boolean stagioneAttiva = new GestioneStagioneController().isStagioneAttiva();
        List<Lezione> disponibili = controller.ricercaDisponibilita();
        if (disponibili.isEmpty()) {
            JLabel empty = new JLabel("Nessuna disponibilita' al momento.");
            empty.setForeground(UiTheme.SLATE);
            empty.setAlignmentX(0f);
            col.add(empty);
        }
        for (Lezione l : disponibili) {
            col.add(buildSlotCard(l, stagioneAttiva));
            col.add(javax.swing.Box.createVerticalStrut(8));
        }
        return col;
    }

    private JPanel buildSlotCard(Lezione l, boolean stagioneAttiva) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UiTheme.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(UiTheme.LINE, 1, true), new EmptyBorder(14, 16, 14, 16)));
        card.setAlignmentX(0f);
        card.setMaximumSize(new Dimension(460, 70));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel ora = new JLabel(l.getOraInizio() + " - " + l.getOraFine());
        ora.setFont(UiTheme.display(18));
        ora.setForeground(UiTheme.NIGHT);
        JLabel meta = new JLabel(l.getData().format(DATA_FMT) + "  -  Maestro " + l.getMaestroNomeCompleto());
        meta.setFont(UiTheme.body(11, false));
        meta.setForeground(UiTheme.SLATE);
        left.add(ora);
        left.add(meta);

        JButton prenota = UiTheme.primaryButton("Prenota");
        prenota.setEnabled(stagioneAttiva);
        prenota.addActionListener(e -> apriDialogPrenotazione(l));

        card.add(left, BorderLayout.WEST);
        card.add(prenota, BorderLayout.EAST);
        return card;
    }

    private void apriDialogPrenotazione(Lezione lezione) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Prenota lezione");
        dialog.setModal(true);
        dialog.setSize(420, 460);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UiTheme.WHITE);
        panel.setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel titolo = new JLabel(lezione.getData().format(DATA_FMT) + "  " + lezione.getOraInizio()
                + " - " + lezione.getOraFine());
        titolo.setFont(UiTheme.display(18));
        titolo.setForeground(UiTheme.NIGHT);
        titolo.setAlignmentX(0f);

        JPanel tipoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tipoRow.setOpaque(false);
        tipoRow.setAlignmentX(0f);
        JButton singolaBtn = UiTheme.outlineButton("Singola");
        JButton gruppoBtn = UiTheme.outlineButton("Gruppo");
        tipoRow.add(singolaBtn);
        tipoRow.add(gruppoBtn);

        JTextField nomeField = new JTextField();
        JTextField cognomeField = new JTextField();
        JTextField refNomeField = new JTextField();
        JTextField refCognomeField = new JTextField();
        JSpinner numSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 10, 1));

        JPanel fieldsSingola = new JPanel();
        fieldsSingola.setLayout(new BoxLayout(fieldsSingola, BoxLayout.Y_AXIS));
        fieldsSingola.setOpaque(false);
        fieldsSingola.setAlignmentX(0f);
        fieldsSingola.add(FormFields.labeled("Nome partecipante", nomeField));
        fieldsSingola.add(javax.swing.Box.createVerticalStrut(10));
        fieldsSingola.add(FormFields.labeled("Cognome partecipante", cognomeField));

        JPanel fieldsGruppo = new JPanel();
        fieldsGruppo.setLayout(new BoxLayout(fieldsGruppo, BoxLayout.Y_AXIS));
        fieldsGruppo.setOpaque(false);
        fieldsGruppo.setAlignmentX(0f);
        fieldsGruppo.setVisible(false);
        fieldsGruppo.add(FormFields.labeled("Nome referente", refNomeField));
        fieldsGruppo.add(javax.swing.Box.createVerticalStrut(10));
        fieldsGruppo.add(FormFields.labeled("Cognome referente", refCognomeField));
        fieldsGruppo.add(javax.swing.Box.createVerticalStrut(10));
        fieldsGruppo.add(FormFields.labeled("Numero partecipanti (2-10)", numSpinner));

        JLabel prezzoLabel = new JLabel();
        prezzoLabel.setFont(UiTheme.body(13, true));
        prezzoLabel.setForeground(UiTheme.ICE_DARK);
        prezzoLabel.setAlignmentX(0f);
        prezzoLabel.setBorder(new EmptyBorder(12, 0, 6, 0));

        JLabel errore = new JLabel(" ");
        errore.setForeground(UiTheme.SLOPE_RED);
        errore.setFont(UiTheme.body(12, true));
        errore.setAlignmentX(0f);

        final TipoLezione[] tipoScelto = {TipoLezione.SINGOLA};
        Runnable aggiornaPrezzo = () -> {
            int n = tipoScelto[0] == TipoLezione.SINGOLA ? 1 : (Integer) numSpinner.getValue();
            double prezzo = n <= 1 ? 50.0 : 60.0 + Math.max(0, n - 2) * 10.0;
            prezzoLabel.setText(String.format(Locale.ITALY, "Prezzo: %.2f EUR", prezzo));
        };
        aggiornaPrezzo.run();
        numSpinner.addChangeListener(e -> aggiornaPrezzo.run());

        singolaBtn.addActionListener(e -> {
            tipoScelto[0] = TipoLezione.SINGOLA;
            fieldsSingola.setVisible(true);
            fieldsGruppo.setVisible(false);
            aggiornaPrezzo.run();
            dialog.revalidate();
        });
        gruppoBtn.addActionListener(e -> {
            tipoScelto[0] = TipoLezione.GRUPPO;
            fieldsSingola.setVisible(false);
            fieldsGruppo.setVisible(true);
            aggiornaPrezzo.run();
            dialog.revalidate();
        });

        JButton conferma = UiTheme.sunButton("Paga e conferma prenotazione");
        conferma.setAlignmentX(0f);
        conferma.setBorder(new EmptyBorder(16, 20, 10, 20));
        conferma.addActionListener(e -> {
            try {
                String username = Session.getCurrent().getUsername();
                if (tipoScelto[0] == TipoLezione.SINGOLA) {
                    controller.prenotaLezioneSingola(lezione.getId(), username,
                            nomeField.getText().trim(), cognomeField.getText().trim());
                } else {
                    controller.prenotaLezioneGruppo(lezione.getId(), username,
                            refNomeField.getText().trim(), refCognomeField.getText().trim(),
                            (Integer) numSpinner.getValue());
                }
                dialog.dispose();
                UiTheme.mostraSuccesso(this, "Lezione confermata: "
                        + String.format(Locale.ITALY, "%.2f", lezione.getPrezzo()) + " EUR pagati con successo.");
                refresh();
            } catch (OperazioneException ex) {
                errore.setText(ex.getMessage());
            }
        });

        panel.add(titolo);
        panel.add(javax.swing.Box.createVerticalStrut(16));
        panel.add(tipoRow);
        panel.add(javax.swing.Box.createVerticalStrut(14));
        panel.add(fieldsSingola);
        panel.add(fieldsGruppo);
        panel.add(prezzoLabel);
        panel.add(errore);
        panel.add(conferma);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JPanel buildMieColumn() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(stepLabel("LE MIE LEZIONI"));
        col.add(javax.swing.Box.createVerticalStrut(10));

        String username = Session.getCurrent().getUsername();
        boolean trovato = false;
        for (Lezione l : DataStore.getInstance().getLezioni()) {
            boolean mia = false;
            for (Partecipante p : l.getIscritti()) {
                if (p.getClienteUsername().equals(username)) {
                    mia = true;
                    break;
                }
            }
            if (!mia) {
                continue;
            }
            trovato = true;
            col.add(buildLezioneMiaCard(l));
            col.add(javax.swing.Box.createVerticalStrut(8));
        }
        if (!trovato) {
            JLabel empty = new JLabel("Nessuna lezione prenotata.");
            empty.setForeground(UiTheme.SLATE);
            empty.setAlignmentX(0f);
            col.add(empty);
        }
        return col;
    }

    private JPanel buildLezioneMiaCard(Lezione l) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UiTheme.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(UiTheme.LINE, 1, true), new EmptyBorder(12, 14, 12, 14)));
        card.setAlignmentX(0f);
        card.setMaximumSize(new Dimension(420, 110));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel ora = new JLabel(l.getOraInizio() + " - " + l.getOraFine());
        ora.setFont(UiTheme.body(13, true));
        ora.setForeground(UiTheme.NIGHT);
        top.add(ora, BorderLayout.WEST);
        top.add(UiTheme.badgeAperta(), BorderLayout.EAST);

        String descrizioneTipo = l.getTipo() == TipoLezione.GRUPPO
                ? "Gruppo - " + l.getNumPartecipanti() + " persone" : "Singola";
        JLabel dettagli = new JLabel(l.getData().format(DATA_FMT) + "  -  " + descrizioneTipo);
        dettagli.setFont(UiTheme.body(11, false));
        dettagli.setForeground(UiTheme.SLATE);
        dettagli.setBorder(new EmptyBorder(6, 0, 0, 0));
        dettagli.setAlignmentX(0f);

        JLabel prezzo = new JLabel(String.format(Locale.ITALY, "%.2f EUR", l.getPrezzo()));
        prezzo.setFont(UiTheme.display(16));
        prezzo.setForeground(UiTheme.NIGHT);
        prezzo.setBorder(new EmptyBorder(6, 0, 0, 0));
        prezzo.setAlignmentX(0f);

        card.add(top);
        card.add(dettagli);
        card.add(prezzo);
        return card;
    }
}
