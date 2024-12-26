package org.agabsk.statorganizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class onCourt {

    private Team team;
    private Map<Player, Map<String, Double>> players = new HashMap<>();
    private int totalPlayTime, currentTimeInSeconds;

    public onCourt(Team team){
        this.team = team;
        this.totalPlayTime = 0;
        this.currentTimeInSeconds = 0;
    }

    public Team getTeam(){
        return this.team;
    }

    public void addPlayer(Player player){
        Map<String, Double> onCourtStats = this.initOnCourtStats();
        this.players.put(player, onCourtStats);
        player.addOnCourt(this);
    }

    public Map<String, Double> initOnCourtStats(){
        Map<String, Double> onCourtStats = new HashMap<>();
        onCourtStats.put("2pt-Attempted", 0.00);
        onCourtStats.put("2pt-Made", 0.00);
        onCourtStats.put("2pt-Percentage", 0.00);
        onCourtStats.put("3pt-Attempted", 0.00);
        onCourtStats.put("3pt-Made", 0.00);
        onCourtStats.put("3pt-Percentage", 0.00);
        onCourtStats.put("FT-Attempted", 0.00);
        onCourtStats.put("FT-Made", 0.00);
        onCourtStats.put("FT-Percentage", 0.00);
        onCourtStats.put("Points", 0.00);
        onCourtStats.put("Assists", 0.00);
        onCourtStats.put("OffReb", 0.00);
        onCourtStats.put("DefReb", 0.00);
        onCourtStats.put("Steal", 0.00);
        onCourtStats.put("TO", 0.00);
        onCourtStats.put("Block", 0.00);
        onCourtStats.put("Fouls", 0.00);
        onCourtStats.put("Fouls-Drawn", 0.00);
        onCourtStats.put("RebTotal", 0.00);
        return onCourtStats;
    }

    private void updateStat(Player player, String statType, Double diff){
        Map<String, Double> onCourtStats = this.players.get(player);
        Double current = onCourtStats.get(statType);
        onCourtStats.put(statType, current + diff);
    }

    private void updatePercentage(Player player, String shotType){
        String attempString, madeString, percentageString;
        attempString = shotType.concat("-Attempted");
        madeString = shotType.concat("-Made");
        percentageString = shotType.concat("-Percentage");
        Double attempts = this.players.get(player).get(attempString);
        Double makes = this.players.get(player).get(madeString);
        Double percentage = attempts > 0 ? ((makes/attempts) * 100) : 0;
        this.players.get(player).put(percentageString, percentage);
    }

    public void twoPTAttempt(Player player, String success){
        if ("true".equals(success)){
            this.updateStat(player, "2pt-Made", 1.00);
            this.updateStat(player, "Points", 2.00);
        }
        this.updateStat(player, "2pt-Attempted", 1.00);
        this.updatePercentage(player, "2pt");
    }

    public boolean checkByPlayers(ArrayList<Player> currentCourt){
        for (Player player : currentCourt){
            if (!this.players.containsKey(player)){
                return false;
            }
        }
        return true;
    }

    public ArrayList<Player> getPlayers(){
        ArrayList<Player> playerList = new ArrayList<>();
        for (Player player : players.keySet()) {
            playerList.add(player);
        }
        return playerList;
    }

    public void setCurrentTimeInSeconds(int time){
        this.currentTimeInSeconds = time;
    }

    public int getCurrentTimeInSeconds(){
        return this.currentTimeInSeconds;
    }

    public void addDuration(int duration){
        this.totalPlayTime = this.totalPlayTime + duration;
    }

}
