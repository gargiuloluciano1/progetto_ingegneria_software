package it.unibo.skiscope.ui.admin;

import it.unibo.skiscope.Session;
import it.unibo.skiscope.ui.AppFrame;
import it.unibo.skiscope.ui.CardLayoutTabs;
import it.unibo.skiscope.ui.UiTheme;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

/**
 * Area riservata all'Amministratore/Presidente (RFG2): gestione della
 * stagione sciistica, apertura/chiusura piste e visualizzazione statistiche.
 */
public class AdminHomePanel extends JPanel {

    public static final int TAB_STAGIONE = 0;
    public static final int TAB_PISTE = 1;
    public static final int TAB_STATISTICHE = 2;

    private final JLabel sottotitolo = new JLabel();
    private final CardLayoutTabs tabs = new CardLayoutTabs(new String[]{"Stagione", "Piste", "Statistiche"});

    private final StagionePanel stagionePanel = new StagionePanel();
    private final PistePanel pistePanel = new PistePanel();
    private final StatistichePanel statistichePanel = new StatistichePanel();

    public AdminHomePanel(AppFrame appFrame) {
        setLayout(new BorderLayout());
        setBackground(UiTheme.SNOW);
        setBorder(new EmptyBorder(30, 32, 20, 32));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel titolo = new JLabel("Area Presidente");
        titolo.setFont(UiTheme.display(26));
        titolo.setForeground(UiTheme.NIGHT);
        titolo.setAlignmentX(0f);
        sottotitolo.setFont(UiTheme.body(13, false));
        sottotitolo.setForeground(UiTheme.SLATE);
        sottotitolo.setAlignmentX(0f);
        sottotitolo.setBorder(new EmptyBorder(2, 0, 14, 0));
        header.add(titolo);
        header.add(sottotitolo);
        header.add(tabs.getTabBar());

        tabs.addCard(stagionePanel);
        tabs.addCard(pistePanel);
        tabs.addCard(statistichePanel);
        tabs.onSelect(this::refreshTabContent);

        JPanel cardsWrapper = new JPanel(new BorderLayout());
        cardsWrapper.setOpaque(false);
        cardsWrapper.setBorder(new EmptyBorder(20, 0, 0, 0));
        cardsWrapper.add(tabs.getCardsPanel(), BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(cardsWrapper, BorderLayout.CENTER);
    }

    private void refreshTabContent(int index) {
        switch (index) {
            case TAB_STAGIONE: stagionePanel.refresh(); break;
            case TAB_PISTE: pistePanel.refresh(); break;
            case TAB_STATISTICHE: statistichePanel.refresh(); break;
            default: break;
        }
    }

    public void refresh() {
        Session session = Session.getCurrent();
        if (session != null) {
            sottotitolo.setText(session.getNomeCompleto());
        }
        refreshTabContent(tabs.getSelectedIndex());
        revalidate();
        repaint();
    }
}
