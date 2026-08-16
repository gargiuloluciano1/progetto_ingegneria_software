package controller;
import model.Model;

public class Controller {
    private static int login_succeded=1;
    private static int login_failed=0;
    private Model model;

    public Controller() {
	    super();
        model = new Model();
    }
    
    public int checkCredentials(String name, String password) {
        if(model.checkUser(name,password)){
            return login_succeded;
        }
	    return login_failed;
    }

    public void addUser(String name, String password){
        model.createUser(name, password);
    }
}
