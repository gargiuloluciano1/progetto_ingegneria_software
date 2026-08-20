package it.unibo.skiscope.ui;

import it.unibo.skiscope.model.Difficolta;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

/**
 * Costanti di stile (palette colori, font) e metodi di fabbrica per i
 * componenti Swing usati in tutta l'applicazione, in modo da mantenere
 * un aspetto coerente tra le diverse aree (Cliente, Maestro, Soccorritore,
 * Amministratore).
 */
public final class UiTheme {

    private UiTheme() {
    }

    // ---- Palette -----------------------------------------------------
    public static final Color NIGHT = new Color(0x0B, 0x25, 0x40);
    public static final Color NIGHT_2 = new Color(0x12, 0x32, 0x55);
    public static final Color SNOW = new Color(0xF7, 0xFA, 0xFC);
    public static final Color SNOW_2 = new Color(0xED, 0xF2, 0xF7);
    public static final Color ICE = new Color(0x3E, 0x9B, 0xD6);
    public static final Color ICE_DARK = new Color(0x2B, 0x7C, 0xB0);
    public static final Color PINE = new Color(0x22, 0x7A, 0x5B);
    public static final Color PINE_LIGHT = new Color(0xE7, 0xF5, 0xEF);
    public static final Color SUN = new Color(0xE8, 0x62, 0x2C);
    // Variante piu' scura di SUN, usata sui bottoni per garantire un contrasto
    // sufficiente (WCAG AA) al testo bianco sopra di essa.
    public static final Color SUN_DARK = new Color(0xC4, 0x50, 0x1E);
    public static final Color SLOPE_RED = new Color(0xC2, 0x3B, 0x3B);
    public static final Color SLOPE_RED_LIGHT = new Color(0xFB, 0xEA, 0xEA);
    public static final Color AZZURRA = new Color(0x27, 0x71, 0xAA);
    public static final Color VERDE_PISTA = new Color(0x27, 0x83, 0x43);
    public static final Color NERA_PISTA = new Color(0x1A, 0x1A, 0x1A);
    public static final Color SLATE = new Color(0x4B, 0x5F, 0x73);
    public static final Color SLATE_LIGHT = new Color(0x8C, 0xA0, 0xB3);
    public static final Color LINE = new Color(0xDC, 0xE4, 0xEC);
    public static final Color WHITE = Color.WHITE;

    // ---- Font ----------------------------------------------------------
    public static Font display(int size) {
        return new Font("SansSerif", Font.BOLD, size);
    }

    public static Font body(int size, boolean bold) {
        return new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, size);
    }

    public static Font mono(int size) {
        return new Font("Monospaced", Font.PLAIN, size);
    }

    // ---- Bottoni ---------------------------------------------------------
    // ICE_DARK e SUN_DARK sono usati (anziche' ICE e SUN) perche' offrono un
    // rapporto di contrasto sufficiente al testo bianco sopra di essi.
    public static JButton primaryButton(String text) {
        return styledButton(text, ICE_DARK, WHITE);
    }

    public static JButton sunButton(String text) {
        return styledButton(text, SUN_DARK, WHITE);
    }

    public static JButton dangerButton(String text) {
        return styledButton(text, SLOPE_RED, WHITE);
    }

    public static JButton nightButton(String text) {
        return styledButton(text, NIGHT, WHITE);
    }

    public static JButton outlineButton(String text) {
        JButton b = styledButton(text, WHITE, NIGHT);
        b.setBorder(new CompoundBorder(new LineBorder(LINE, 1, true), new EmptyBorder(9, 18, 9, 18)));
        return b;
    }

    private static JButton styledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(body(13, true));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        return button;
    }

    // ---- Testi -----------------------------------------------------------
    public static JLabel titolo(String text, int size) {
        return titolo(text, size, NIGHT);
    }

    /** Variante che permette di scegliere il colore del testo (es. WHITE su sfondo NIGHT). */
    public static JLabel titolo(String text, int size, Color colore) {
        JLabel label = new JLabel(text);
        label.setFont(display(size));
        label.setForeground(colore);
        return label;
    }

    public static JLabel sottotitolo(String text) {
        return sottotitolo(text, SLATE);
    }

    /** Variante che permette di scegliere il colore del testo (es. SLATE_LIGHT su sfondo NIGHT). */
    public static JLabel sottotitolo(String text, Color colore) {
        JLabel label = new JLabel(text);
        label.setFont(body(13, false));
        label.setForeground(colore);
        return label;
    }

    // ---- Contenitori -------------------------------------------------
    public static JPanel card() {
        JPanel panel = new JPanel();
        panel.setBackground(WHITE);
        panel.setBorder(new CompoundBorder(new LineBorder(LINE, 1, true), new EmptyBorder(18, 18, 18, 18)));
        return panel;
    }

    // ---- Badge di stato --------------------------------------------------
    public static JLabel badge(String text, Color bg, Color fg) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(bg);
        label.setForeground(fg);
        label.setFont(body(11, true));
        Border padding = new EmptyBorder(4, 12, 4, 12);
        label.setBorder(padding);
        return label;
    }

    public static JLabel badgeAperta() {
        return badge("APERTA", PINE_LIGHT, PINE);
    }

    public static JLabel badgeChiusa() {
        return badge("CHIUSA", SLOPE_RED_LIGHT, SLOPE_RED);
    }

    public static JLabel badgeAttesa() {
        return badge("IN ATTESA", new Color(0xFD, 0xEB, 0xE2), SUN);
    }

    public static JLabel badgeInfo(String text) {
        return badge(text, new Color(0xEA, 0xF3, 0xFB), ICE_DARK);
    }

    public static JLabel badgeDifficolta(Difficolta difficolta) {
        Color colore;
        switch (difficolta) {
            case VERDE: colore = VERDE_PISTA; break;
            case AZZURRA: colore = AZZURRA; break;
            case ROSSA: colore = SLOPE_RED; break;
            case NERA: colore = NERA_PISTA; break;
            default: colore = SLATE;
        }
        return badge(difficolta.name(), colore, WHITE);
    }

    // ---- Dialoghi di esito -------------------------------------------
    public static void mostraSuccesso(Component parent, String messaggio) {
        JOptionPane.showMessageDialog(parent, messaggio, "Operazione completata",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void mostraErrore(Component parent, String messaggio) {
        JOptionPane.showMessageDialog(parent, messaggio, "Operazione non riuscita",
                JOptionPane.ERROR_MESSAGE);
    }

    public static Border emptyBorder(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }

    public static Border lineBorder() {
        return new LineBorder(LINE, 1, true);
    }
}
