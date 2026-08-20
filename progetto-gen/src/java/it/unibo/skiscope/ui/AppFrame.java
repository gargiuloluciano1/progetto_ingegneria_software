package it.unibo.skiscope.ui;

import it.unibo.skiscope.Session;
import it.unibo.skiscope.controller.AutenticazioneController;
import it.unibo.skiscope.ui.admin.AdminHomePanel;
import it.unibo.skiscope.ui.cliente.ClienteHomePanel;
import it.unibo.skiscope.ui.maestro.MaestroHomePanel;
import it.unibo.skiscope.ui.soccorritore.SoccorritoreHomePanel;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * Finestra principale dell'applicazione. Contiene una barra superiore
 * (logo, stato dell'utente autenticato) e un'area centrale a CardLayout
 * che ospita, alternativamente, la Landing, l'Autenticazione e le quattro
 * aree riservate ai ruoli previsti dal documento di analisi: Cliente,
 * Maestro di Sci, Soccorritore, Amministratore (Presidente).
 */
public class AppFrame extends JFrame {

    public static final String CARD_LANDING = "landing";
    public static final String CARD_AUTH = "auth";
    public static final String CARD_CLIENTE = "cliente";
    public static final String CARD_MAESTRO = "maestro";
    public static final String CARD_SOCCORRITORE = "soccorritore";
    public static final String CARD_ADMIN = "admin";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardsPanel = new JPanel(cardLayout);

    private final JPanel headerBar = new JPanel(new BorderLayout());
    private final JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));

    private final LandingPanel landingPanel;
    private final AuthPanel authPanel;
    private final ClienteHomePanel clienteHomePanel;
    private final MaestroHomePanel maestroHomePanel;
    private final SoccorritoreHomePanel soccorritoreHomePanel;
    private final AdminHomePanel adminHomePanel;

    public AppFrame() {
        super("SkiScope - Comprensorio del Cimone");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 780);
        setMinimumSize(new java.awt.Dimension(980, 640));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UiTheme.SNOW);

        add(buildHeader(), BorderLayout.NORTH);

        landingPanel = new LandingPanel(this);
        authPanel = new AuthPanel(this);
        clienteHomePanel = new ClienteHomePanel(this);
        maestroHomePanel = new MaestroHomePanel(this);
        soccorritoreHomePanel = new SoccorritoreHomePanel(this);
        adminHomePanel = new AdminHomePanel(this);

        cardsPanel.setBackground(UiTheme.SNOW);
        cardsPanel.add(landingPanel, CARD_LANDING);
        cardsPanel.add(authPanel, CARD_AUTH);
        cardsPanel.add(clienteHomePanel, CARD_CLIENTE);
        cardsPanel.add(maestroHomePanel, CARD_MAESTRO);
        cardsPanel.add(soccorritoreHomePanel, CARD_SOCCORRITORE);
        cardsPanel.add(adminHomePanel, CARD_ADMIN);

        add(cardsPanel, BorderLayout.CENTER);

        refreshHeader();
        showLanding();
    }

    private JPanel buildHeader() {
        headerBar.setBackground(UiTheme.NIGHT);
        headerBar.setBorder(new EmptyBorder(12, 24, 12, 24));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);
        JLabel badge = new JLabel("S", SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(UiTheme.ICE);
        badge.setForeground(Color.WHITE);
        badge.setFont(UiTheme.display(18));
        badge.setPreferredSize(new java.awt.Dimension(36, 36));
        badge.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel brandText = new JPanel();
        brandText.setOpaque(false);
        brandText.setLayout(new javax.swing.BoxLayout(brandText, javax.swing.BoxLayout.Y_AXIS));
        JLabel title = new JLabel("SKISCOPE");
        title.setFont(UiTheme.display(20));
        title.setForeground(Color.WHITE);
        JLabel subtitle = new JLabel("Comprensorio del Cimone");
        subtitle.setFont(UiTheme.body(10, false));
        subtitle.setForeground(UiTheme.SLATE_LIGHT);
        brandText.add(title);
        brandText.add(subtitle);

        brand.add(badge);
        brand.add(brandText);
        brand.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        brand.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showLanding();
            }
        });

        headerRight.setOpaque(false);

        headerBar.add(brand, BorderLayout.WEST);
        headerBar.add(headerRight, BorderLayout.EAST);
        return headerBar;
    }

    /** Ricostruisce la parte destra della barra superiore in base alla sessione corrente. */
    public void refreshHeader() {
        headerRight.removeAll();
        Session session = Session.getCurrent();
        if (session == null) {
            javax.swing.JButton home = UiTheme.outlineButton("Home");
            home.setForeground(Color.WHITE);
            home.setBackground(UiTheme.NIGHT);
            home.setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.LineBorder(UiTheme.SLATE_LIGHT, 1, true),
                    new EmptyBorder(8, 16, 8, 16)));
            home.addActionListener(e -> showLanding());

            javax.swing.JButton accedi = UiTheme.primaryButton("Accedi / Registrati");
            accedi.addActionListener(e -> showAuth());

            headerRight.add(home);
            headerRight.add(accedi);
        } else {
            JLabel info = new JLabel(session.getNomeCompleto() + "  -  " + session.getRuoloLabel());
            info.setForeground(Color.WHITE);
            info.setFont(UiTheme.body(13, true));
            info.setBorder(new EmptyBorder(0, 0, 0, 10));

            javax.swing.JButton esci = UiTheme.outlineButton("Esci");
            esci.setForeground(Color.WHITE);
            esci.setBackground(UiTheme.NIGHT);
            esci.setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.LineBorder(UiTheme.SLATE_LIGHT, 1, true),
                    new EmptyBorder(8, 16, 8, 16)));
            esci.addActionListener(e -> logout());

            headerRight.add(info);
            headerRight.add(esci);
        }
        headerRight.revalidate();
        headerRight.repaint();
    }

    public void showLanding() {
        landingPanel.refresh();
        cardLayout.show(cardsPanel, CARD_LANDING);
        refreshHeader();
    }

    public void showAuth() {
        authPanel.reset();
        cardLayout.show(cardsPanel, CARD_AUTH);
        refreshHeader();
    }

    /** Da richiamare quando la sezione richiesta è riservata: se non loggato porta al login. */
    public void showAuthOrSectionForCliente(String tabName) {
        Session session = Session.getCurrent();
        if (session == null) {
            showAuth();
            return;
        }
        if (session.getRuolo() != Session.Ruolo.CLIENTE) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Questa sezione è riservata ai clienti.", "Accesso non consentito",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        clienteHomePanel.refresh();
        clienteHomePanel.selezionaTab(tabName);
        cardLayout.show(cardsPanel, CARD_CLIENTE);
        refreshHeader();
    }

    /** Instrada l'utente appena autenticato verso l'area corretta in base al ruolo. */
    public void afterLogin() {
        Session session = Session.getCurrent();
        if (session == null) {
            showLanding();
            return;
        }
        switch (session.getRuolo()) {
            case CLIENTE:
                clienteHomePanel.refresh();
                cardLayout.show(cardsPanel, CARD_CLIENTE);
                break;
            case MAESTRO:
                maestroHomePanel.refresh();
                cardLayout.show(cardsPanel, CARD_MAESTRO);
                break;
            case SOCCORRITORE:
                soccorritoreHomePanel.refresh();
                cardLayout.show(cardsPanel, CARD_SOCCORRITORE);
                break;
            case AMMINISTRATORE:
                adminHomePanel.refresh();
                cardLayout.show(cardsPanel, CARD_ADMIN);
                break;
            default:
                showLanding();
        }
        refreshHeader();
    }

    public void logout() {
        new AutenticazioneController().logout();
        showLanding();
    }
}
