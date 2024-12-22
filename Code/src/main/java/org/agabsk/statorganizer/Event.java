package org.agabsk.statorganizer;

public class Event {
    private int playerNum, clockSeconds;
    private String clock, eventType, playerName, success;
    private final String eventID;

    /**
     * Constructor to initialize event with ID.
     * @param ID the unique identifier for the event
     */
    public Event(String ID){
        this.playerNum = -1;
        this.clock = null;
        this.eventType = null;
        this.playerName = null;
        this.eventID = ID;
    }

    /**
     * Get the event ID.
     * @return the event ID
     */
    public String getEventID(){
        return this.eventID;
    }

    /**
     * Set the event type.
     * @param eventType the event type to set
     */
    public void setEventType(String eventType){
        this.eventType = eventType;
    }

    /**
     * Get the event type.
     * @return the event type
     */
    public String getEventType(){
        return this.eventType;
    }

    /**
     * Set the player number.
     * @param playerNum the player number to set
     */
    public void setPlayerNum(int playerNum){
        this.playerNum = playerNum;
    }

    /**
     * Get the player number.
     * @return the player number
     */
    public int getPlayerNum(){
        return this.playerNum;
    }

    /**
     * Set the player name.
     * @param playerName the player name to set
     */
    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }

    /**
     * Get the player name.
     * @return the player name
     */
    public String getPlayerName(){
        return this.playerName;
    }

    /**
     * Set the clock time.
     * @param clock the clock time to set
     */
    public void setClock(String clock){
        this.clock = clock;

        String[] parts = clock.substring(2).split("[MS]");

        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        
        this.clockSeconds = (60 * minutes) + seconds;
        
    }

    /**
     * Get the clock time.
     * @return the clock time
     */
    public String getClock(){
        return this.clock;
    }

    /**
     * Get the clock time in seconds.
     * @return the clock time in seconds
     */
    public int getClockInSeconds(){
        return this.clockSeconds;
    }

    /**
     * Set the success status.
     * @param success the success status to set
     */
    public void setSuccess(String success){
        this.success = success;
    }

    /**
     * Get the success status.
     * @return the success status
     */
    public String getSuccess(){
        return this.success;
    }

}
