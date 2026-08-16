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
	this.controller = controller;

	this.getStyleClass().add("login-card");
	this.setHgap(10);
	this.setVgap(15);
	this.setAlignment(javafx.geometry.Pos.CENTER);

	Label lblUsername = new Label("Username");
	lblUsername.getStyleClass().add("login-subtitle");
	TextField tfUsername = new TextField();
	tfUsername.setPromptText("Username");

	Label lblPassword = new Label("Password");
	lblPassword.getStyleClass().add("login-subtitle");
	TextField tfPassword = new TextField();
	tfPassword.setPromptText("Password");

	Button btnRegister = new Button("Register");
	btnRegister.getStyleClass().add("btn-secondary");

	Button btnLogin = new Button("Login");
	btnLogin.getStyleClass().add("btn-login");

	btnLogin.setOnAction( e->{ 
	    if (controller.checkCredentials(tfUsername.getText(), tfPassword.getText()) == 1) {
			this.fireEvent(new UserEvent(UserEvent.LOGIN_SUCCEDED));
	    }else{
			this.fireEvent(new UserEvent(UserEvent.LOGIN_FAILED));
		}
	});

	btnRegister.setOnAction( e->{ 
	    
		this.fireEvent(new UserEvent(UserEvent.REGISTRATION));
	    
	});

	this.add(lblUsername, 0, 1); this.add(tfUsername, 1, 1);
	this.add(lblPassword, 0, 2); this.add(tfPassword, 1, 2);
	this.add(btnRegister, 0, 3); this.add(btnLogin, 1, 3);
    }
}