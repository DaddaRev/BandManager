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
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Represents the graphical user interface (GUI) for the client main menu related 
 * to the management mode by bands where, once authenticated, it will be possibile to 
 * manage songs and events.
 * When the corresponding buttons are pressed, the various interfaces that 
 * handle all possible functions are handled.
 * 
 * @author Davide Reverberi
 */
public class BandManagementGUI 
{

	private static List<Song> songsList = new ArrayList<>(); 			 //List of the songs per selected band
	private static List<Event> eventList = new ArrayList<>();			 //List of the events per selected band
	
	public BandManagementGUI() 
	{
		//Empty contructor
	}
	
	/**
	 * Creates a TableView for displaying a list of songs.
	 *
	 * @param observableSongList The ObservableList containing the songs to be displayed in the table.
	 * @return A TableView object configured to display songs.
	 */
	@SuppressWarnings("unchecked")
	private static TableView<Song> createSongsTable(ObservableList<Song> observableSongsList) {
        TableView<Song> tableView = new TableView<>(observableSongsList);

        TableColumn<Song, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        
        TableColumn<Song, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("Author"));

        TableColumn<Song, Float> lengthColumn = new TableColumn<>("Length");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("Length"));
        
        TableColumn<Song, Integer> bpmColumn = new TableColumn<>("Bpm");
        bpmColumn.setCellValueFactory(new PropertyValueFactory<>("Bpm"));

        tableView.getColumns().addAll(nameColumn, authorColumn, lengthColumn, bpmColumn);

        return tableView;
    }
	
	/**
	 * Creates a TableView for displaying a list of events.
	 *
	 * @param observableEventList The ObservableList containing the events to be displayed in the table.
	 * @return A TableView object configured to display events.
	 */
	@SuppressWarnings("unchecked")
	private static TableView<Event> createEventsTable(ObservableList<Event> observableEventsList) {
        TableView<Event> tableView = new TableView<>(observableEventsList);

        TableColumn<Event, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        
        TableColumn<Event, String> placeColumn = new TableColumn<>("Place");
        placeColumn.setCellValueFactory(new PropertyValueFactory<>("Place"));

        TableColumn<Event, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        
        TableColumn<Event, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));

        tableView.getColumns().addAll(nameColumn, placeColumn, dateColumn, typeColumn);

        return tableView;
    }
	
	/**
	 * Displays the interface for adding a new song.
	 *
	 * @param client The Socket for communication with the server.
	 * @param is The BufferedReader for reading data from the server.
	 * @param os The DataOutputStream for writing data to the server.
	 * @param selectedBand The name of the selected band.
	 */
	public static void addSongInterface(Socket client, BufferedReader is, DataOutputStream os, String selectedBand)
	{
		Stage addWindowStage = new Stage();
		
		Label titleLabel = new Label("Add a new song:");
        titleLabel.setFont(new Font("Arial", 15));
        
        Label nameLabel = new Label("Song name:");
        TextField nameField = new TextField();
        
        Label authorLabel = new Label("Author:");
        TextField authorField = new TextField();
        
        Label lengthLabel = new Label("Length (in seconds):");
        TextField lengthField = new TextField();

        Label bpmLabel = new Label("Bpm:");
        TextField bpmField = new TextField();
        
        titleLabel.getStyleClass().add("custom-title");
        nameLabel.getStyleClass().add("custom-label");
        authorLabel.getStyleClass().add("custom-label");
        lengthLabel.getStyleClass().add("custom-label");
        bpmLabel.getStyleClass().add("custom-label");

        // Buttons
        Button okButton = new Button("ok");
        Button backButton = new Button("back");
        
        okButton.getStyleClass().add("custom-button");
        backButton.getStyleClass().add("custom-button");
        
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(35, 50, 60, 50));

        //Creating and displaying the fields to enter the features of the new song
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(authorLabel, 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(lengthLabel, 0, 2);
        grid.add(lengthField, 1, 2);
        grid.add(bpmLabel, 0, 3);
        grid.add(bpmField, 1, 3);
        
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("custom-box");

        BorderPane.setMargin(titleLabel, new Insets(60, 0, 0, 60));
        borderPane.setTop(titleLabel);
        borderPane.setCenter(grid);

        HBox buttonBox = new HBox(30); 
        buttonBox.getChildren().addAll(okButton, backButton);
        buttonBox.setPadding(new Insets(10));
        
        BorderPane.setMargin(buttonBox, new Insets(0, 0, 500, 140));
        borderPane.setBottom(buttonBox);
        
        //ok button handling
        okButton.setOnAction(e -> {
        	try
        	{
        		//Creating an alert if some parameter format rules are violated or they are all correct
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
	            alert.setTitle("add song alert");
	            alert.setHeaderText(null);
	            
	        	if(!nameField.getText().isEmpty()) //True if name field isn't empty
	        	{
	        		if (lengthField.getText().matches("-?\\d+(\\.\\d+)?") && bpmField.getText().matches("-?\\d+(\\.\\d+)?")) //Regular expression to check if length and bpm are numbers
	        		{
	        			os.writeBytes("add_song\n"); //Telling the server to add a new song from server in his list
	        			
	        			if ( authorField.getText().isEmpty())
	        			{
		    				os.writeBytes(nameField.getText()+" "+"unknow "+ lengthField.getText()+" "+bpmField.getText()+ selectedBand + "\n");
	        			}else {
	        				os.writeBytes(nameField.getText()+" "+authorField.getText()+" "+ lengthField.getText()+" "+bpmField.getText()+" "+ selectedBand + "\n");
						}
	        			
	        			String serverAnwser = is.readLine(); //Blocking instruction, waits for the outcome of adding the song to the server's list	
	    	            
	    	            if(serverAnwser.equals("alreadyin")) //Waiting for the server to receive the song back.
	    	            {
	    	            	alert.setContentText(nameField.getText() + " already exist!");
	    					
	    	            }else { //is returns "ok" --> Operation done
	    	            	alert.setContentText("song succesfully added in the list!");
	    				} 
	        		}else // length and bpm are not a number --> Invalid
	        		{
	        			alert.setContentText("Insert valid length or bpm!\n(Insert 0 if unknow)");
	        		}    
	        	}else { //Name fields is empty, cannot add the song
	        		
					alert.setContentText("Add a song with a valid name!");
				}
	
	            alert.showAndWait(); //Waiting for the user to close the alert
				
	            addWindowStage.close();
	            BandManagementGUI.showManageSongsInterface(client, is, os, selectedBand); //Returns back to the songs display
	            
        	} catch (IOException e1) 
    		{
				e1.printStackTrace();
			}
        });
        
        //back button handling
        backButton.setOnAction(e -> {
        	try {
				BandManagementGUI.showManageSongsInterface(client, is, os, selectedBand); //Returns back to the songs display
			} catch (IOException e1) {
				e1.printStackTrace();
			} //Open a new add window
        	addWindowStage.close();
        	
        });
        
        Scene scene = new Scene(borderPane, 450, 450);

        //Importing css style file for custom layouts
        scene.getStylesheets().add(ClientLoginGUI.class.getResource("texture.css").toExternalForm());

        addWindowStage.setScene(scene);
        addWindowStage.show();
	}
	
	/**
	 * Displays the interface for adding a new event.
	 *
	 * @param client The Socket for communication with the server.
	 * @param is The BufferedReader for reading data from the server.
	 * @param os The DataOutputStream for writing data to the server.
	 * @param selectedBand The name of the selected band.
	 */
	public static void addEventInterface(Socket client, BufferedReader is, DataOutputStream os, String selectedBand)
	{
		Stage addWindowStage = new Stage();
		
		Label titleLabel = new Label("Add a new event:");
        titleLabel.setFont(new Font("Arial", 15));
        
        Label nameLabel = new Label("Event name:");
        TextField nameField = new TextField();
        
        Label placeLabel = new Label("Place:");
        TextField placeField = new TextField();
        
        Label typeLabel = new Label("Type:");
        TextField typeField = new TextField();

        Label dateLabel = new Label("Date:");
        TextField dateField = new TextField();
        
        titleLabel.getStyleClass().add("custom-title");
        nameLabel.getStyleClass().add("custom-label");
        placeLabel.getStyleClass().add("custom-label");
        typeLabel.getStyleClass().add("custom-label");
        dateLabel.getStyleClass().add("custom-label");

        // Buttons
        Button okButton = new Button("ok");
        Button backButton = new Button("back");
        
        okButton.getStyleClass().add("custom-button");
        backButton.getStyleClass().add("custom-button");
        
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(35, 50, 60, 50));

        //Creating and displaying the fields to enter the features of the new event
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(placeLabel, 0, 1);
        grid.add(placeField, 1, 1);
        grid.add(typeLabel, 0, 2);
        grid.add(typeField, 1, 2);
        grid.add(dateLabel, 0, 3);
        grid.add(dateField, 1, 3);
        
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("custom-box");

        BorderPane.setMargin(titleLabel, new Insets(60, 0, 0, 60));
        borderPane.setTop(titleLabel);
        borderPane.setCenter(grid);

        HBox buttonBox = new HBox(30); 
        buttonBox.getChildren().addAll(okButton, backButton);
        buttonBox.setPadding(new Insets(10));
        
        BorderPane.setMargin(buttonBox, new Insets(0, 0, 500, 140));
        borderPane.setBottom(buttonBox);
        
        //ok button handling
        okButton.setOnAction(e -> {
        	try
        	{
        		//Creating an alert if some parameter format rules are violated or they are all correct
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
	            alert.setTitle("add event alert");
	            alert.setHeaderText(null);
	            
	        	if(!nameField.getText().isEmpty() && !typeField.getText().isEmpty() && !dateField.getText().isEmpty()) //True if name, type and date fields aren't empty
	        	{
	        		os.writeBytes("add_event\n"); //Telling the server to add a new event from server in his list
	        			
	        		if ( placeField.getText().isEmpty())
	        		{
		    			os.writeBytes(nameField.getText()+" "+"unknow "+ dateField.getText()+" "+typeField.getText()+ selectedBand + "\n");
	        		}else {
	        			os.writeBytes(nameField.getText()+" "+placeField.getText()+" "+ dateField.getText()+" "+typeField.getText()+" "+ selectedBand + "\n");
					}
	        		
	        		String serverAnwser = is.readLine(); //Blocking instruction, waits for the outcome of adding the event to the server's list	
	    	           
	    	        if(serverAnwser.equals("alreadyin")) 
	    	        {
	    	        	alert.setContentText(nameField.getText() + " already exist!");
	    					
	    	        }else { //is returns "ok" --> Operation done
	    	            	alert.setContentText("event succesfully added in the list!");
	    	        }  
	        	}else { //Name fields is empty, cannot add the event
	        		
					alert.setContentText("Add an event with a valid name!");
				}
	
	            alert.showAndWait(); //Waiting for the user to close the alert
				
	            addWindowStage.close();
	            BandManagementGUI.showManageEventsInterface(client, is, os, selectedBand);  //Returns back to the events display
	            
        	} catch (IOException e1) 
    		{
				e1.printStackTrace();
			}
        });
        
        //back button handling
        backButton.setOnAction(e -> {
        	try {
				BandManagementGUI.showManageEventsInterface(client, is, os, selectedBand);	//Returns back to the events display
			} catch (IOException e1) {
				e1.printStackTrace();
			} //Open a new add window
        	addWindowStage.close();
        	
        });
        
        Scene scene = new Scene(borderPane, 450, 450);

        //Importing css style file for custom layouts
        scene.getStylesheets().add(ClientLoginGUI.class.getResource("texture.css").toExternalForm());

        addWindowStage.setScene(scene);
        addWindowStage.show();
	}
	
	/**
	 * Displays the interface for managing songs of a selected band.
	 *
	 * @param client The Socket for communication with the server.
	 * @param is The BufferedReader for reading data from the server.
	 * @param os The DataOutputStream for writing data to the server.
	 * @param selectedBand The name of the selected band.
	 * @throws IOException If an I/O error occurs.
	 */
	public static void showManageSongsInterface(Socket client, BufferedReader is, DataOutputStream os, String selectedBand) throws IOException
	{
		songsList.clear();
		os.writeBytes("show_songs\n");  //Telling the server to send the songs
		GuestMainGUI.getSongsListFromServer(is, os, selectedBand); 		// Function provided by GuestMainGUI

		songsList = GuestMainGUI.getSongsList(); //Function provided by GuestMainGUI
		
		ObservableList<Song> observableSongsList = FXCollections.observableArrayList(songsList);
		
		Stage showStage = new Stage();
		showStage.setTitle(selectedBand +" setlist management");
		
		Button backButton = new Button("Back");
		Button closeButton = new Button("Close");
		Button addButton = new Button("Add");
		Button deleteButton = new Button("Delete");
		
        backButton.getStyleClass().add("custom-button");
        closeButton.getStyleClass().add("custom-button");
        addButton.getStyleClass().add("custom-button");
        deleteButton.getStyleClass().add("custom-button");
		
		TableView<Song> tableView = createSongsTable(observableSongsList);
        
        Label titleLabel = new Label("Songs currently in the setlist:");
        titleLabel.getStyleClass().add("custom-title");
        
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN); //Using all the available space
        tableView.setMinWidth(300); // Minimum width of the table
        
		BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setCenter(tableView);
        root.setTop(titleLabel);
        root.getStyleClass().add("custom-box");
        
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 15, 0));
        
        // Creating an HBox for the buttons
        HBox buttonBox = new HBox(25);	 // Spacing of 25 pixels between the buttons
        buttonBox.getChildren().addAll(backButton, addButton, deleteButton, closeButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle("-fx-alignment: CENTER;"); 	// Center the buttons inside the HBox
        
        root.setBottom(buttonBox);
        
        //back button handler:
        backButton.setOnAction(e -> {  
        	BandManagementGUI.mainDisplay(client, is, os, selectedBand);  //Return back to the main display function
        	showStage.close();
        });
        
        addButton.setOnAction(e -> {  
        	BandManagementGUI.addSongInterface(client, is, os, selectedBand);	//Calling the specific class method to add a new song
        	showStage.close();
        });
        
        //delete button handler:
        deleteButton.setOnAction(e -> {  
        	if (!tableView.getSelectionModel().isEmpty()) //If true, a song in the list shown is selected and ready to be deleted
        	{  
        		try {
					Song selectedSong = tableView.getSelectionModel().getSelectedItem();
					songsList.remove(selectedSong);
					
					os.writeBytes("remove_song\n");
					os.writeBytes(selectedSong.getName()+"\n");  // Only the name is required to perform the removal of the song
										
					//Creating an alert for displaying to the user the outcome of the removal
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
		            alert.setTitle("Song viewer");
		            alert.setHeaderText(null);
		            
		            //Blocking instruction, wait for the outcome of adding the song to the server list:
		            String serverAnwser = is.readLine();
		            
		            System.out.println(serverAnwser);
		            
		            if(serverAnwser.equals("ok")) //Song has been removed 
		            {
						alert.setContentText(selectedSong.getName() + " succesfully removed from the setlist");
						
		            }else {
						alert.setContentText("Operation failed, try again!");
		            }
		            
		            alert.showAndWait(); //Waiting for the user to close the alert
		            showStage.close();
		            
		            //If the removal was successful, refresh the window by reopening it.
			        BandManagementGUI.showManageSongsInterface(client, is, os, selectedBand); 
		            
				} catch (IOException e1)
        		{
					e1.printStackTrace();
				} 
        	}else 
        	{
        		Label titleLabel2 = new Label("Select a song!");
        		titleLabel2.getStyleClass().add("custom-title");
        		root.setTop(titleLabel2);
        	}
        });
        
        //close button handler:
        closeButton.setOnAction(e ->{ 
        	try {
				os.writeBytes("quit\n"); 	//Close the connection and the application
			} catch (IOException e1) {
				e1.printStackTrace();
			}	
        	showStage.close();
        });
        
        Scene scene = new Scene(root, 800, 400);
        
        //Importing css style file for custom layouts
        scene.getStylesheets().add(GuestMainGUI.class.getResource("texture.css").toExternalForm());
        
        showStage.setScene(scene);
        showStage.show();
	}
	
	/**
	 * Displays the interface for managing events of a selected band.
	 *
	 * @param client The Socket for communication with the server.
	 * @param is The BufferedReader for reading data from the server.
	 * @param os The DataOutputStream for writing data to the server.
	 * @param selectedBand The name of the selected band.
	 * @throws IOException If an I/O error occurs.
	 */
	public static void showManageEventsInterface(Socket client, BufferedReader is, DataOutputStream os, String selectedBand) throws IOException
	{
		eventList.clear();
		os.writeBytes("show_events\n");  //Telling the server to send the events
		GuestMainGUI.getEventsListFromServer(is, os, selectedBand);   // Function provided by GuestMainGUI
		
		eventList = GuestMainGUI.getEventsList();
		
		ObservableList<Event> observableEventsList = FXCollections.observableArrayList(eventList);
		
		Stage showStage = new Stage();
		showStage.setTitle(selectedBand+" event management");
		
		Button backButton = new Button("Back");
		Button closeButton = new Button("Close");
		Button addButton = new Button("Add");
		Button deleteButton = new Button("Delete");
		
        backButton.getStyleClass().add("custom-button");
        closeButton.getStyleClass().add("custom-button");
        addButton.getStyleClass().add("custom-button");
        deleteButton.getStyleClass().add("custom-button");
		
		TableView<Event> tableView = createEventsTable(observableEventsList);
        
        Label titleLabel = new Label("Events currently in the setlist:");
        titleLabel.getStyleClass().add("custom-title");
        
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN); //Using all the available space
        tableView.setMinWidth(300); // Minimum width of the table
        
		BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setCenter(tableView);
        root.setTop(titleLabel);
        root.getStyleClass().add("custom-box");
        
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 15, 0));
        
        // Creating an HBox for the buttons
        HBox buttonBox = new HBox(20);	 // Spacing of 10 pixels between the buttons
        buttonBox.getChildren().addAll(backButton, addButton, deleteButton, closeButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle("-fx-alignment: CENTER;"); 	// Center the buttons inside the HBox
        
        root.setBottom(buttonBox);
        
        //back button handler:
        backButton.setOnAction(e -> {  
        	BandManagementGUI.mainDisplay(client, is, os, selectedBand); 	//Returns back to the main display
        	showStage.close();
        });
        
        //add button handler:
        addButton.setOnAction(e -> {  
        	BandManagementGUI.addEventInterface(client, is, os, selectedBand); 	//Calling the specific class method to add a new song
        	showStage.close();
        });
        
        deleteButton.setOnAction(e -> {  
        	if (!tableView.getSelectionModel().isEmpty()) //If true, an event in the list shown is selected and ready to be deleted
        	{  
        		try {
					Event selectedEvent = tableView.getSelectionModel().getSelectedItem();
					eventList.remove(selectedEvent);
					
					os.writeBytes("remove_event\n");
					os.writeBytes(selectedEvent.getName()+"\n");  // Only the name is required to perform the removal of the event
								
					//Creating an alert to display to the user the outcome of the removal
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
		            alert.setTitle("Event viewer");
		            alert.setHeaderText(null);
		            
		            //Blocking instruction, wait for the outcome of adding the event to the server list:
		            String serverAnwser = is.readLine();
		            
		            if(serverAnwser.equals("ok")) //event has been removed 
		            {
						alert.setContentText(selectedEvent.getName() + " succesfully removed ");
						
		            }else {
						alert.setContentText("Operation failed, try again!");
		            }
		            
		            alert.showAndWait(); //Waiting for the user to close the alert
		            showStage.close();
		            
		            //If the removal was successful, refresh the window by reopening it.
			        BandManagementGUI.showManageEventsInterface(client, is, os, selectedBand);
		            
				} catch (IOException e1)
        		{
					e1.printStackTrace();
				} 
        	}else 
        	{
        		Label titleLabel2 = new Label("Select an event!");
        		titleLabel2.getStyleClass().add("custom-title");
        		root.setTop(titleLabel2);
        	}
        });
        
        //close button handler:
        closeButton.setOnAction(e ->{ 
        	try {
				os.writeBytes("quit\n"); 	//Close the connection and the application
			} catch (IOException e1) {
				e1.printStackTrace();
			}	
        	showStage.close();
        });
        
        Scene scene = new Scene(root, 800, 400);
        
        //Importing css style file for custom layouts
        scene.getStylesheets().add(GuestMainGUI.class.getResource("texture.css").toExternalForm());
        
        showStage.setScene(scene);
        showStage.show();
	}
	
	/**
	 * Displays the main management interface for selecting songs or events to manage.
	 * The interface should be seen as a kind of intermediate choice display.
	 *
	 * @param client The Socket for communication with the server.
	 * @param is The BufferedReader for reading data from the server.
	 * @param os The DataOutputStream for writing data to the server.
	 * @param selectedBand The name of the selected band.
	 */
	public static void mainDisplay(Socket client, BufferedReader is, DataOutputStream os, String selectedBand)
	{
		Stage choiceWindowStage = new Stage();
		
        Label titleLabel = new Label(selectedBand+",\n\nChoose what you want to manage: ");
        titleLabel.getStyleClass().add("custom-label2");
		
		Button songsButton = new Button("Songs");
        Button eventsButton = new Button("Events");
        Button backButton = new Button("Back");
        
        songsButton.getStyleClass().add("custom-button");
        eventsButton.getStyleClass().add("custom-button");
        backButton.getStyleClass().add("custom-button");

        HBox buttonsLayout = new HBox(25);
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.getChildren().addAll(songsButton, eventsButton, backButton);
        buttonsLayout.setPadding(new Insets(10));

        // Interface creation
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setHgap(20);
        root.add(titleLabel, 0, 0);
        root.add(buttonsLayout, 0, 1);
        root.getStyleClass().add("custom-box");
        
        GridPane.setMargin(titleLabel, new Insets(0, 0, 40, 0));
        
        //songs button handler:
        songsButton.setOnAction(e -> {   
        	try {
				BandManagementGUI.showManageSongsInterface(client, is, os, selectedBand);  //Call to the class-specific method for handling songs
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			choiceWindowStage.close(); 
        });
        
        //events button handler:
        eventsButton.setOnAction(e -> {   
        	try {
				BandManagementGUI.showManageEventsInterface(client, is, os, selectedBand);	//Call to the class-specific method for handling events
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			choiceWindowStage.close(); 
        });
        
        //back button handler:
        backButton.setOnAction(e -> {   
        	try {
        		
        		os.writeBytes("back\n");
				Stage primaryStage = new Stage();
	        	ClientLoginGUI.loginDisplay(primaryStage);	//Return to main menu (mainDisplay function)
	        	choiceWindowStage.close();
	        	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        });

        Scene scene = new Scene(root, 400, 300);
        
        //Importing css style file for custom layouts
        scene.getStylesheets().add(GuestMainGUI.class.getResource("texture.css").toExternalForm());
        
        choiceWindowStage.setScene(scene);
        choiceWindowStage.setTitle("manage mode");
        choiceWindowStage.show();
	}
}
