package it.unibo.skiscope.ui;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;

/**
 * Helper per costruire campi di form con etichetta sopra il componente,
 * nello stile usato in tutte le schermate (label maiuscola piccola +
 * campo di input sottostante).
 */
public final class FormFields {

    private FormFields() {
    }

    public static JPanel labeled(String etichetta, JComponent campo) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(0f);

        JLabel label = new JLabel(etichetta.toUpperCase());
        label.setFont(UiTheme.body(11, true));
        label.setForeground(UiTheme.SLATE);
        label.setAlignmentX(0f);
        label.setBorder(new EmptyBorder(0, 0, 5, 0));

        campo.setAlignmentX(0f);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, campo.getPreferredSize().height + 12));
        campo.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.LineBorder(UiTheme.LINE, 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        // Colori impostati esplicitamente (non lasciati al Look and Feel di sistema):
        // su alcuni sistemi con tema scuro, i campi di input erediterebbero
        // altrimenti colori imprevedibili e potenzialmente illeggibili.
        campo.setBackground(UiTheme.WHITE);
        campo.setForeground(UiTheme.NIGHT);
        campo.setOpaque(true);

        panel.add(label);
        panel.add(campo);
        return panel;
    }
}
