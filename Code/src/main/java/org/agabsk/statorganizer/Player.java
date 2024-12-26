package org.agabsk.statorganizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private String playerName;
    private final String playerID;
    private int playerNum;
    private final Map<String, Integer> playerStats = new HashMap<>();
    private final Map<String, Double> percentages = new HashMap<>();
    private int totalPlayTime, currentPlayTimeInSeconds;
    private Team team;
    private ArrayList<Game> games = new ArrayList<>();
    private ArrayList<onCourt> onCourts = new ArrayList<>();

    /**
     * Constructor to initialize player with ID and stats.
     * @param ID the unique identifier for the player
     */
    public Player(String ID) {
        this.playerID = ID;
        this.initializeStats();
        this.totalPlayTime = 0;
        this.currentPlayTimeInSeconds = 0;
    }

    /**
     * Set the team for the player.
     * @param team the team to set
     */
    public void setTeam(Team team){
        this.team = team;
    }

    /**
     * Get the team of the player.
     * @return the team of the player
     */
    public Team getTeam(){
        return this.team;
    }

    /**
     * Initialize player statistics.
     */
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

    /**
     * Set the player's name.
     * @param playerName the name to set
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Get the player's name.
     * @return the player's name
     */
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Set the player's number.
     * @param playerNum the number to set
     */
    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    /**
     * Get the player's number.
     * @return the player's number
     */
    public int getPlayerNum() {
        return this.playerNum;
    }

    /**
     * Update stats for a 2-point attempt.
     * @param success indicates if the attempt was successful ("true" or "false")
     */
    public void twoPTAttempt(String success) {
        if ("true".equals(success)) {
            this.playerStats.put("2pt-Made", this.playerStats.get("2pt-Made") + 1);
            this.playerStats.put("Points", this.playerStats.get("Points") + 2);
        }
        this.playerStats.put("2pt-Attempted", this.playerStats.get("2pt-Attempted") + 1);
        updatePercentage("2pt-Made", "2pt-Attempted", "2pt-Percentage");
    }

    /**
     * Update stats for a 3-point attempt.
     * @param success indicates if the attempt was successful ("true" or "false")
     */
    public void threePTAttempt(String success) {
        if ("true".equals(success)) {
            this.playerStats.put("3pt-Made", this.playerStats.get("3pt-Made") + 1);
            this.playerStats.put("Points", this.playerStats.get("Points") + 3);
        }
        playerStats.put("3pt-Attempted", playerStats.get("3pt-Attempted") + 1);
        updatePercentage("3pt-Made", "3pt-Attempted", "3pt-Percentage");
    }

    /**
     * Update stats for a free throw attempt.
     * @param success indicates if the attempt was successful ("true" or "false")
     */
    public void FTAttempt(String success) {
        if ("true".equals(success)) {
            this.playerStats.put("FT-Made", this.playerStats.get("FT-Made") + 1);
            this.playerStats.put("Points", this.playerStats.get("Points") + 1);
        }
        this.playerStats.put("FT-Attempted", this.playerStats.get("FT-Attempted") + 1);
        updatePercentage("FT-Made", "FT-Attempted", "FT-Percentage");
    }

    /**
     * Update shooting percentage.
     * @param madeKey the key for made shots
     * @param attemptedKey the key for attempted shots
     * @param percentageKey the key for the percentage
     */
    private void updatePercentage(String madeKey, String attemptedKey, String percentageKey) {
        int made = this.playerStats.get(madeKey);
        int attempted = this.playerStats.get(attemptedKey);
        double percentage = attempted > 0 ? (double) ((made / (double) attempted) * 100) : 0;
        this.percentages.put(percentageKey, percentage);
    }

    /**
     * Update stats for a rebound.
     * @param rebType the type of rebound ("offensive" or "defensive")
     */
    private void rebound(String rebType){
        if ("offensive".equals(rebType)){
            this.playerStats.put("OffReb", this.playerStats.get("OffReb") + 1);
        } else {
            this.playerStats.put("DefReb", this.playerStats.get("DefReb") + 1);
        }
        this.playerStats.put("RebTotal", this.playerStats.get("RebTotal") + 1);
    }

    /**
     * Update stats for a foul.
     * @param foulType the type of foul ("drawn" or other)
     */
    private void foul(String foulType){
        if ("drawn".equals(foulType)){
            this.playerStats.put("Fouls-Drawn", this.playerStats.get("Fouls-Drawn") + 1);
        } else{
            this.playerStats.put("Fouls", this.playerStats.get("Fouls") + 1);
        }
    }

    /**
     * Placeholder for substitution logic.
     * @param subType the type of substitution
     */
    public void substitution(String subType, int subTime){
        if (subType.equals("out")){
            this.team.subOut(this, subTime);
            int duration = subTime - this.currentPlayTimeInSeconds;
            this.totalPlayTime = this.totalPlayTime + duration;
        } else if(subType.equals("in")){
            this.team.subIn(this, subTime);
            this.currentPlayTimeInSeconds = subTime;
        }
    }

    /**
     * Get a specific stat by key.
     * @param key the key for the stat
     * @return the value of the stat
     */
    public int getStat(String key) {
        return this.playerStats.getOrDefault(key, 0);
    }

    /**
     * Get average of a specific stat by key.
     * @param key the key for the stat
     * @return the average value of the stat
     */
    public double getAverageStat(String key) {
        int divider = this.games.size();
        if (this.games.isEmpty()){
            divider = 1;
        }

        return (double) (this.playerStats.get(key) / (double)divider);
    }

    /**
     * Update player stats based on an event.
     * @param event the event to update stats from
     */
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
            case "substitution" -> this.substitution(event.getEventType().split("-")[1], event.getClockInSeconds());
        
            default -> System.out.println(event.getEventType());
        }
    }

    /**
     * Get the player's ID.
     * @return the player's ID
     */
    public String getPlayerID(){
        return this.playerID;
    }

    /**
     * Add a game to the player's game list.
     * @param game the game to add
     */
    public void addGame(Game game){
        this.games.add(game);
    }

    /**
     * Get the list of games the player has played.
     * @return the list of games
     */
    public ArrayList<Game> getGames(){
        return this.games;
    }

    /**
     * Get a specific game against an opponent team.
     * @param Opp the opponent team
     * @return the game against the opponent team, or null if not found
     */
    public Game getGame(Team Opp){
        for (Game game : this.games) {
            if ((Opp == game.getAwayTeam()) || (Opp == game.getHomeTeam())){
                return game;
            }
        }
        System.out.println("Player hasn't played a game against ".concat(Opp.getTeamName()));
        return null;
    }

    /**
     * Calculate and return average stats.
     * @return a map of average stats
     */
    public Map<String, Double> getAverages(){
        int divider = this.games.size();
        if (this.games.isEmpty()){
            divider = 1;
        }
        Map<String, Double> averages = new HashMap<>();
        Double ave;
        for (String key : this.playerStats.keySet()) {
            ave = (double) (this.playerStats.get(key) / (double)divider);
            ave = (double) Math.round((ave*100)/100);
            averages.put(key, ave);
        }
        averages.putAll(this.percentages);
        return averages;
    }

    public void addOnCourt(onCourt newOnCourt){
        this.onCourts.add(newOnCourt);
    }

    public onCourt checkOnCourt(ArrayList<Player> currentCourt){
        for (onCourt Court : this.onCourts){
            if (Court.checkByPlayers(currentCourt)){
                return Court;
            }
        }
        onCourt newCourt = new onCourt(this.team);
        for (Player player : currentCourt) {
            newCourt.addPlayer(player);
        }
        return newCourt;
    }

}
