package controller;
public class Controller {
    private static int login_succeded=1;
    private static int login_failed=1;

    public Controller() {
	super();
    }
    
    public int checkCredentials(String name, String username) {
	return login_succeded;
    }
}
