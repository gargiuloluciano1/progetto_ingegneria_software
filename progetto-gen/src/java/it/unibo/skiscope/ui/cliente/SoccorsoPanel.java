package it.unibo.skiscope.ui.cliente;

import it.unibo.skiscope.Session;
import it.unibo.skiscope.controller.GestioneStagioneController;
import it.unibo.skiscope.controller.OperazioneException;
import it.unibo.skiscope.controller.RichiestaSoccorsoController;
import it.unibo.skiscope.model.RichiestaSoccorso;
import it.unibo.skiscope.model.StatoRichiesta;
import it.unibo.skiscope.ui.FormFields;
import it.unibo.skiscope.ui.UiTheme;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * RFSC1, RFSC4: invio di una richiesta di soccorso con luogo e descrizione
 * dell'infortunio.
 */
public class SoccorsoPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final RichiestaSoccorsoController controller = new RichiestaSoccorsoController();
    private final JPanel content = new JPanel(new GridLayout(1, 2, 24, 0));

    public SoccorsoPanel() {
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
        col.add(stepLabel("RICHIEDI SOCCORSO"));
        col.add(javax.swing.Box.createVerticalStrut(10));

        String username = Session.getCurrent().getUsername();
        boolean haAttiva = controller.haRichiestaAttiva(username);
        boolean stagioneAttiva = new GestioneStagioneController().isStagioneAttiva();

        if (haAttiva) {
            JLabel avviso = new JLabel("<html><div style='width:300px;'>Hai gia' una richiesta attiva: non e' "
                    + "possibile inviarne un'altra finche' non viene chiusa.</div></html>");
            avviso.setForeground(UiTheme.SUN);
            avviso.setFont(UiTheme.body(12, true));
            avviso.setAlignmentX(0f);
            col.add(avviso);
            return col;
        }

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(UiTheme.WHITE);
        form.setBorder(new CompoundBorder(new LineBorder(UiTheme.LINE, 1, true), new EmptyBorder(16, 16, 16, 16)));
        form.setAlignmentX(0f);
        form.setMaximumSize(new Dimension(480, 280));

        JTextField luogoField = new JTextField();
        JTextArea descrizioneArea = new JTextArea(4, 20);
        descrizioneArea.setLineWrap(true);
        descrizioneArea.setWrapStyleWord(true);
        descrizioneArea.setFont(UiTheme.body(13, false));
        descrizioneArea.setEnabled(stagioneAttiva);
        luogoField.setEnabled(stagioneAttiva);

        JLabel errore = new JLabel(" ");
        errore.setForeground(UiTheme.SLOPE_RED);
        errore.setFont(UiTheme.body(12, true));
        errore.setAlignmentX(0f);

        JButton invia = UiTheme.dangerButton("Invia richiesta di soccorso");
        invia.setAlignmentX(0f);
        invia.setEnabled(stagioneAttiva);
        invia.addActionListener(e -> {
            try {
                controller.inviaRichiesta(username, Session.getCurrent().getNomeCompleto(),
                        luogoField.getText().trim(), descrizioneArea.getText().trim());
                UiTheme.mostraSuccesso(this,
                        "Richiesta di soccorso inviata. Un soccorritore la prendera' in carico a breve.");
                refresh();
            } catch (OperazioneException ex) {
                errore.setText(ex.getMessage());
            }
        });

        form.add(FormFields.labeled("Luogo dell'infortunio", luogoField));
        form.add(javax.swing.Box.createVerticalStrut(12));
        form.add(FormFields.labeled("Descrizione dell'infortunio", descrizioneArea));
        form.add(javax.swing.Box.createVerticalStrut(8));
        form.add(errore);
        form.add(javax.swing.Box.createVerticalStrut(10));
        form.add(invia);

        col.add(form);
        return col;
    }

    private JPanel buildStatoColumn() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(stepLabel("STATO RICHIESTE"));
        col.add(javax.swing.Box.createVerticalStrut(10));

        String username = Session.getCurrent().getUsername();
        List<RichiestaSoccorso> mie = controller.getRichiesteCliente(username);

        if (mie.isEmpty()) {
            JLabel empty = new JLabel("Nessuna richiesta inviata.");
            empty.setForeground(UiTheme.SLATE);
            empty.setAlignmentX(0f);
            col.add(empty);
        }
        for (int i = mie.size() - 1; i >= 0; i--) {
            col.add(buildRichiestaCard(mie.get(i)));
            col.add(javax.swing.Box.createVerticalStrut(8));
        }
        return col;
    }

    private JPanel buildRichiestaCard(RichiestaSoccorso r) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UiTheme.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(UiTheme.LINE, 1, true), new EmptyBorder(12, 14, 12, 14)));
        card.setAlignmentX(0f);
        card.setMaximumSize(new Dimension(420, 150));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel luogo = new JLabel(r.getLuogo());
        luogo.setFont(UiTheme.body(13, true));
        luogo.setForeground(UiTheme.NIGHT);
        top.add(luogo, BorderLayout.WEST);
        top.add(r.getStato() == StatoRichiesta.APERTA ? UiTheme.badgeAttesa() : UiTheme.badgeInfo("PRESA IN CARICO"),
                BorderLayout.EAST);

        JLabel descr = new JLabel("<html><div style='width:280px;'>" + r.getDescrizione() + "</div></html>");
        descr.setFont(UiTheme.body(11, false));
        descr.setForeground(UiTheme.SLATE);
        descr.setBorder(new EmptyBorder(6, 0, 0, 0));
        descr.setAlignmentX(0f);

        JLabel invio = new JLabel("Inviata: " + r.getDataOraInvio().format(FMT));
        invio.setFont(UiTheme.body(11, false));
        invio.setForeground(UiTheme.SLATE);
        invio.setBorder(new EmptyBorder(4, 0, 0, 0));
        invio.setAlignmentX(0f);

        card.add(top);
        card.add(descr);
        card.add(invio);

        if (r.getSoccorritoreNomeCompleto() != null) {
            JLabel socc = new JLabel("Soccorritore: " + r.getSoccorritoreNomeCompleto());
            socc.setFont(UiTheme.body(11, true));
            socc.setForeground(UiTheme.PINE);
            socc.setBorder(new EmptyBorder(4, 0, 0, 0));
            socc.setAlignmentX(0f);
            card.add(socc);
        }
        return card;
    }
}
