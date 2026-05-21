package model;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class UserDatabase {
    private final String path_to_users ="/app/src/resources/Users.csv";
    private final int usermax_count = 10;
    /* NOTE
     * Penso esista un pattern per questa cosa, array di superclasse per istanze diverse
     * UserDatabase si occupa del casting down ecc... 
     */
    private User users[];
    private int usercount;

    public UserDatabase() {
	super();

	this.usercount = 0;
	this.users = new User[usermax_count];

	start();
    }
    // NOTE(Facciamo finta sia un vero database)
    private void start() { 
	try (
		FileInputStream inFile = new FileInputStream(path_to_users);
	    ) {
	    BufferedReader br = new BufferedReader(new InputStreamReader(inFile));
	    String line = null;
	    while ((line = br.readLine()) != null) {
		String[] tkns = line.split(",");
		//NOTE(tipo di utente, un utente potrebbe essere piu di uno?)
		if (tkns[0].equals("1")) {
		    this.users[usercount] = new Cliente(tkns[1], tkns[2]);
		}
		else if (tkns[0].equals("2")) {
		    this.users[usercount] = new Cliente(tkns[1], tkns[2]);
		}
		else if (tkns[0].equals("3")) {
		    this.users[usercount] = new Cliente(tkns[1], tkns[2]);
		}
		usercount++;
	    }
	    //TODO(Invocare il Logger)
	    for (int i = 0; i < usercount; i++) {
		System.out.println(users[i].toString());
	    }

	} catch(Exception e) {
	    //TODO(Invocare il Logger)
	    System.err.println("[USERDATABASE:ERROR]" + e.getMessage());
	}
    }

    //NOTE(nome da cambiare probabilmente)
    //TODO(NON FUNZIONA)
    //TODO(Vorrei aggiungere uno struct credenziali piu generico)
    private void createUser(String username, String password) {
	try (
		FileOutputStream outFile = new FileOutputStream(path_to_users);
	    ) {
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outFile));
	    Cliente cl = new Cliente(username, password); 
	    bw.append(cl.toString());
	} catch(Exception e) {
	    System.err.println("[USERDATABASE:ERROR]" + e.getMessage());
	}
    }
}
