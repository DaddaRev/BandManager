/*
 * BandManager by Davide Reverberi (matr. 332781)
 * 
 * UniPr, Software Engineering course.
 * Last Modified: 1/03/2024
 * 
 */
package BandManagerPackage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Represents the graphical user interface (GUI) for the client login in the BandManager application.
 * The class extends the JavaFX Application class and provides methods to handle user authentication
 * by connecting to the server and displaying a login window.
 * Upon successful login, it transitions to the main display of the BandManager application,
 * depending on the guest mode or not.
 * 
 * @author Davide Reverberi
 */
public class ClientLoginGUI extends Application {

	private static final int SPORT = 4444;
	private static final String SHOST = "localhost";
	
	/**
	 * Attempts to authenticate a client by sending the provided username and password to the server.
	 * The authentication process involves sending the credentials to the server, receiving a response,
	 * and checking whether the authentication was successful.
	 *
	 * @param client Socket object representing the client connection.
	 * @param os DataOutputStream object for sending data to the server.
	 * @param is BufferedReader object for reading data from the server.
	 * @param user Username to be used for authentication.
	 * @param password Password to be used for authentication.
	 * @return true if the authentication is successful; false otherwise.
	 */
	private static boolean login(Socket client, DataOutputStream os, BufferedReader is, String user, String password)
	{
		try 
		{
			os.writeBytes(user+"\n");
			os.writeBytes(password+"\n");
			
			if(!is.readLine().equals("ok"))    //Reading the outcome of the authentication process
			{
				return false;
			}else {
				System.out.println("Succesfully logged in\n");
			}
			return true;
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Handles the login process for the Band Manager application.
	 * This method establishes a connection with the server and sends
	 * the user credentials for authentication. It also handles guest login.
	 *
	 * @param userText The TextField containing the band name entered by the user.
	 * @param passwordText The PasswordField containing the password entered by the user.
	 * @param grid The GridPane layout where the login interface is displayed.
	 * @param primaryStage The primary stage of the Band Manager application.
	 * @param guest A boolean indicating whether the login attempt is a guest login.
	 */
	private static void handleLogin(TextField userText, PasswordField passwordText, GridPane grid, Stage primaryStage, boolean guest)
	{
		try
			{
				//Creating a client every time the login or login as guest button is pressed
				Socket client = new Socket(SHOST, SPORT);
		    	BufferedReader   is = new BufferedReader(new InputStreamReader(client.getInputStream()));
				DataOutputStream os = new DataOutputStream(client.getOutputStream());
				
				if (!guest)  //If it's not in guest mode, check the credentials to allow user login
				{
					os.writeBytes("noguest\n");  // Telling the server authentication is required
					
					String bandUser = userText.getText();
					String password = passwordText.getText();
					
		            if(ClientLoginGUI.login(client, os, is, bandUser, password))
		            {
			            BandManagementGUI.mainDisplay(client, is, os, bandUser); 	//Main menu performed in the ClientMainGUI class
			            primaryStage.close();

		            }else {
		                final Text actiontarget = new Text();
		                grid.add(actiontarget, 0, 0, 2, 1);
		                
		            	actiontarget.setFill(Color.WHITE);
		            	actiontarget.setText("Wrong user or password!");
		            	userText.clear();
		                passwordText.clear();
		            	client.close();	           
		            }
		            
				}else	// guest mode
				{
					os.writeBytes("guest\n");  // Telling the server authentication is not required
					
					GuestMainGUI.showBandsInterface(client, is, os);
					primaryStage.close();
				}
				
			} catch (UnknownHostException e1) 
			{
				e1.printStackTrace();
				
			} catch (IOException e1) 
			{
				e1.printStackTrace();
			}
	}
	
	/**
	 * Displays the login interface for the Band Manager application.
	 * This method creates a JavaFX stage with fields for entering the band name
	 * and password, along with buttons for login and guest login.
	 *
	 * @param primaryStage The primary stage where the login interface will be displayed.
	 */
	public static void loginDisplay(Stage primaryStage) {
				
		Label titleLabel = new Label("Band Manager - Login");
        titleLabel.setFont(new Font("Arial", 20));
        
        Label usernameLabel = new Label("Band name:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        
        titleLabel.getStyleClass().add("custom-title");
        usernameLabel.getStyleClass().add("custom-label");
        passwordLabel.getStyleClass().add("custom-label");

        // Buttons
        Button loginButton = new Button("Login");
        Button guestButton = new Button("Login as Guest");
        Button closeButton = new Button("Close");
        
        loginButton.getStyleClass().add("custom-button");
        guestButton.getStyleClass().add("custom-button");
        closeButton.getStyleClass().add("custom-button");
        
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(35, 60, 60, 60));

        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("custom-box");

        BorderPane.setMargin(titleLabel, new Insets(60, 0, 0, 60));
        borderPane.setTop(titleLabel);
        borderPane.setCenter(grid);

        HBox buttonBox = new HBox(30); 
        buttonBox.getChildren().addAll(loginButton, guestButton);
        buttonBox.setPadding(new Insets(10));
        
        BorderPane.setMargin(buttonBox, new Insets(0, 0, 100, 110));
        borderPane.setBottom(buttonBox);
        
        //Buttons handling
        loginButton.setOnAction(e -> handleLogin(usernameField, passwordField, grid, primaryStage, false));
        guestButton.setOnAction(e -> handleLogin(usernameField, passwordField, grid, primaryStage, true));
        
        Scene scene = new Scene(borderPane, 450, 400);

        scene.getStylesheets().add(ClientLoginGUI.class.getResource("texture.css").toExternalForm()); //Import the css file to use custom styles

        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
    /**
     * Overrides the start method from the Application class.
     * Initializes the primary stage and displays the login interface
     * using the loginDisplay method from the ClientLoginGUI class.
     *
     * @param primaryStage The primary stage for the Band Manager GUI.
     */
	@Override
	public void start(Stage primarStage)
	{
		ClientLoginGUI.loginDisplay(primarStage);
	}

	 /**
     * The main entry point for the Band Manager application.
     * Launches the JavaFX application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
