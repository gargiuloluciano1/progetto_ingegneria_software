package it.unibo.skiscope.ui.maestro;

import it.unibo.skiscope.Session;
import it.unibo.skiscope.controller.GestioneDisponibilitaController;
import it.unibo.skiscope.controller.GestioneStagioneController;
import it.unibo.skiscope.controller.OperazioneException;
import it.unibo.skiscope.model.Lezione;
import it.unibo.skiscope.model.Partecipante;
import it.unibo.skiscope.model.StatoLezione;
import it.unibo.skiscope.model.TipoLezione;
import it.unibo.skiscope.ui.AppFrame;
import it.unibo.skiscope.ui.FormFields;
import it.unibo.skiscope.ui.UiTheme;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import java.util.List;

/**
 * Area riservata al Maestro di Sci (RFM1, RFM2, RFM5): inserimento e
 * rimozione delle fasce orarie di disponibilita' per le lezioni.
 */
public class MaestroHomePanel extends JPanel {

    private static final DateTimeFormatter DATA_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final GestioneDisponibilitaController controller = new GestioneDisponibilitaController();
    private final JLabel sottotitolo = new JLabel();
    private final JLabel avvisoStagione = new JLabel();
    private final JPanel content = new JPanel(new GridLayout(1, 2, 24, 0));

    public MaestroHomePanel(AppFrame appFrame) {
        setLayout(new BorderLayout());
        setBackground(UiTheme.SNOW);
        setBorder(new EmptyBorder(30, 32, 20, 32));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel titolo = new JLabel("Area Maestro");
        titolo.setFont(UiTheme.display(26));
        titolo.setForeground(UiTheme.NIGHT);
        titolo.setAlignmentX(0f);
        sottotitolo.setFont(UiTheme.body(13, false));
        sottotitolo.setForeground(UiTheme.SLATE);
        sottotitolo.setAlignmentX(0f);
        sottotitolo.setBorder(new EmptyBorder(2, 0, 10, 0));
        avvisoStagione.setFont(UiTheme.body(12, true));
        avvisoStagione.setForeground(UiTheme.SUN);
        avvisoStagione.setAlignmentX(0f);
        avvisoStagione.setBorder(new EmptyBorder(0, 0, 10, 0));

        header.add(titolo);
        header.add(sottotitolo);
        header.add(avvisoStagione);

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
        boolean attiva = new GestioneStagioneController().isStagioneAttiva();
        avvisoStagione.setVisible(!attiva);
        avvisoStagione.setText("Fuori stagione: non è possibile inserire nuove disponibilità.");

        content.removeAll();
        content.add(buildFormColumn(attiva));
        content.add(buildListColumn());
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

    private JPanel buildFormColumn(boolean stagioneAttiva) {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(stepLabel("AGGIUNGI DISPONIBILITA'"));
        col.add(javax.swing.Box.createVerticalStrut(10));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(UiTheme.WHITE);
        form.setBorder(new CompoundBorder(new LineBorder(UiTheme.LINE, 1, true), new EmptyBorder(16, 16, 16, 16)));
        form.setAlignmentX(0f);
        form.setMaximumSize(new Dimension(420, 280));

        JTextField dataField = new JTextField("10/01/2027");
        dataField.setEnabled(stagioneAttiva);

        Integer[] ore = new Integer[11];
        for (int i = 0; i <= 10; i++) {
            ore[i] = 8 + i;
        }
        JComboBox<Integer> oraInizioBox = new JComboBox<>(ore);
        JComboBox<Integer> oraFineBox = new JComboBox<>(ore);
        oraFineBox.setSelectedIndex(4);
        oraInizioBox.setEnabled(stagioneAttiva);
        oraFineBox.setEnabled(stagioneAttiva);

        JLabel hint = new JLabel("La disponibilita' viene suddivisa in blocchi da 1 ora.");
        hint.setFont(UiTheme.body(11, false));
        hint.setForeground(UiTheme.SLATE);
        hint.setAlignmentX(0f);
        hint.setBorder(new EmptyBorder(6, 0, 6, 0));

        JLabel errore = new JLabel(" ");
        errore.setForeground(UiTheme.SLOPE_RED);
        errore.setFont(UiTheme.body(12, true));
        errore.setAlignmentX(0f);

        JButton aggiungi = UiTheme.primaryButton("Aggiungi disponibilita'");
        aggiungi.setAlignmentX(0f);
        aggiungi.setEnabled(stagioneAttiva);
        aggiungi.addActionListener(e -> {
            try {
                LocalDate data;
                try {
                    data = LocalDate.parse(dataField.getText().trim(), DATA_FMT);
                } catch (DateTimeException ex) {
                    throw new OperazioneException("Data non valida. Usa il formato gg/mm/aaaa.");
                }
                int oraInizio = (Integer) oraInizioBox.getSelectedItem();
                int oraFine = (Integer) oraFineBox.getSelectedItem();
                Session session = Session.getCurrent();
                List<Lezione> nuove = controller.aggiungiDisponibilita(session.getUsername(),
                        session.getNomeCompleto(), data, oraInizio, oraFine);
                UiTheme.mostraSuccesso(this, "Disponibilità aggiunta: " + nuove.size()
                        + " fascia/e da 1 ora create.");
                refresh();
            } catch (OperazioneException ex) {
                errore.setText(ex.getMessage());
            }
        });

        form.add(FormFields.labeled("Data (gg/mm/aaaa)", dataField));
        form.add(javax.swing.Box.createVerticalStrut(12));
        JPanel oreRow = new JPanel(new GridLayout(1, 2, 10, 0));
        oreRow.setOpaque(false);
        oreRow.setAlignmentX(0f);
        oreRow.add(FormFields.labeled("Ora inizio", oraInizioBox));
        oreRow.add(FormFields.labeled("Ora fine", oraFineBox));
        form.add(oreRow);
        form.add(hint);
        form.add(errore);
        form.add(javax.swing.Box.createVerticalStrut(8));
        form.add(aggiungi);

        col.add(form);
        return col;
    }

    private JPanel buildListColumn() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(stepLabel("LE MIE DISPONIBILITA'"));
        col.add(javax.swing.Box.createVerticalStrut(10));

        String username = Session.getCurrent().getUsername();
        List<Lezione> mie = controller.getDisponibilitaMaestro(username);
        if (mie.isEmpty()) {
            JLabel empty = new JLabel("Nessuna disponibilita' inserita.");
            empty.setForeground(UiTheme.SLATE);
            empty.setAlignmentX(0f);
            col.add(empty);
        }
        for (Lezione l : mie) {
            col.add(buildLezioneCard(l));
            col.add(javax.swing.Box.createVerticalStrut(8));
        }
        return col;
    }

