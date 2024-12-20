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
    
    public Game(ArrayList<Player>[] players){
        this.homePlayers = players[0];
        this.awayPlayers = players[1];

        this.players.addAll(this.homePlayers);

    }

    public JsonArray[] getQuarters(){
        return this.Quarters;
    }

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

    public JsonArray getQtr(int qtrKey){
        if (qtrKey < 5){
            return this.Quarters[qtrKey-1];
        }
        return this.Quarters[qtrKey-7];
    }

    public void setHomeTeam(Team homeTeam){
        this.homeTeam = homeTeam;
        for (Player player : this.homePlayers) {
            if (this.homeTeam.getPlayerByID(player.getPlayerID()) == null){
                this.homeTeam.addPlayer(player);
                System.out.println("Player ".concat(player.getPlayerName()).concat(" is added to the ").concat(homeTeam.getTeamName()));
            }
        }
    }

    public void setAwayTeam(Team awayTeam){
        this.awayTeam = awayTeam;
        for (Player player : this.awayPlayers) {
            if (this.awayTeam.getPlayerByID(player.getPlayerID()) == null){
                this.awayTeam.addPlayer(player);
                System.out.println("Player ".concat(player.getPlayerName()).concat(" is added to the ").concat(awayTeam.getTeamName()));
            }
        }
    }

    public Team getHomeTeam(){
        return this.homeTeam;
    }

    public Team getAwayTeam(){
        return this.awayTeam;
    }

    public void newEvent(JsonObject event){
        if (event.get("bib") == null){
            return;
        }
        if (event.get("eventType").getAsString().matches("jumpBall")){
            return;
        }
        Event newEvent = new Event(event.get("eventId").getAsString());
        try {
            if (event.get("eventType").getAsString().matches("turnover|2pt|3pt|freeThrow")){
                throw new Exception();
            }
            newEvent.setEventType(event.get("eventType").getAsString().concat("-").concat(event.get("eventSubType").getAsString()));
        } catch (Exception e) {
            newEvent.setEventType(event.get("eventType").getAsString());
        }
        newEvent.setClock(event.get("clock").getAsString());
        newEvent.setPlayerNum(event.get("bib").getAsInt());
        newEvent.setPlayerName(event.get("name").getAsString());
        if (event.get("eventType").getAsString().matches("2pt|3pt|freeThrow")){
            newEvent.setSuccess(event.get("success").getAsString());
        }
        this.events.add(newEvent);

        for (Player player : this.players){
            if (player.getPlayerID().equals(event.get("personId").getAsString())){
                player.updateStat(newEvent);
                break;
            }
        }

    }

    public void setGameName(){
        this.gameName = this.homeTeam.getTeamName().concat("_").concat(this.awayTeam.getTeamName()).replace(" ", "");
    }

    public String getGameName(){
        return this.gameName;
    }

}
