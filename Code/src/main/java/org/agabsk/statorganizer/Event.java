package org.agabsk.statorganizer;

public class Event {
    private int playerNum, clockSeconds;
    private String clock, eventType, playerName, eventID;

    public Event(String ID){
        this.playerNum = -1;
        this.clock = null;
        this.eventType = null;
        this.playerName = null;
        this.eventID = ID;
    }

    public String getEventID(){
        return this.eventID;
    }

    public void setEventType(String eventType){
        this.eventType = eventType;
    }

    public String getEventType(){
        return this.eventType;
    }

    public void setPlayerNum(int playerNum){
        this.playerNum = playerNum;
    }

    public int getPlayerNum(){
        return this.playerNum;
    }

    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public void setClock(String clock){
        this.clock = clock;

        // Remove "PT" and split the remaining part at "M" and "S"
        String[] parts = clock.substring(2).split("[MS]");
        
        // Extract minutes and seconds
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        
        this.clockSeconds = (60 * minutes) + seconds;
        
    }

    public String getClock(){
        return this.clock;
    }

    public int getClockInSeconds(){
        return this.clockSeconds;
    }

    @Override
    public String toString(){
        String eventString = "";
        return eventString;
    }
}
