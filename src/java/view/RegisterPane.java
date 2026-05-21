package view;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class RegisterPane extends GridPane {
    public RegisterPane() {
	super();
	// DEBUG
	setGridLinesVisible(true);

	Label lblUsername = new Label("Username");
	TextField tfUsername = new TextField();

	Label lblPassword = new Label("Password");
	TextField tfPassword = new TextField();

	Button btnRegister = new Button("Register");

	// col, row
	this.add(lblUsername, 0, 1);this.add(tfUsername, 1, 1);
	this.add(lblPassword, 0, 2);this.add(tfPassword, 1, 2);
	this.add(btnRegister, 0, 3);
    }
}
