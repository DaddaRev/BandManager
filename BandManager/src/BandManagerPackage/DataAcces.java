/*
 * BandManager by Davide Reverberi (matr. 332781)
 * 
 * UniPr, Software Engineering course.
 * Last Modified: 1/03/2024
 * 
 */
package BandManagerPackage;

/**
 * IMPORTANT NOTE:
 * 
 * The following class is currently structured to provide a DEMO of the program in order to speed 
 * up the user experience with some dummy starting data. For a realistic situation, 
 * it is recommended that blocks of code involving these additions be removed.
 * 
 */

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for Database operation.
 * Class responsible for the actual operations on the database and the communication of updated data with the server.
 * (Lower level in layered architecture, see documentation and user manual for deeper understanding)
 * 
 * @author Davide Reverberi
 */
public class DataAcces {
	
	static Connection connection = null;
	public static List<Song> songsList = new ArrayList<>(); 	//List of songs passed to the server Logic 
	public static List<Event> eventsList = new ArrayList<>();	//List og events passed to the server Logic
	public static List<String> performers = new ArrayList<>();	//List of performers available 
	
	/**
	 * Costructor used for making the the connection with the database.
	 * If database not exist, a new one is been created.
	 * If the connection goes bad, higher levels are informed.	
	 * 
	 */
    public DataAcces()
    {
    	String url = "jdbc:mysql://localhost:3306/bandmanagerdb?"; 	//Change this to change the referenced database
    	String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC"; 	//Feature used to create a new database if not exist
        String user = "root";
        String password = "";  

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Making the connection
            connection = DriverManager.getConnection(url + ARGS, user, password);
            Statement stmt = connection.createStatement();
            
            String create1 = "CREATE TABLE IF NOT EXISTS songs (Name CHAR(40) PRIMARY KEY, Author CHAR(40), Length FLOAT, BPM INT(3), Performer CHAR(40))";
            String create2 = "CREATE TABLE IF NOT EXISTS events (Name CHAR(40) PRIMARY KEY, Place CHAR(70), Date CHAR(15), Type CHAR(60), Performer CHAR(40))";
            
            stmt.executeUpdate(create1);
            stmt.executeUpdate(create2);
            
            // Songs to add inconditionally *****REMOVE IF NOT IN DEMO MODE*****
            String[] songsToAdd = {
            	    "('Confortably_Numb', 'Pink_Floyd', '6.19', '127', 'Whisky&Lo')",
            	    "('StrongerThanMe', 'AmyWinehouse', '3.34', '91', 'SkyGround')",
            	    "('Trouble', 'JoseJames', '3.29', '89', 'SkyGround')",
            	    "('Crazy', 'ScaryPockets', '2.44', '98', 'SkyGround')",
            	    "('BillieJean', 'MichealJackson', '4.54', '117', 'PollyValentine')",
            	    "('Golden_Hour', 'Yuji', '3.25', '82', 'PollyValentine')",
            	    "('As_It_Was', 'HarryStyles', '2.47', '174', 'PollyValentine')",
            	    "('Mortiz', 'RenouxSanchez', '3.45', '90', 'BassaNuova')",
            	    "('DomingoSol', 'JohnGood', '2.20', '80', 'BassaNuova')",
            	    "('Imaginating', 'BassaNuova', '5.20', '70', 'BassaNuova')",
            	    "('Stand_By_Me', 'Weezer', '3.01', '119', 'FreshCyl')",
            	};

            //*****REMOVE IF NOT IN DEMO MODE*****
            for (String songData : songsToAdd) {
            	String insertEvent = "INSERT IGNORE INTO songs (Name, Author, Length, BPM, Performer) VALUES " + songData;
            	stmt.executeUpdate(insertEvent);
            }
            
            // Events to add inconditionally *****REMOVE IF NOT IN DEMO MODE*****
            String[] eventsToAdd = {
            	    "('Manifesto_Rock', 'ViaFrancia_Milano', '1-07-2024', 'Concerto', 'BassaNuova')",
            	    "('MagretaInFestival', 'Magreta_Modena', '15-08-2024', 'FieraDiPaese', 'BassaNuova')",
            	    "('Matrimonio_Maf', 'Agriturismo_QuerciaRossa', '15-06-2024', 'Matrimonio', 'PollyValentine')",
            	    "('LouFestival', 'ViaCorazzata_Modena', '23-09-2024', 'Festival', 'BassaNuova')",
            	    "('ManzoMusic', 'ViaBrigate_Reggio', '21-10-2024', 'Concerto', 'FreshCyl')"
            	};

            //*****REMOVE IF NOT IN DEMO MODE*****
            for (String eventData : eventsToAdd) {
            	String insertEvent2 = "INSERT IGNORE INTO events (Name, Place, Date, Type, Performer) VALUES " + eventData;
            	stmt.executeUpdate(insertEvent2);
            }
            
            System.out.println("Connection with database: DONE");
            
        } catch (ClassNotFoundException | SQLException e) 
        {	
            System.out.println("Connection with database: FAILED");
            e.printStackTrace();
        }
    }
    
    /**
     * Removes a song from the database based on the provided Song object.
     * This method executes a SQL DELETE query to remove the song from the 'songs' table
     * where the name of the song matches the provided Song object's name.
     *
     * @param s The Song object representing the song to be removed.
     */
    public static void removeSongQuery(Song s)
    {
    	String remove_query = "DELETE FROM songs WHERE Name = ?";
    	
    	try {
    		PreparedStatement preparedStatement = connection.prepareStatement(remove_query);
    		preparedStatement.setString(1, s.getName());
    		
    		preparedStatement.executeUpdate(); //Removing the song from the songs table in the db
    		
    	}catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Removes an event from the database based on the provided Event object.
     * This method executes a SQL DELETE query to remove the event from the 'events' table
     * where the name of the event matches the provided Event object's name.
     *
     * @param s The Event object representing the event to be removed.
     */
    public static void removeEventQuery(Event s)
    {
    	String remove_query = "DELETE FROM events WHERE Name = ?";
    	
    	try {
    		PreparedStatement preparedStatement = connection.prepareStatement(remove_query);
    		preparedStatement.setString(1, s.getName());
    		
    		preparedStatement.executeUpdate(); //Removing the event from the events table in the db
    		
    	}catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Adds a new song to the database using the provided Song object.
     * This method executes a SQL INSERT query to add the song to the 'songs' table
     * with the specified attributes such as name, author, length, BPM, and performer.
     *
     * @param s The Song object representing the song to be added to the database.
     */
    public static void addSongQuery(Song s)
    {
    	String add_query = "INSERT INTO songs (Name, Author, Length, BPM, Performer) VALUES (?, ?, ?, ?, ?)";
    	
    	try {
    		PreparedStatement preparedStatement = connection.prepareStatement(add_query);
    		preparedStatement.setString(1, s.getName());
    		preparedStatement.setString(2, s.getAuthor());
    		preparedStatement.setFloat(3, s.getLength());
    		preparedStatement.setInt(4, s.getBpm());
    		preparedStatement.setString(5, s.getPerformer());
    		
    		preparedStatement.executeUpdate(); //Adding the song to the songs table in the DB
    		
    	}catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Adds a new event to the database using the provided Event object.
     * This method executes a SQL INSERT query to add the event to the 'events' table
     * with the specified attributes such as name, place, date, type, and performer.
     *
     * @param s The Event object representing the event to be added to the database.
     */
    public static void addEventQuery(Event s)
    {
    	String add_query = "INSERT INTO events (Name, Place, Date, Type, Performer) VALUES (?, ?, ?, ?, ?)";
    	
    	try {
    		PreparedStatement preparedStatement = connection.prepareStatement(add_query);
    		preparedStatement.setString(1, s.getName());
    		preparedStatement.setString(2, s.getPlace());
    		preparedStatement.setString(3, s.getDate());
    		preparedStatement.setString(4, s.getType());
    		preparedStatement.setString(5, s.getPerformer());
    		
    		preparedStatement.executeUpdate(); //Adding the song to the songs table in the DB
    		
    	}catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Retrieves a list of songs from the database for a specific performer.
     * This method executes a SQL SELECT query to fetch songs from the 'songs' table
     * where the performer matches the provided performer name.
     *
     * @param performer The name of the performer whose songs are to be retrieved.
     * @return A list of Song objects representing the songs performed by the specified performer, null if an SQL exception occurs.
     */
    public static List<Song> getSongsQuery(String performer)
    {
    	songsList.clear();
		try {
			
	        Statement statement = connection.createStatement();
	        ResultSet result = statement.executeQuery("SELECT * FROM songs WHERE performer = '"+ performer +"'");
	        
            while (result.next()) {
                String songName = result.getString("Name");
                String songAuthor = result.getString("Author");
                Float songLength = Float.parseFloat(result.getString("Length"));
                Integer songBpm = Integer.parseInt(result.getString("BPM"));

                //Adding the song in the local list.
                Song song = new Song(songName, songAuthor, songLength, songBpm, performer); //Adding the songs in the local list.
                songsList.add(song);
            }
            statement.close();
            result.close();
            
            return songsList; //returning the songs list to server
            
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * Retrieves a list of events from the database for a specific performer.
     * This method executes a SQL SELECT query to fetch events from the 'events' table
     * where the performer matches the provided performer name.
     *
     * @param performer The name of the performer whose events are to be retrieved.
     * @return A list of Song objects representing the events performed by the specified performer, null if an SQL exception occurs.
     */
    public static List<Event> getEventsQuery(String performer)
    {
    	eventsList.clear();
		try {
			
	        Statement statement = connection.createStatement();
	        ResultSet result = statement.executeQuery("SELECT * FROM events WHERE performer = '"+ performer +"'");
	        
            while (result.next()) {
                String eventName = result.getString("Name");
                String eventPlace = result.getString("Place");
                String eventDate = result.getString("Date");
                String eventType = result.getString("Type");

                //Adding the event in the local list.
                Event event = new Event(eventName, eventPlace, eventDate, eventType, performer);
                eventsList.add(event);
            }
            statement.close();
            result.close();
            
            return eventsList; //returning the songs list to server
            
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * Retrieves a list of performers from the database.
     * This method executes a SQL SELECT query to fetch distinct performer names
     * from the 'songs' table.
     *
     * @return A list of strings representing the names of performers, returns null if an SQL exception occurs.
     */
    public static List<String> getPerformers()
    {
    	performers.clear();
		try {
			
	        Statement statement = connection.createStatement();
	        ResultSet result = statement.executeQuery("SELECT DISTINCT performer FROM songs");
	        
	        while (result.next()) {
                String performerName = result.getString("performer");
                
                //Adding the performer (band) in the local list.
                performers.add(performerName);
            }
            statement.close();
            result.close();
	        
	        return performers;

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
    }
}
