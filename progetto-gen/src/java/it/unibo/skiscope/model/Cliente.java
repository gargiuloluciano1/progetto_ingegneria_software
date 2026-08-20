package it.unibo.skiscope.model;

/**
 * Cliente: utente che usufruisce delle infrastrutture del comprensorio
 * (parcheggio, skipass, lezioni, soccorso). RFG3, RFG4.
 */
public class Cliente extends UtenteRegistrato {

    private final String codiceFiscale;
    private final String password;

    public Cliente(String nome, String cognome, String username, String codiceFiscale, String password) {
        super(username, nome, cognome);
        this.codiceFiscale = codiceFiscale;
        this.password = password;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public String getPassword() {
        return password;
    }

    /**
     * RFG3: la password deve avere lunghezza 8-16, almeno una maiuscola
     * e almeno un carattere speciale.
     */
    public boolean isPasswordValida() {
        return isPasswordValida(this.password);
    }

    public static boolean isPasswordValida(String candidata) {
        if (candidata == null || candidata.length() < 8 || candidata.length() > 16) {
            return false;
        }
        boolean haMaiuscola = false;
        boolean haSpeciale = false;
        for (char c : candidata.toCharArray()) {
            if (Character.isUpperCase(c)) {
                haMaiuscola = true;
            } else if (!Character.isLetterOrDigit(c)) {
                haSpeciale = true;
            }
        }
        return haMaiuscola && haSpeciale;
    }
}
