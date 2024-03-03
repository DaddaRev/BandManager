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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for Server operation.
 * Class responsible for communicating data with the client and communicating with the 
 * DataAccess class related to updating the database.
 * (Intermediate level in layered architecture, see documentation and user manual for deeper understanding)
 * 
 * @author Davide Reverberi
 */
public class ServerLogic {

	private static final int SPORT = 4444; 	//Port for the socket connection.
	private static final String FILENAME = "./src/BandManagerPackage/accounts.txt";  //Path of the input file.
	
    public final Map<String, String> profileMap = new HashMap<>();	//Map of the accounts
    
	public List<Song> songsList_toSend = new ArrayList<>();  		//Local list of the available songs.
	public List<Event> eventsList_toSend = new ArrayList<>();		//Local list of the available events.
    public List<String> bandsList = new ArrayList<>();				//Local list of the available bands.
	
    /**
     * Constructor for the Server class.
     * Reads a file to retrieve credentials and possible accounts and put them in the local profileMap, 
     * used in the authentication process. 
     * The file format should be: "username password".
     * Each line in the file represents a valid authenthication account.
     * If the file has a line containing only the number -1, the reading process stops.
     * 
     * @throws IOException If an error occurs while reading the file.
     */
	public ServerLogic() 
	{
		try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    String username = parts[0];
                    String password = parts[1];
                    profileMap.put(username, password);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		DataAcces database = new DataAcces(); //Creating the connection with the database in the specific class
	}
	
