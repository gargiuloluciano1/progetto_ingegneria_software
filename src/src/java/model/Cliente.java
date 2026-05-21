package model;
public class Cliente extends User {

    public Cliente(String username, String password) {
	super(username, password);
    }
    @Override
    public String toString() {
	return "3,"+getUsername()+","+getPassword();
    }
}
