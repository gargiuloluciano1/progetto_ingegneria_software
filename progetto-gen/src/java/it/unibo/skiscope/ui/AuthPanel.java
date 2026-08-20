package it.unibo.skiscope.ui;

import it.unibo.skiscope.Session;
import it.unibo.skiscope.controller.AutenticazioneController;
import it.unibo.skiscope.controller.OperazioneException;
import it.unibo.skiscope.controller.RegistrazioneController;
import it.unibo.skiscope.data.DataStore;
import it.unibo.skiscope.model.Amministratore;
import it.unibo.skiscope.model.Cliente;
import it.unibo.skiscope.model.MaestroDiSci;
import it.unibo.skiscope.model.Soccorritore;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

/**
 * RFG3, RFG4: schermata di accesso per gli utenti registrati e di
 * registrazione per i nuovi clienti.
 */
public class AuthPanel extends JPanel {

    private final AppFrame appFrame;
    private final CardLayoutTabs tabs;

    private final JTextField loginUsername = new JTextField();
    private final JPasswordField loginPassword = new JPasswordField();
    private final JLabel loginError = new JLabel(" ");

    private final JTextField regNome = new JTextField();
    private final JTextField regCognome = new JTextField();
    private final JTextField regUsername = new JTextField();
    private final JTextField regCf = new JTextField();
    private final JPasswordField regPassword = new JPasswordField();
    private final JLabel regError = new JLabel(" ");

    public AuthPanel(AppFrame appFrame) {
        this.appFrame = appFrame;
        setLayout(new BorderLayout());
        setBackground(UiTheme.SNOW);

        JPanel content = new JPanel(new GridLayout(1, 2, 24, 0));
        content.setBackground(UiTheme.SNOW);
        content.setBorder(new EmptyBorder(48, 60, 48, 60));

        JPanel left = UiTheme.card();
        left.setLayout(new BorderLayout());

        tabs = new CardLayoutTabs(new String[]{"Accedi", "Registrati"});
        tabs.onSelect(index -> tabs.show(index));

        JPanel loginForm = buildLoginForm();
        JPanel registerForm = buildRegisterForm();
        tabs.addCard(loginForm);
        tabs.addCard(registerForm);

        left.add(tabs.getTabBar(), BorderLayout.NORTH);
        left.add(tabs.getCardsPanel(), BorderLayout.CENTER);

        JPanel right = buildDemoPanel();

        content.add(left);
        content.add(right);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildLoginForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(UiTheme.WHITE);
        form.setBorder(new EmptyBorder(20, 6, 6, 6));

        form.add(FormFields.labeled("Username", loginUsername));
        form.add(javax.swing.Box.createVerticalStrut(12));
        form.add(FormFields.labeled("Password", loginPassword));
        form.add(javax.swing.Box.createVerticalStrut(6));

        loginError.setForeground(UiTheme.SLOPE_RED);
        loginError.setFont(UiTheme.body(12, true));
        loginError.setAlignmentX(0f);
        form.add(loginError);
        form.add(javax.swing.Box.createVerticalStrut(10));

        javax.swing.JButton loginBtn = UiTheme.primaryButton("Accedi");
        loginBtn.setAlignmentX(0f);
        loginBtn.addActionListener(e -> doLogin());
        form.add(loginBtn);

        return form;
    }

    private JPanel buildRegisterForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(UiTheme.WHITE);
        form.setBorder(new EmptyBorder(20, 6, 6, 6));

        JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
        row.setOpaque(false);
        row.add(FormFields.labeled("Nome", regNome));
        row.add(FormFields.labeled("Cognome", regCognome));
        row.setAlignmentX(0f);
        form.add(row);
        form.add(javax.swing.Box.createVerticalStrut(12));
        form.add(FormFields.labeled("Username", regUsername));
        form.add(javax.swing.Box.createVerticalStrut(12));
        form.add(FormFields.labeled("Codice Fiscale", regCf));
        form.add(javax.swing.Box.createVerticalStrut(12));
        form.add(FormFields.labeled("Password", regPassword));

        JLabel hint = new JLabel("8-16 caratteri, almeno una maiuscola e un carattere speciale.");
        hint.setFont(UiTheme.body(11, false));
        hint.setForeground(UiTheme.SLATE);
        hint.setAlignmentX(0f);
        form.add(hint);
        form.add(javax.swing.Box.createVerticalStrut(8));

        regError.setForeground(UiTheme.SLOPE_RED);
        regError.setFont(UiTheme.body(12, true));
        regError.setAlignmentX(0f);
        form.add(regError);
        form.add(javax.swing.Box.createVerticalStrut(10));

        javax.swing.JButton registerBtn = UiTheme.primaryButton("Crea account");
        registerBtn.setAlignmentX(0f);
        registerBtn.addActionListener(e -> doRegister());
        form.add(registerBtn);

