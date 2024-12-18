package org.agabsk.statorganizer;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private String playerName, playerID;
    private int playerNum;
    private Map<String, Integer> playerStats = new HashMap<>();
    private Map<String, Double> percentages = new HashMap<>();

    public Player(String ID) {
        this.playerID = ID;
        this.initializeStats();
    }

    private void initializeStats() {
        this.playerStats.put("2pt-Attempted", 0);
        this.playerStats.put("2pt-Made", 0);
        this.percentages.put("2pt-Percentage", 0.00);
        this.playerStats.put("3pt-Attempted", 0);
        this.playerStats.put("3pt-Made", 0);
        this.percentages.put("3pt-Percentage", 0.00);
        this.playerStats.put("FT-Attempted", 0);
        this.playerStats.put("FT-Made", 0);
        this.percentages.put("FT-Percentage", 0.00);
        this.playerStats.put("Points", 0);
        this.playerStats.put("Assists", 0);
        this.playerStats.put("OffReb", 0);
        this.playerStats.put("DefReb", 0);
        this.playerStats.put("Steal", 0);
        this.playerStats.put("TO", 0);
        this.playerStats.put("Block", 0);
        this.playerStats.put("Fouls", 0);
        this.playerStats.put("Fouls-Drawn", 0);
        this.playerStats.put("RebTotal", 0);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public int getPlayerNum() {
        return this.playerNum;
    }

    public void twoPTAttempt(String success) {
        if ("true".equals(success)) {
            this.playerStats.put("2pt-Made", this.playerStats.get("2pt-Made") + 1);
            this.playerStats.put("Points", this.playerStats.get("Points") + 2);
        }
        this.playerStats.put("2pt-Attempted", this.playerStats.get("2pt-Attempted") + 1);
        updatePercentage("2pt-Made", "2pt-Attempted", "2pt-Percentage");
    }

    public void threePTAttempt(String success) {
        if ("true".equals(success)) {
            this.playerStats.put("3pt-Made", this.playerStats.get("3pt-Made") + 1);
            this.playerStats.put("Points", this.playerStats.get("Points") + 3);
        }
        playerStats.put("3pt-Attempted", playerStats.get("3pt-Attempted") + 1);
        updatePercentage("3pt-Made", "3pt-Attempted", "3pt-Percentage");
    }

    public void FTAttempt(String success) {
        if ("true".equals(success)) {
            this.playerStats.put("FT-Made", this.playerStats.get("FT-Made") + 1);
            this.playerStats.put("Points", this.playerStats.get("Points") + 1);
        }
        this.playerStats.put("FT-Attempted", this.playerStats.get("FT-Attempted") + 1);
        updatePercentage("FT-Made", "FT-Attempted", "FT-Percentage");
    }

    private void updatePercentage(String madeKey, String attemptedKey, String percentageKey) {
        int made = this.playerStats.get(madeKey);
        int attempted = this.playerStats.get(attemptedKey);
        double percentage = attempted > 0 ? (double) ((made / (double) attempted) * 100) : 0;
        this.percentages.put(percentageKey, percentage);
    }

    private void rebound(String rebType){
        if ("offensive".equals(rebType)){
            this.playerStats.put("OffReb", this.playerStats.get("OffReb") + 1);
        } else {
            this.playerStats.put("DefReb", this.playerStats.get("DefReb") + 1);
        }
        this.playerStats.put("RebTotal", this.playerStats.get("RebTotal") + 1);
    }

    private void foul(String foulType){
        if ("drawn".equals(foulType)){
            this.playerStats.put("Fouls-Drawn", this.playerStats.get("Fouls-Drawn") + 1);
        } else{
            this.playerStats.put("Fouls", this.playerStats.get("Fouls") + 1);
        }
    }

    public void substitution(String subType){

    }

    public int getStat(String key) {
        return this.playerStats.getOrDefault(key, 0);
    }

    public void updateStat(Event event){
        switch (event.getEventType().split("-")[0]) {
            case "2pt" -> this.twoPTAttempt(event.getSuccess());
            case "3pt" -> this.threePTAttempt(event.getSuccess());
            case "freeThrow" -> this.FTAttempt(event.getSuccess());
            case "assist" -> this.playerStats.put("Assists", this.playerStats.get("Assists")+1);
            case "rebound" -> this.rebound(event.getEventType().split("-")[1]);
            case "steal" -> this.playerStats.put("Steal", this.playerStats.get("Steal")+1);
            case "turnover" -> this.playerStats.put("TO", this.playerStats.get("TO")+1);
            case "foul" -> this.foul(event.getEventType().split("-")[1]);
            case "block" -> this.playerStats.put("Block", this.playerStats.get("Block")+1);
            case "substitution" -> this.substitution(event.getEventType().split("-")[1]);
        
            default -> System.out.println(event.getEventType());
        }
    }

    public String getPlayerID(){
        return this.playerID;
    }
}
