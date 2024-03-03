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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Represents the graphical user interface (GUI) for the client main menu related 
 * to the guest mode and some other useful client functions for the bandManager application.
 * When the corresponding buttons are pressed, the various interfaces that 
 * handle all possible functions are handled.
 * 
 * @author Davide Reverberi
 */
public class GuestMainGUI 
{
	
	private static List<String> bandNamesList = new ArrayList<>();		 //List of the band names
	private static List<Song> songsList = new ArrayList<>(); 			 //List of the songs per selected band
	private static List<Event> eventList = new ArrayList<>();			 //List of the events per selected band
	
	public GuestMainGUI()
	{
		//Empty costructor
	}
	
	/**
	 * Retrieves band names from the server and populates the bandNamesList.
	 *
	 * @param is The BufferedReader for reading data from the server.
	 * @param os The DataOutputStream for writing data to the server.
	 */
	private static void getNamesFromServer(BufferedReader is, DataOutputStream os)
	{
		bandNamesList.clear();
		String data_in;
		try {
			
			data_in = is.readLine();
			while(!data_in.equals("end")) //Getting band names from server
			{
	            bandNamesList.add(data_in); 
				data_in = is.readLine();
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieves the list of songs.
	 * Useful function for other client's methods
	 *
	 * @return The list of Song objects.
	 */
	public static List<Song> getSongsList()
	{
		return songsList;
	}
	
	/**
	 * Retrieves the list of events.
	 * Useful function for other client's methods
	 *
	 * @return The list of Event objects.
	 */
	public static List<Event> getEventsList()
	{
		return eventList;
	}
	 
	/**
	 * Retrieves the list of songs for the selected band from the server and populates the songsList.
	 *
	 * @param is The BufferedReader for reading data from the server.
	 * @param os The DataOutputStream for writing data to the server.
	 * @param selectedBand The name of the selected band for which songs are to be retrieved.
	 */
	public static void getSongsListFromServer (BufferedReader is, DataOutputStream os, String selectedBand)
	{
		songsList.clear();
		String data_in;
		try {
			
			os.writeBytes(selectedBand+"\n");	//Sending the band name to server
			data_in = is.readLine();
			
			while(!data_in.equals("endsongs")) //Getting songs List from server until "endsongs" occurs.
			{	
				String[] parts = data_in.split(" ");  
	            String songName = parts[0]; 
	            String songAuthor = parts[1]; 
	            float songLength = Float.parseFloat(parts[2]); 
	            int songBpm = Integer.parseInt(parts[3]);
	            songsList.add(new Song(songName, songAuthor, songLength, songBpm, selectedBand)); 	//Adding the song to the local List.

				data_in = is.readLine();
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieves the list of events for the selected band from the server and populates the eventList.
	 *
	 * @param is The BufferedReader for reading data from the server.
	 * @param os The DataOutputStream for writing data to the server.
	 * @param selectedBand The name of the selected band for which events are to be retrieved.
	 */
	public static void getEventsListFromServer (BufferedReader is, DataOutputStream os, String selectedBand)
	{
		eventList.clear();
		String data_in;
		try {
			
			os.writeBytes(selectedBand+"\n");  //Sending band name to server
			data_in = is.readLine();
			
			while(!data_in.equals("endevents")) //Getting songs List from server
			{
				String[] parts = data_in.split(" ");  
	            String eventName = parts[0]; 
	            String eventPlace = parts[1];
	            String eventDate = parts[2];
	            String eventType = parts[3];
	            
	            eventList.add(new Event(eventName, eventPlace, eventDate, eventType, selectedBand)); //Adding the event to the local List.

				data_in = is.readLine();
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a TableView for displaying a list of songs.
	 *
	 * @param observableSongList The ObservableList containing the songs to be displayed in the table.
	 * @return A TableView object configured to display songs.
	 */
	@SuppressWarnings("unchecked")
	private static TableView<Song> createSongTable_songs(ObservableList<Song> observableSongList) {
        TableView<Song> tableView = new TableView<>(observableSongList);

        TableColumn<Song, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        
        TableColumn<Song, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("Author"));

        TableColumn<Song, Float> lengthColumn = new TableColumn<>("Length");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("Length"));
        
        TableColumn<Song, Integer> bpmColumn = new TableColumn<>("Bpm");
        bpmColumn.setCellValueFactory(new PropertyValueFactory<>("Bpm"));
        
        TableColumn<Song, String> performerColomun = new TableColumn<>("Performer");
        performerColomun.setCellValueFactory(new PropertyValueFactory<>("Performer"));

        tableView.getColumns().addAll(nameColumn, authorColumn, lengthColumn, bpmColumn, performerColomun);

        return tableView;
    }
	
	/**
	 * Creates a TableView for displaying a list of events.
	 *
	 * @param observableEventList The ObservableList containing the events to be displayed in the table.
	 * @return A TableView object configured to display events.
	 */
	@SuppressWarnings("unchecked")
	private static TableView<Event> createEventTable_events(ObservableList<Event> observableEventList) {
        TableView<Event> tableView = new TableView<>(observableEventList);

        TableColumn<Event, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        
        TableColumn<Event, String> placeColumn = new TableColumn<>("Place");
        placeColumn.setCellValueFactory(new PropertyValueFactory<>("Place"));

        TableColumn<Event, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        
        TableColumn<Event, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        
        TableColumn<Event, String> performerColomun = new TableColumn<>("Performer");
        performerColomun.setCellValueFactory(new PropertyValueFactory<>("Performer"));

        tableView.getColumns().addAll(nameColumn, placeColumn, dateColumn, typeColumn, performerColomun);

        return tableView;
    }
	
	/**
	 * Displays the band data interface for selecting between songs and events.
	 * The interface should be seen as a kind of intermediate choice display.
	 *
	 * @param client The Socket for communication with the server.
	 * @param is The BufferedReader for reading data from the server.
	 * @param os The DataOutputStream for writing data to the server.
	 * @param selectedBand The name of the selected band.
	 */
	public static void showBandDataInterface(Socket client, BufferedReader is, DataOutputStream os, String selectedBand)
	{
		Stage choiceWindowStage = new Stage();
		
        Label titleLabel = new Label("Choose what you want to see for "+selectedBand+": ");
        titleLabel.getStyleClass().add("custom-label");
		
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

        // Creazione del layout principale
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setHgap(20);
        root.add(titleLabel, 0, 0);
        root.add(buttonsLayout, 0, 1);
        root.getStyleClass().add("custom-box");
        
        GridPane.setMargin(titleLabel, new Insets(0, 0, 40, 0));
        
        //songsButton handling
        songsButton.setOnAction(e -> {   
        	try {
				GuestMainGUI.showSongsInterface(client, is, os, selectedBand); //Opens the specific class method to show the songs
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			choiceWindowStage.close(); 
        });
        
        //eventsButton handling
        eventsButton.setOnAction(e -> {   
        	try {
				GuestMainGUI.showEventsInterface(client, is, os, selectedBand);	//Opens the specific class method to show the events
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			choiceWindowStage.close(); 
        });
        
        //backButton handling
        backButton.setOnAction(e -> {   
        	try {
				GuestMainGUI.showBandsInterface(client, is, os); //Opens the specific class to show the available band names
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			choiceWindowStage.close(); 
        });

        Scene scene = new Scene(root, 400, 300);
        
        scene.getStylesheets().add(GuestMainGUI.class.getResource("texture.css").toExternalForm());
        
        choiceWindowStage.setScene(scene);
        choiceWindowStage.setTitle("guest mode");
        choiceWindowStage.show();
	}
	
	/**
	 * Displays the interface for viewing songs.
	 *
	 * @param client The Socket for communication with the server.
	 * @param is The BufferedReader for reading data from the server.
	 * @param os The DataOutputStream for writing data to the server.
	 * @param selectedBand The name of the selected band.
	 * @throws IOException If an I/O error occurs.
	 */
	public static void showSongsInterface(Socket client, BufferedReader is, DataOutputStream os, String selectedBand) throws IOException
	{
		songsList.clear();
		
		//Telling the server to send the songs
		os.writeBytes("show_songs\n");  
		getSongsListFromServer(is, os, selectedBand); 
		
		ObservableList<Song> observableSongList = FXCollections.observableArrayList(songsList);
		
		Stage showStage = new Stage();
		showStage.setTitle("Songs viewer");
		
		// Buttons
		Button backButton = new Button("Back");
		Button closeButton = new Button("Close");
        backButton.getStyleClass().add("custom-button");
        closeButton.getStyleClass().add("custom-button");
		
		TableView<Song> tableView = createSongTable_songs(observableSongList);
        
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
        HBox buttonBox = new HBox(20);	 // Spacing of 10 pixels between the buttons
        buttonBox.getChildren().addAll(backButton, closeButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle("-fx-alignment: CENTER;"); 	// Center the buttons inside the HBox
        
        root.setBottom(buttonBox);
        
        //buy button handler:
        backButton.setOnAction(e -> {  
        	GuestMainGUI.showBandDataInterface(client, is, os, selectedBand); //Returns back to the previous choice interface
        	showStage.close();
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
        scene.getStylesheets().add(GuestMainGUI.class.getResource("texture.css").toExternalForm());
        
        showStage.setScene(scene);
        showStage.show();
	}
	
	/**
	 * Displays the interface for viewing events.
	 *
	 * @param client The Socket for communication with the server.
	 * @param is The BufferedReader for reading data from the server.
	 * @param os The DataOutputStream for writing data to the server.
	 * @param selectedBand The name of the selected band.
	 * @throws IOException If an I/O error occurs.
	 */
	public static void showEventsInterface(Socket client, BufferedReader is, DataOutputStream os, String selectedBand) throws IOException
	{
		eventList.clear();
		
		//Telling the server to send the events
		os.writeBytes("show_events\n");  
		getEventsListFromServer(is, os, selectedBand); 
		
		ObservableList<Event> observableEventList = FXCollections.observableArrayList(eventList);
		
		Stage showStage = new Stage();
		showStage.setTitle("Events viewer");
		
		// Buttons
		Button backButton = new Button("Back");
		Button closeButton = new Button("Close");
        backButton.getStyleClass().add("custom-button");
        closeButton.getStyleClass().add("custom-button");
		
		TableView<Event> tableView = createEventTable_events(observableEventList);
        
        Label titleLabel = new Label("Scheduled events:");
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
        HBox buttonBox = new HBox(10);	 // Spacing of 10 pixels between the buttons
        buttonBox.getChildren().addAll(backButton, closeButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle("-fx-alignment: CENTER;"); 	// Center the buttons inside the HBox
        
        root.setBottom(buttonBox);
        
        //buy button handler:
        backButton.setOnAction(e -> {  
        	GuestMainGUI.showBandDataInterface(client, is, os, selectedBand);   //Returns back to the previous choice interface
        	showStage.close();
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
        scene.getStylesheets().add(GuestMainGUI.class.getResource("texture.css").toExternalForm());
        
        showStage.setScene(scene);
        showStage.show();
	}

	/**
	 * Displays the interface for selecting bands and viewing their songs and events.
	 * This method is called only in guest mode.
	 *
	 * @param client The Socket for communication with the server.
	 * @param is The BufferedReader for reading data from the server.
	 * @param os The DataOutputStream for writing data to the server.
	 * @throws IOException If an I/O error occurs.
	 */
	public static void showBandsInterface(Socket client, BufferedReader is, DataOutputStream os) throws IOException
	{
		
		os.writeBytes("getbands\n");  //Telling the server to send the band names
		getNamesFromServer(is, os);
		
		ObservableList<String> observableNamesList = FXCollections.observableArrayList(bandNamesList);
		
		Stage showStage = new Stage();
		showStage.setTitle("guest mode");
		
		Button okButton = new Button("Ok");
		Button backButton = new Button("Back");
		
        okButton.getStyleClass().add("custom-button");
        backButton.getStyleClass().add("custom-button");
		
        ListView<String> listView = new ListView<>();
        listView.setItems(observableNamesList);
        
        Label titleLabel = new Label("Select one of this bands to see songs and events:");
        titleLabel.getStyleClass().add("custom-title");
        
		BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setCenter(listView);
        root.setTop(titleLabel);
        titleLabel.setPadding(new Insets(12, 0, 15, 0));
        
        root.getStyleClass().add("custom-box");
        
        // Creating an HBox for the buttons
        HBox buttonBox = new HBox(20);	 // Spacing of 10 pixels between the buttons
        buttonBox.getChildren().addAll(okButton, backButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER);
        
        root.setBottom(buttonBox);
        BorderPane.setAlignment(buttonBox, Pos.CENTER);
        
        //ok button handler:
        okButton.setOnAction(e -> {  
        	if (!listView.getSelectionModel().isEmpty()) //If true, a song in the list shown is selected and ready to be choosen
        	{  
        		try {
					String selectedBand = listView.getSelectionModel().getSelectedItem();
					os.writeBytes("show_guest\n"); //Sending the request to show all the songs of the specified artist to the server 
					os.writeBytes(selectedBand+"\n");
					
					GuestMainGUI.showBandDataInterface(client, is, os, selectedBand); //Opens the class method to choose what to display (songs or events)
					showStage.close();
					
				} catch (IOException e1)
        		{
					e1.printStackTrace();
				} 
        		
        	}else //No band selected, unable to confirm
        	{
        		Label titleLabel2 = new Label("Select a band!");
        		titleLabel2.getStyleClass().add("custom-title");
                titleLabel.setPadding(new Insets(12, 0, 15, 0));
        		root.setTop(titleLabel2);
        	}
        });
        
        //back button handler:
        backButton.setOnAction(e ->{ 
        	try {
        		
        		os.writeBytes("back\n");	//Telling the server to return back 
				Stage primaryStage = new Stage();
	        	ClientLoginGUI.loginDisplay(primaryStage);	//Return to main menu (mainDisplay function)
	        	showStage.close();
	        	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        });
        
        Scene scene = new Scene(root, 800, 400);
        
        //Importing css style file for custom layouts
        scene.getStylesheets().add(GuestMainGUI.class.getResource("texture.css").toExternalForm()); 

        showStage.setScene(scene);
        showStage.show();
	}

}