        return form;
    }

    private JPanel buildDemoPanel() {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));

        JPanel quick = UiTheme.card();
        quick.setLayout(new BoxLayout(quick, BoxLayout.Y_AXIS));
        JLabel quickTitle = new JLabel("DEMO RAPIDA");
        quickTitle.setForeground(UiTheme.ICE_DARK);
        quickTitle.setFont(UiTheme.body(11, true));
        quickTitle.setAlignmentX(0f);
        JLabel quickDesc = new JLabel("<html><div style='width:260px;'>Accedi istantaneamente con un "
                + "account dimostrativo per ciascun ruolo.</div></html>");
        quickDesc.setFont(UiTheme.body(11, false));
        quickDesc.setForeground(UiTheme.SLATE);
        quickDesc.setAlignmentX(0f);
        quickDesc.setBorder(new EmptyBorder(4, 0, 12, 0));

        quick.add(quickTitle);
        quick.add(quickDesc);
        quick.add(quickLoginButton("Entra come Cliente", Session.Ruolo.CLIENTE));
        quick.add(javax.swing.Box.createVerticalStrut(6));
        quick.add(quickLoginButton("Entra come Maestro", Session.Ruolo.MAESTRO));
        quick.add(javax.swing.Box.createVerticalStrut(6));
        quick.add(quickLoginButton("Entra come Soccorritore", Session.Ruolo.SOCCORRITORE));
        quick.add(javax.swing.Box.createVerticalStrut(6));
        quick.add(quickLoginButton("Entra come Presidente", Session.Ruolo.AMMINISTRATORE));

        JPanel creds = UiTheme.card();
        creds.setLayout(new BoxLayout(creds, BoxLayout.Y_AXIS));
        creds.setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel credsTitle = new JLabel("CREDENZIALI DEMO");
        credsTitle.setForeground(UiTheme.ICE_DARK);
        credsTitle.setFont(UiTheme.body(11, true));
        credsTitle.setAlignmentX(0f);
        creds.add(credsTitle);
        creds.add(javax.swing.Box.createVerticalStrut(8));
        creds.add(credLine("mrossi", "Password1!"));
        creds.add(credLine("lbianchi", "Maestro1!"));
        creds.add(credLine("averdi", "Soccorso1!"));
        creds.add(credLine("presidente", "CimonePwd!1"));

        wrap.add(quick);
        wrap.add(javax.swing.Box.createVerticalStrut(16));
        wrap.add(creds);
        return wrap;
    }

    private JLabel credLine(String username, String password) {
        JLabel label = new JLabel(username + "   /   " + password);
        label.setFont(UiTheme.mono(12));
        label.setForeground(UiTheme.SLATE);
        label.setAlignmentX(0f);
        label.setBorder(new EmptyBorder(2, 0, 2, 0));
        return label;
    }

    private javax.swing.JButton quickLoginButton(String testo, Session.Ruolo ruolo) {
        javax.swing.JButton button = UiTheme.outlineButton(testo);
        button.setAlignmentX(0f);
        button.addActionListener(e -> quickLogin(ruolo));
        return button;
    }

    private void quickLogin(Session.Ruolo ruolo) {
        DataStore db = DataStore.getInstance();
        Session session;
        switch (ruolo) {
            case CLIENTE:
                Cliente c = db.getClienti().get(0);
                session = new Session(c.getUsername(), c.getNome(), c.getCognome(), Session.Ruolo.CLIENTE);
                break;
            case MAESTRO:
                MaestroDiSci m = db.getMaestri().get(0);
                session = new Session(m.getUsername(), m.getNome(), m.getCognome(), Session.Ruolo.MAESTRO);
                break;
            case SOCCORRITORE:
                Soccorritore s = db.getSoccorritori().get(0);
                session = new Session(s.getUsername(), s.getNome(), s.getCognome(), Session.Ruolo.SOCCORRITORE);
                break;
            default:
                Amministratore a = db.getAmministratore();
                session = new Session(a.getUsername(), a.getNome(), a.getCognome(), Session.Ruolo.AMMINISTRATORE);
        }
        Session.setCurrent(session);
        appFrame.afterLogin();
    }

    private void doLogin() {
        loginError.setText(" ");
        try {
            new AutenticazioneController().autentica(loginUsername.getText().trim(),
                    new String(loginPassword.getPassword()));
            appFrame.afterLogin();
        } catch (OperazioneException ex) {
            loginError.setText(ex.getMessage());
        }
    }

    private void doRegister() {
        regError.setText(" ");
        try {
            new RegistrazioneController().registraUtente(
                    regNome.getText().trim(),
                    regCognome.getText().trim(),
                    regUsername.getText().trim(),
                    new String(regPassword.getPassword()),
                    regCf.getText().trim());
            new AutenticazioneController().autentica(regUsername.getText().trim(),
                    new String(regPassword.getPassword()));
            UiTheme.mostraSuccesso(this, "Registrazione completata! Benvenuto su SkiScope.");
            appFrame.afterLogin();
        } catch (OperazioneException ex) {
            regError.setText(ex.getMessage());
        }
    }

    /** Ripristina i campi del form quando si torna sulla schermata di autenticazione. */
    public void reset() {
        loginUsername.setText("");
        loginPassword.setText("");
        loginError.setText(" ");
        regNome.setText("");
        regCognome.setText("");
        regUsername.setText("");
        regCf.setText("");
        regPassword.setText("");
        regError.setText(" ");
        tabs.show(0);
    }
}
