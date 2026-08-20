package it.unibo.skiscope.ui;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

/**
 * Componente riutilizzabile che combina una barra di tab con un'area a
 * CardLayout, usato per organizzare le interfacce di Cliente e
 * Amministratore in sezioni distinte, in linea con i requisiti funzionali
 * (es. Parcheggio, Skipass, Lezioni, Soccorso per il Cliente).
 */
public class CardLayoutTabs {

    private final JPanel tabBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
    private final JPanel cardsPanel = new JPanel(new CardLayout());
    private final List<JButton> buttons = new ArrayList<>();
    private int selected = 0;
    private IntConsumer listener;

    public CardLayoutTabs(String[] etichette) {
        tabBar.setOpaque(false);
        tabBar.setBorder(new MatteBorder(0, 0, 1, 0, UiTheme.LINE));
        cardsPanel.setOpaque(false);

        for (int i = 0; i < etichette.length; i++) {
            final int index = i;
            JButton tab = new JButton(etichette[i]);
            tab.setFocusPainted(false);
            tab.setContentAreaFilled(false);
            tab.setBorderPainted(false);
            tab.setFont(UiTheme.body(13, true));
            tab.setForeground(UiTheme.SLATE);
            tab.setBorder(new EmptyBorder(10, 16, 10, 16));
            tab.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
            tab.addActionListener(e -> {
                show(index);
                if (listener != null) {
                    listener.accept(index);
                }
            });
            buttons.add(tab);
            tabBar.add(tab);
        }
        aggiornaStileTab();
    }

    public void addCard(JPanel panel) {
        cardsPanel.add(panel, String.valueOf(cardsPanel.getComponentCount()));
    }

    public void show(int index) {
        selected = index;
        ((CardLayout) cardsPanel.getLayout()).show(cardsPanel, String.valueOf(index));
        aggiornaStileTab();
    }

    public int getSelectedIndex() {
        return selected;
    }

    public void onSelect(IntConsumer listener) {
        this.listener = listener;
    }

    public JPanel getTabBar() {
        return tabBar;
    }

    public JPanel getCardsPanel() {
        return cardsPanel;
    }

    private void aggiornaStileTab() {
        for (int i = 0; i < buttons.size(); i++) {
            JButton b = buttons.get(i);
            if (i == selected) {
                b.setForeground(UiTheme.NIGHT);
                b.setBorder(new javax.swing.border.CompoundBorder(
                        new MatteBorder(0, 0, 3, 0, UiTheme.ICE), new EmptyBorder(10, 16, 7, 16)));
            } else {
                b.setForeground(UiTheme.SLATE);
                b.setBorder(new EmptyBorder(10, 16, 10, 16));
            }
        }
    }
}
