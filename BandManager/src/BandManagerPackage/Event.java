/*
 * BandManager by Davide Reverberi (matr. 332781)
 * 
 * UniPr, Software Engineering course.
 * Last Modified: 1/03/2024
 * 
 */
package BandManagerPackage;

/**
* Represents a Event with a name, place, date, type and performer.
* The class provides constructors for creating instances of the event,
* as well as methods to retrieve information about the event such as its name,
* place, date, type and performer.
*/
public class Event {
	
	private String name = null;
	private String place = null;
	private String date = null;
	private String type = null;
	private String performer = null;

	public Event()
	{
		//Default constructor creating an invalid object.
	}

	/**
     * Constructs a event with the specified name, place, date, type and perfomer.
     *
     * @param n    	The name of the event.
     * @param plac  The place of the event.
     * @param date	The date of the event.
     * @param t		The type of the event.
     * @param p 	The performer of the event.
     */
	public Event(String n, String plac, String d, String t, String p)
	{
		this.name = n;
		this.place = plac;
		this.date = d;
		this.type = t;
		this.performer= p;
	}
	
	 /**
     * Gets the name of the event.
     *
     * @return The name of the event.
     */
	public String getName()
	{
		return this.name;
	}
	
	 /**
     * Gets the place of the event.
     *
     * @return The place of the event.
     */
	public String getPlace()
	{
		return this.place;
	}
	
	 /**
     * Gets the date of the event.
     *
     * @return The date of the event.
     */
	public String getDate()
	{
		return this.date;
	}
	
	 /**
     * Gets the type of the event.
     *
     * @return The type of the event.
     */
	public String getType()
	{
		return this.type;
	}
	
	 /**
     * Gets the performer of the event.
     *
     * @return The performer of the event.
     */
	public String getPerformer()
	{
		return this.performer;
	}
	
    /**
     * Returns a string representation of the event.
     * The string includes the name, place, date, type and the performer of the event.
     *
     * @return A string representation of the event.
     */
	@Override
    public String toString() {
        return name + "  " + place + "  " + date + " " + type + " " + performer;
    }
	
}


