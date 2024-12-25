package org.agabsk.statorganizer;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Game {
    private final JsonArray[] Quarters = new JsonArray[8];
    private String gameName;
    private final ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Player> homePlayers = new ArrayList<>();
    private ArrayList<Player> awayPlayers = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private Team homeTeam, awayTeam;
    
    /**
     * Constructor to initialize game with players.
     * @param players an array of player lists for home and away teams
     */
    public Game(ArrayList<Player>[] players){
        this.homePlayers = players[0];
        this.awayPlayers = players[1];

        this.players.addAll(this.homePlayers);
        this.players.addAll(this.awayPlayers);
    }

    /**
     * Get the quarters of the game.
     * @return an array of JsonArray representing the quarters
     */
    public JsonArray[] getQuarters(){
        return this.Quarters;
    }

    /**
     * Set the events for a specific quarter.
     * @param events the events to set
     * @param qtrKey the key for the quarter
     */
    public void setQtr(JsonArray events, int qtrKey){
        if (qtrKey < 5){
            this.Quarters[qtrKey-1] = events;
        } else{
            this.Quarters[qtrKey-7] = events;
        }
        for (int i = 0; i < events.size(); i++) {
            this.newEvent((JsonObject) events.get(i));
        }
    }

    /**
     * Get the events for a specific quarter.
     * @param qtrKey the key for the quarter
     * @return the events for the quarter
     */
    public JsonArray getQtr(int qtrKey){
        if (qtrKey < 5){
            return this.Quarters[qtrKey-1];
        }
        return this.Quarters[qtrKey-7];
    }

    /**
     * Set the home team for the game.
     * @param homeTeam the home team to set
     */
    public void setHomeTeam(Team homeTeam){
        this.homeTeam = homeTeam;
        for (Player player : this.homePlayers) {
            if (this.homeTeam.getPlayerByID(player.getPlayerID()) == null){
                this.homeTeam.addPlayer(player);
            }
            player.addGame(this);
        }
    }

    /**
     * Set the away team for the game.
     * @param awayTeam the away team to set
     */
    public void setAwayTeam(Team awayTeam){
        this.awayTeam = awayTeam;
        for (Player player : this.awayPlayers) {
            if (this.awayTeam.getPlayerByID(player.getPlayerID()) == null){
                this.awayTeam.addPlayer(player);
            }
            player.addGame(this);
        }
    }

    /**
     * Get the home team of the game.
     * @return the home team
     */
    public Team getHomeTeam(){
        return this.homeTeam;
    }

    /**
     * Get the away team of the game.
     * @return the away team
     */
    public Team getAwayTeam(){
        return this.awayTeam;
    }

    /**
     * Add a new event to the game.
     * @param event the event to add
     */
    public void newEvent(JsonObject event){
        // Check if the event has a player number (bib)
        if (event.get("bib") == null){
            return;
        }
        // Ignore jump ball events
        if (event.get("eventType").getAsString().matches("jumpBall")){
            return;
        }
        Event newEvent;
        try {
            // Handle specific event types with subtypes
            if (event.get("eventType").getAsString().matches("turnover|2pt|3pt|freeThrow")){
                throw new Exception();
            }
            newEvent = new Event(event.get("eventId").getAsString());
            newEvent.setEventType(event.get("eventType").getAsString().concat("-").concat(event.get("eventSubType").getAsString()));
        } catch (Exception e) {
            // Handle other event types
            newEvent = new Event(event.get("eventId").getAsString());
            newEvent.setEventType(event.get("eventType").getAsString());
        }
        // Set event details
        newEvent.setClock(event.get("clock").getAsString());
        newEvent.setPlayerNum(event.get("bib").getAsInt());
        newEvent.setPlayerName(event.get("name").getAsString());
        if (event.get("eventType").getAsString().matches("2pt|3pt|freeThrow")){
            newEvent.setSuccess(event.get("success").getAsString());
        }
        this.events.add(newEvent);

        // Update player stats based on the event
        for (Player player : this.players){
            if (player.getPlayerID().equals(event.get("personId").getAsString())){
                player.updateStat(newEvent);
                break;
            }
        }

    }

    /**
     * Set the name of the game.
     */
    public void setGameName(){
        this.gameName = this.homeTeam.getTeamName().concat("_").concat(this.awayTeam.getTeamName()).replace(" ", "");
    }

    /**
     * Get the name of the game.
     * @return the name of the game
     */
    public String getGameName(){
        return this.gameName;
    }

}
