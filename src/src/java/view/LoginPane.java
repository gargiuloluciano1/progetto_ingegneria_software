package view;
import controller.Controller;
import javafx.event.*;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class LoginPane extends GridPane {
    private Controller controller;

    public LoginPane(Controller controller) {
	super();
	// DEBUG
	this.controller = controller;

	setGridLinesVisible(true);
	Label lblUsername = new Label("Username");
	TextField tfUsername = new TextField();

	Label lblPassword = new Label("Password");
	TextField tfPassword = new TextField();

	Button btnRegister = new Button("Register");
	Button btnLogin = new Button("Login");

	// Event Management
	btnLogin.setOnAction( e->{ 
	    if (controller.checkCredentials(tfUsername.getText(), tfPassword.getText()) == 1) {
		this.fireEvent(new UserEvent(UserEvent.LOGIN_SUCCEDED));
	    }
	});

	// col, row
	this.add(lblUsername, 0, 1);this.add(tfUsername, 1, 1);
	this.add(lblPassword, 0, 2);this.add(tfPassword, 1, 2);
	this.add(btnRegister, 0, 3);this.add(btnLogin, 1, 3);
    }
}
