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

    public void setTeam(Team team){
        this.team = team;
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
        onCourtStats.put("+/-", 0.00);
        return onCourtStats;
    }

    public void updatePlusMinus(int diff){
        for (Player player : this.getPlayers()) {
            player.updatePlusMinus(diff);
            this.updateStat(player, "+/-", Double.valueOf(diff));
        }
    }

    public void updateStat(Player player, String statType, Double diff){
        Map<String, Double> onCourtStats = this.players.get(player);
        if (onCourtStats == null){
            System.out.println("Here");
            return;
        }
        Double current = onCourtStats.get(statType);
        onCourtStats.put(statType, current + diff);
    }

    public void updatePercentage(Player player, String shotType){
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
            this.updatePlusMinus(2);
            if (this.team == null){
                System.out.println("Here");
            }
            this.team.getCurrentOpponent().getCurrentOnCourt().updatePlusMinus(-2);
        }
        this.updateStat(player, "2pt-Attempted", 1.00);
        this.updatePercentage(player, "2pt");
    }

    public void threePTAttempt(Player player, String success){
        if ("true".equals(success)){
            this.updateStat(player, "3pt-Made", 1.00);
            this.updateStat(player, "Points", 3.00);
            this.updatePlusMinus(3);
            if (this.team == null){
                System.out.println("Here");
            }
            this.team.getCurrentOpponent().getCurrentOnCourt().updatePlusMinus(-3);
        }
        this.updateStat(player, "3pt-Attempted", 1.00);
        this.updatePercentage(player, "3pt");
    }

    public void FTAttempt(Player player, String success){
        if ("true".equals(success)){
            this.updateStat(player, "FT-Made", 1.00);
            this.updateStat(player, "Points", 1.00);
            this.updatePlusMinus(1);
            if (this.team == null){
                System.out.println("Here");
            }
            this.team.getCurrentOpponent().getCurrentOnCourt().updatePlusMinus(-1);
        }
        this.updateStat(player, "FT-Attempted", 1.00);
        this.updatePercentage(player, "FT");
    }

    public void rebound(Player player, String rebType){
        if ("offensive".equals(rebType)){
            this.updateStat(player, "OffReb", 1.00);
        } else {
            this.updateStat(player, "DefReb", 1.00);
        }
        this.updateStat(player, "RebTotal", 1.00);
    }

    public void foul(Player player, String foulType){
        if ("drawn".equals(foulType)){
            this.updateStat(player, "Fouls-Drawn", 1.00);
        } else{
            this.updateStat(player, "Fouls", 1.00);
        }
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

    public Map<String, Double> getTotalStats(){
        Double statVal, current;
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
        for (Map<String, Double> playerStats: players.values()){
            for (String statType : onCourtStats.keySet()) {
                if (statType.contains("Percentage")){
                    continue;
                }
                statVal = playerStats.get(statType);
                current = onCourtStats.get(statType);
                onCourtStats.put(statType, statVal+current);
            }
        }
        for (String statType : onCourtStats.keySet()) {
            if (statType.contains("Percentage")){
                String shotType = statType.substring(0, statType.length()-11);
                String attempString, madeString, percentageString;
                attempString = shotType.concat("-Attempted");
                madeString = shotType.concat("-Made");
                percentageString = shotType.concat("-Percentage");
                Double attempts = onCourtStats.get(attempString);
                Double makes = onCourtStats.get(madeString);
                Double percentage = attempts > 0 ? ((makes/attempts) * 100) : 0;
                onCourtStats.put(percentageString, percentage);
            }
        }
        return onCourtStats;
    }

    public int getTotalPlayTime(){
        return this.totalPlayTime;
    }

    public Map<Player, Map<String, Double>> getPlayersStruct(){
        return this.players;
    }
}
