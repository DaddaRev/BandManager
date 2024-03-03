/*
 * BandManager by Davide Reverberi (matr. 332781)
 * 
 * UniPr, Software Engineering course.
 * Last Modified: 1/03/2024
 * 
 */
package BandManagerPackage;

//Optimization idea: Builder architectural pattern to avoid the creation of different costructors.

/**
 * Represents a Song with a name, author, length, bpm and performer.
 * The class provides constructors for creating instances of the song,
 * as well as methods to retrieve information about the song such as its name,
 * author, length, bpm and performer.
 */
public class Song {
	
	private String name = null;
	private String author = null;
	private float length = 0;
	private int bpm = 0;
	private String performer = null;

	public Song()
	{
		//Default constructor creating an invalid object.
	}

	/**
     * Constructs a song with the specified name, author, length and perfomer.
     *
     * @param n    	The name of the song.
     * @param auth  The author of the song.
     * @param l		The length of the song.
     * @param p 	The performer of the song.
     */
	public Song(String n, String auth, float l, String p)  //Constructor if bpm is non specified
	{
		this.name = n;
		this.author = auth;
		this.length = l;
		this.performer= p;
	}
	
	/**
     * Constructs a song with the specified name, author, length, bpm and perfomer.
     *
     * @param n    	The name of the song.
     * @param auth  The price of the song.
     * @param l		The length of the song.
     * @param b		The bpm of the song.
     * @param p 	The performer of the song.
     */
	public Song(String n, String auth, float l, int b, String p) 
	{
		this.name = n;
		this.author = auth;
		this.length = l;
		this.bpm = b;
		this.performer = p;
	}
	
    /**
     * Gets the name of the song.
     *
     * @return The name of the song.
     */
	public String getName()
	{
		return this.name;
	}
	
    /**
     * Gets the author of the song.
     *
     * @return The author of the song.
     */
	public String getAuthor()
	{
		return this.author;
	}
	
    /**
     * Gets the length of the song.
     *
     * @return The length of the song.
     */
	public float getLength()
	{
		return this.length;
	}
	
    /**
     * Gets the bpm of the song.
     *
     * @return The bpm of the song.
     */
	public int getBpm()
	{
		return this.bpm;
	}
	
    /**
     * Gets the performer of the song.
     *
     * @return The performer of the song.
     */
	public String getPerformer()
	{
		return this.performer;
	}
	
    /**
     * Returns a string representation of the song.
     * The string includes the name, author, length, bpm (if exist) and the performer of the song.
     *
     * @return A string representation of the song.
     */
	@Override
    public String toString() {
        return name + "  " + author + "  " + length + " " + bpm + " " + performer;
    }
	
}

