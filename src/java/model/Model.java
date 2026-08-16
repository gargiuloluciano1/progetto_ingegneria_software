package model;

public class Model {
    public UserDatabase udb;

    public Model() {
	    this.udb = new UserDatabase();
    }

    public boolean checkUser(String name, String password){
        return udb.checkUser(name, password);
    }

    public void createUser(String name, String password){
        udb.createUser(name, password);
    }
}
