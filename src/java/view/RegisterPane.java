package view;
import controller.Controller;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class RegisterPane extends GridPane {
	private Controller controller;
    public RegisterPane(Controller controller) {
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

	Button btnRegister = new Button("Create User");
	btnRegister.getStyleClass().add("btn-secondary");

	btnRegister.setOnAction( e-> { 
	    controller.addUser(tfUsername.getText(), tfPassword.getText());
		this.fireEvent(new UserEvent(UserEvent.REGISTRATION_SUCCEDED));
	    
	});

	// col, row
	this.add(lblUsername, 0, 1);this.add(tfUsername, 1, 1);
	this.add(lblPassword, 0, 2);this.add(tfPassword, 1, 2);
	this.add(btnRegister, 0, 3);
    }
}