	/**
	 * Function to check if a song is in the songsList_toSend.
	 * 
	 * @param name String name of the song to check
	 * @return true if song is found, false if not
	 */
	public boolean isInSongsList(String name)
	{
		for(Song p : this.songsList_toSend)
		{
			if(p.getName().equals(name))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Function to check if an event is in the eventsList_toSend.
	 * 
	 * @param name String name of the event to check
	 * @return true if event is found, false if not
	 */
	public boolean isInEventsList(String name)
	{
		for(Event p : this.eventsList_toSend)
		{
			if(p.getName().equals(name))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Function to remove the song requested by the user from the songs list (songsList_toSend).
	 * 
	 * @param os DataOutputStream object used to send each song contained in the songsList_toSend to the client.
	 * @param is BufferedReader object used to receive each song contained in the songsList from the client.
	 * @throws IOException If an error occurs while using the output stream.
	 */
	public void song_removeFromList(DataOutputStream os, BufferedReader is) throws IOException
	{
		String song_name = is.readLine(); //Server is waiting for the name of the song to remove
		boolean removed = false;
				
		for(Song p : this.songsList_toSend)
		{
			if(p.getName().equals(song_name))
			{
				DataAcces.removeSongQuery(p);
				removed = true;
				break;
			}
		}
		
		if (removed)
		{
			os.writeBytes("ok\n");
		}else {
			os.writeBytes("notok\n"); 	// removed = false --> can't remove the song from the setlist
		}
	}
	
	/**
	 * Function to remove the event requested by the user from the events list (eventsList_toSend).
	 * 
	 * @param os DataOutputStream object used to send each event contained in the eventsList_toSend to the client.
	 * @param is BufferedReader object used to receive each event contained in the eventsList from the client.
	 * @throws IOException If an error occurs while using the output stream.
	 */
	public void event_removeFromList(DataOutputStream os, BufferedReader is) throws IOException
	{
		String event_name = is.readLine(); //Server is waiting for the name of the event to remove
		boolean removed = false;
				
		for(Event p : this.eventsList_toSend)
		{
			if(p.getName().equals(event_name))
			{
				DataAcces.removeEventQuery(p);
				removed = true;
				break;
			}
		}
		
		if (removed)
		{
			os.writeBytes("ok\n");
		}else {
			os.writeBytes("notok\n"); 	// removed = false --> can't remove the event from events list
		}
	}
	
	/**
	 * Function to add a song to the Songs list. 
	 * This method is called by a client to add a new song to the list.
	 *
	 * @param os DataOutputStream for sending the response back to the client.
	 * @param is BufferedReader for reading data sent by the client.
	 * @throws IOException If an error occurs during the reading or sending of data through the streams.
	 */
	public void song_addToList(DataOutputStream os, BufferedReader is) throws IOException		
	{					
		String temp_song = is.readLine(); // Reading the song sent by the client
		
		String[] parts = temp_song.split(" "); 
		
		String songName = null;
		String songAuthor = null;
		String songPerformer = null;
		Float songLength = null;
		Integer songBpm = 0;

		// Parsing data
		songName = parts[0];
        songAuthor = parts[1];
        songLength = Float.parseFloat(parts[2]);
        songBpm = Integer.parseInt(parts[3]);
        songPerformer = parts[4];

        if(isInSongsList(songName))
        {
        	os.writeBytes("alreadyin\n"); 	//Song already exist, can't perform the adding
        }else {
        	Song temp_Song2 = new Song(songName, songAuthor, songLength, songBpm, songPerformer);
        	
        	DataAcces.addSongQuery(temp_Song2); 	//Using the DataAccess method to update the database with the new song
        	
	        os.writeBytes("ok\n");
		}
	}
	
	/**
	 * Function to add an event to the Events list. 
	 * This method is called by a client to add a new event to the list.
	 *
	 * @param os DataOutputStream for sending the response back to the client.
	 * @param is BufferedReader for reading data sent by the client.
	 * @throws IOException If an error occurs during the reading or sending of data through the streams.
	 */
	public void event_addToList(DataOutputStream os, BufferedReader is) throws IOException		
	{					
		String temp_event = is.readLine();
		
		String[] parts = temp_event.split(" "); 
		
		String eventName = null;
		String eventPlace = null;
		String eventDate = null;
		String eventType = null;
		String eventPerformer= null;

		eventName = parts[0];
        eventPlace = parts[1];
        eventDate = parts[2];
        eventType = parts[3];
        eventPerformer = parts[4];

        if(isInEventsList(eventName))
        {
        	os.writeBytes("alreadyin\n"); 	//Event already exist, can't perform the adding
        }else {
        	Event temp_Event2 = new Event(eventName, eventPlace, eventDate, eventType, eventPerformer);
        	
        	DataAcces.addEventQuery(temp_Event2); 	//Using the DataAccess method to update the database with the new song
        	
	        os.writeBytes("ok\n");
		}
	}

	/**
	 * Function to send every song or event about a specific communicated band name in the respectively local
	 * songsList_toSend or eventsList_toSend in the Client buffer, depending on the request.
	 * Each song or product is sent as a string text to the client.
	 * 
	 * @param os DataOutputStream object used to send each product contained in the Productslist to the client.
	 * @param is BufferedReader for reading data sent by the client.
	 * @param mode Int specifies if songs have been requested (mode=1) or events have been requested (mode=0)
	 * @throws IOException If an error occurs while using the output stream.
	 */
	public void sendDataToClient(DataOutputStream os, BufferedReader is, int mode) throws IOException
	{
		songsList_toSend.clear();
		eventsList_toSend.clear();
		String bandName = is.readLine();
		
		if (mode == 1) // mode 1 means that songs have been requested from the client
		{	
			songsList_toSend = DataAcces.getSongsQuery(bandName);	
			
			for(Song song  : songsList_toSend)
			{
				os.writeBytes(song.getName()+" "+song.getAuthor()+" "+song.getLength()+" "+song.getBpm()+"\n"); 	//Performer is useless, it's bandName
			}
			os.writeBytes("endsongs\n"); 	//end of iteration
			
		}else  		   // mode 0 means that events have been requested from the client
		{
			eventsList_toSend = DataAcces.getEventsQuery(bandName);	
			
			for(Event event  : eventsList_toSend)
			{
				os.writeBytes(event.getName()+" "+event.getPlace()+" "+event.getDate()+" "+event.getType()+"\n"); 	//Performer is useless, it's bandName
			}
			os.writeBytes("endevents\n");	//end of iteration
		}
	}
	
	/**
	 * Function to send all band names available in the database.
	 * 
	 * @param os DataOutputStream object used to send each product contained in the Productslist to the client.
	 * @throws IOException If an error occurs while using the output stream.
	 */
	public void sendNamesToClient(DataOutputStream os) throws IOException
	{
		bandsList.clear();
		bandsList = DataAcces.getPerformers();  //Getting the performers from the database
		
		for (String name : this.bandsList) 
		{
			os.writeBytes(name+"\n");
		}
		os.writeBytes("end\n");
	}
	
	/**
	 * Waits for client connections on a specified server socket and provides services based on client requests.
	 * The method handles authentication, allowing only authenticated users to use the service. Once authenticated,
	 * the method processes client commands, such as displaying the list of available songs or events, sending requested
	 * songs or events to the client, and adding new songs or events to the server's local lists.
	 *
	 * @throws IOException If an error occurs during socket operations or input/output streams.
	 */
	public void service()
	{
		try
		{
			ServerSocket server = new ServerSocket(SPORT);

			boolean authentication = false;
			String client_user = "";
			String password_client = "";
			String command = "";
			
			while(true)  
			{
				System.out.println("Waiting for a connection\n");
				Socket client = server.accept();
				System.out.println("Connected");
				authentication = false;

				BufferedReader   is = new BufferedReader(new InputStreamReader(client.getInputStream()));
				DataOutputStream os = new DataOutputStream(client.getOutputStream());
				
				String guest_mode = is.readLine();  //Reading if user is in guest mode or not
				
				if(guest_mode.equals("noguest"))  //If user is not in guest mode, check if he's entered right credentials.
				{
					client_user = is.readLine();
					
					//Checking if user has entered the right credentials
					for (java.util.Map.Entry<String, String> e: profileMap.entrySet())
					{
						if(client_user.equals(e.getKey()))
						{
							password_client = is.readLine();
							
							if(password_client.equals(e.getValue())) {
								authentication = true;
								os.writeBytes("ok\n");
								break;
							}
						}
					}
					
				}else // guest mode
				{
					authentication = true;  //If in guest mode, user can enter the service. Limitations are handled in the client side
				}
			
				if(authentication) //if authentication is propertly done or guest mode is active, service is ready to be used
				{
					boolean closed_client = false;
					while(!closed_client)
					{
						command = is.readLine(); //Input from the client
						
						switch (command) {
						case "getbands":
						{
							//Function to send the band names to the client
							this.sendNamesToClient(os);
							break;
						}
						case "back":
						{	
							//Return back to the main login interface
							closed_client = true; 
							client.close();
							break;
						}
						case "show_songs":
						{
							//Show songs of the specified band by the client (guest mode)
							this.sendDataToClient(os, is, 1);
							break;
						}
						case "show_events":
						{
							//Show events of the specified band by the client (guest mode)
							this.sendDataToClient(os, is, 0);
							break;
						}
						case "add_song":
						{
							//Add the specified song to the local list of songs
							this.song_addToList(os, is);
							break;
						}
						case "add_event":
						{
							//Add the specified event to the local list of events
							this.event_addToList(os,is);
							break;
						}
						case "remove_song":
						{
							//Remove the specified song to the local list of songs
							this.song_removeFromList(os,is);
							break;
						}
						case "remove_event":
						{
							//Remove the specified event to the local list of events
							this.event_removeFromList(os,is);
							break;
						}
						case "close":
						{
							//Close the client connection
							closed_client = true;
							client.close();
							break;
						}
						case "quit":
						{
							//Close the service (and the connected client)
							client.close();
							server.close();
							System.out.println("\nSERVICE CLOSED");
							return;
						}
						default:
							//The invalid command is handled by the client
							break;
						}
					}
					
				}else 
				{
					os.writeBytes("notok\n");
				}
			}			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * The main entry point for starting the server application and the BandManager application.
	 * Instantiates a Server object and calls the service method to handle client connections
	 * and provide server logic functionalities.
	 *
	 * @param args Command-line arguments (not used in this application).
	 */
	public static void main(final String[] args)
	{
		ServerLogic server = new ServerLogic();
		server.service();
	}
}
