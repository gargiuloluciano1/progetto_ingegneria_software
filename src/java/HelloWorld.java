
import model.Model;
import controller.Controller;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
//import javafx.scene.layout.StackPane;
import view.*;
import javafx.stage.Stage;
 
public class HelloWorld extends Application {
    private final int WIDTH  = 600; 
    private final int HEIGHT = 500;

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
	/*
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
	*/
	Model model = new Model();
	Controller controller = new Controller();

        Parent root = new LoginPane(controller);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());
        primaryStage.setScene(scene);
        

	// STAGE Event Handling 
	//handle event
	primaryStage.addEventFilter(UserEvent.ANY, e -> {
	    if (e.getEventType() == UserEvent.LOGIN_SUCCEDED) {
            System.out.println("Login Succeded!!!");
        
            primaryStage.setScene(new Scene(new RegisterPane(controller), WIDTH, HEIGHT));

	    } else if (e.getEventType() == UserEvent.REGISTRATION_SUCCEDED) {

            primaryStage.setScene(new Scene(new LoginPane(controller), WIDTH, HEIGHT));
            System.out.println("Registration Succeded!!!");

        }else if(e.getEventType() == UserEvent.REGISTRATION){

            System.out.println("New Registration Request");
            primaryStage.setScene(new Scene(new RegisterPane(controller), WIDTH, HEIGHT));

        }
        else {
		    System.out.println("Login Failed");
	    }
	});

        primaryStage.show();
    }
}