    private JPanel buildLezioneCard(Lezione l) {
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

        JPanel right = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        boolean libera = l.getStato() == StatoLezione.LIBERA;
        right.add(libera ? UiTheme.badgeAperta() : UiTheme.badgeAttesa());
        if (libera) {
            JButton rimuovi = UiTheme.outlineButton("Rimuovi");
            rimuovi.addActionListener(e -> {
                try {
                    controller.rimuoviDisponibilita(l.getId());
                    UiTheme.mostraSuccesso(this, "Disponibilità rimossa.");
                    refresh();
                } catch (OperazioneException ex) {
                    UiTheme.mostraErrore(this, ex.getMessage());
                }
            });
            right.add(rimuovi);
        }
        top.add(right, BorderLayout.EAST);

        JLabel data = new JLabel(l.getData().format(DATA_FMT));
        data.setFont(UiTheme.body(11, false));
        data.setForeground(UiTheme.SLATE);
        data.setBorder(new EmptyBorder(6, 0, 0, 0));
        data.setAlignmentX(0f);

        card.add(top);
        card.add(data);

        if (!libera && !l.getIscritti().isEmpty()) {
            Partecipante p = l.getIscritti().get(0);
            String descr = l.getTipo() == TipoLezione.GRUPPO
                    ? ("Gruppo - ref. " + p.getNomeCompleto() + " - " + l.getNumPartecipanti() + " pers.")
                    : ("Singola - " + p.getNomeCompleto());
            JLabel dettagli = new JLabel(descr);
            dettagli.setFont(UiTheme.body(11, false));
            dettagli.setForeground(UiTheme.SLATE);
            dettagli.setBorder(new EmptyBorder(4, 0, 0, 0));
            dettagli.setAlignmentX(0f);
            card.add(dettagli);
        }
        return card;
    }
}
