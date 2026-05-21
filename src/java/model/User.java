package model;
public abstract class User { 
    // NOTE(forse l'id viene generato automaticamente o preso da una class globale che mantiene una specie di contatore)
    private int id;

    private String username;
    private String password;

    public String getPassword() {
	return password;
    }
    public String getUsername() {
	return username;
    }

    public User() {
    }

    public User(String username, String password) {
	if (username==null || password==null) throw new NullPointerException(); 
	this.username = username;
	this.password = password;
    }
}
