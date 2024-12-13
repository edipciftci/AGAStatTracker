package org.agabsk.statorganizer;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Game {
    private final JsonArray[] Quarters = new JsonArray[8];
    private String homeTeam, awayTeam, gameName;
    private final ArrayList<Event> events = new ArrayList<>();
    private ArrayList<String> singularEvents = new ArrayList<>();

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

    public void setHomeTeam(String homeTeam){
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam){
        this.awayTeam = awayTeam;
    }

    public String getHomeTeam(){
        return this.homeTeam;
    }

    public String getAwayTeam(){
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
        this.events.add(newEvent);
        for (String singEv : this.singularEvents) {
            if (singEv.contentEquals(newEvent.getEventType())){
                return;
            }
        }
        this.singularEvents.add(newEvent.getEventType());
    }

    public ArrayList<String> getSingularEvents() {
        return this.singularEvents;
    }

    public void setGameName(){
        this.gameName = this.homeTeam.concat("_").concat(this.awayTeam).replace(" ", "");
    }

    public String getGameName(){
        return this.gameName;
    }
}
