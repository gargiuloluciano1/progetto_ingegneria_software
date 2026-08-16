package model;
import java.io.*;


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
			line = br.readLine(); //brucio gli headers del csv
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
    public void createUser(String username, String password) {
		try (
			FileWriter outFile = new FileWriter(path_to_users, true);
			) {
			Cliente cl = new Cliente(username, password); 
			outFile.append(cl.toString()).append(System.lineSeparator());
			outFile.close();

		} catch(Exception e) {
			System.err.println("[USERDATABASE:ERROR]" + e.getMessage());
		}
    }

	public boolean checkUser(String username, String password){
		
		try (BufferedReader br = new BufferedReader(new FileReader(path_to_users))){

			String linea = br.readLine(); //brucio gli headers del csv
			while((linea = br.readLine())!=null){
				
				String[] tokens = linea.split(",");
				if (tokens[1].trim().equals(username) && tokens[2].trim().equals(password)){
					return true;
				}
			}
			br.close();
		}catch(Exception e){
			System.err.println("[USERDATABASE:ERROR]" + e.getMessage());
		}
		return false;

	}
}
