package it.unibo.skiscope.ui.cliente;

import it.unibo.skiscope.Session;
import it.unibo.skiscope.controller.GestioneStagioneController;
import it.unibo.skiscope.ui.AppFrame;
import it.unibo.skiscope.ui.CardLayoutTabs;
import it.unibo.skiscope.ui.UiTheme;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

/**
 * Area riservata al Cliente (RFG1, RFG4): raccoglie le quattro sezioni
 * funzionali Parcheggio (RFP), Skipass (RFS), Lezioni (RFM), Soccorso (RFSC).
 */
public class ClienteHomePanel extends JPanel {

    public static final int TAB_PARCHEGGIO = 0;
    public static final int TAB_SKIPASS = 1;
    public static final int TAB_LEZIONI = 2;
    public static final int TAB_SOCCORSO = 3;

    private final JLabel titolo = new JLabel();
    private final JLabel sottotitolo = new JLabel();
    private final JLabel avvisoStagione = new JLabel();

    private final CardLayoutTabs tabs = new CardLayoutTabs(
            new String[]{"Parcheggio", "Skipass", "Lezioni", "Soccorso"});

    private final ParcheggioPanel parcheggioPanel = new ParcheggioPanel();
    private final SkipassPanel skipassPanel = new SkipassPanel();
    private final LezioniPanel lezioniPanel = new LezioniPanel();
    private final SoccorsoPanel soccorsoPanel = new SoccorsoPanel();

    public ClienteHomePanel(AppFrame appFrame) {
        setLayout(new BorderLayout());
        setBackground(UiTheme.SNOW);
        setBorder(new EmptyBorder(30, 32, 20, 32));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        titolo.setFont(UiTheme.display(26));
        titolo.setForeground(UiTheme.NIGHT);
        titolo.setText("Area Cliente");
        titolo.setAlignmentX(0f);

        sottotitolo.setFont(UiTheme.body(13, false));
        sottotitolo.setForeground(UiTheme.SLATE);
        sottotitolo.setAlignmentX(0f);
        sottotitolo.setBorder(new EmptyBorder(2, 0, 14, 0));

        avvisoStagione.setFont(UiTheme.body(12, true));
        avvisoStagione.setForeground(UiTheme.SUN);
        avvisoStagione.setAlignmentX(0f);
        avvisoStagione.setBorder(new EmptyBorder(0, 0, 14, 0));

        header.add(titolo);
        header.add(sottotitolo);
        header.add(avvisoStagione);
        header.add(tabs.getTabBar());

        tabs.addCard(parcheggioPanel);
        tabs.addCard(skipassPanel);
        tabs.addCard(lezioniPanel);
        tabs.addCard(soccorsoPanel);
        tabs.onSelect(this::onTabSelected);

        JPanel cardsWrapper = new JPanel(new BorderLayout());
        cardsWrapper.setOpaque(false);
        cardsWrapper.setBorder(new EmptyBorder(20, 0, 0, 0));
        cardsWrapper.add(tabs.getCardsPanel(), BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(cardsWrapper, BorderLayout.CENTER);
    }

    private void onTabSelected(int index) {
        refreshTabContent(index);
    }

    private void refreshTabContent(int index) {
        switch (index) {
            case TAB_PARCHEGGIO: parcheggioPanel.refresh(); break;
            case TAB_SKIPASS: skipassPanel.refresh(); break;
            case TAB_LEZIONI: lezioniPanel.refresh(); break;
            case TAB_SOCCORSO: soccorsoPanel.refresh(); break;
            default: break;
        }
    }

    public void selezionaTab(String nomeSezione) {
        int index;
        switch (nomeSezione) {
            case "parcheggio": index = TAB_PARCHEGGIO; break;
            case "skipass": index = TAB_SKIPASS; break;
            case "lezioni": index = TAB_LEZIONI; break;
            case "soccorso": index = TAB_SOCCORSO; break;
            default: index = TAB_PARCHEGGIO;
        }
        tabs.show(index);
        refreshTabContent(index);
    }

    /** Ricarica intestazione e contenuto della sezione attualmente selezionata. */
    public void refresh() {
        Session session = Session.getCurrent();
        if (session != null) {
            sottotitolo.setText("Benvenuto, " + session.getNomeCompleto());
        }
        boolean attiva = new GestioneStagioneController().isStagioneAttiva();
        avvisoStagione.setVisible(!attiva);
        avvisoStagione.setText("Il comprensorio è fuori stagione: le operazioni sono disabilitate.");

        refreshTabContent(tabs.getSelectedIndex());
        revalidate();
        repaint();
    }
}
