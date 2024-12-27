package org.agabsk.statorganizer;

import java.util.ArrayList;

public class Team {
    private final ArrayList<Player> roster;
    private final String teamName;
    private final ArrayList<Game> games;
    private final ArrayList<onCourt> onCourts;
    private onCourt currentOnCourt;
    private ArrayList<Player> currOnCrtPlayers;
    private Team currentOpponent;

    /**
     * Constructor to initialize team with name.
     * @param teamName the name of the team
     */
    public Team(String teamName){
        this.roster = new ArrayList<>();
        this.teamName = teamName;
        this.games = new ArrayList<>();
        this.onCourts = new ArrayList<>();
    }

    /**
     * Get the roster of the team.
     * @return the roster of the team
     */
    public ArrayList<Player> getRoster(){
        return this.roster;
    }

    /**
     * Get a player by their ID.
     * @param ID the ID of the player
     * @return the player with the given ID, or null if not found
     */
    public Player getPlayerByID(String ID){
        for (Player player : this.roster) {
            if (ID.matches(player.getPlayerID())){
                return player;
            }
        }
        return null;
    }

    /**
     * Get the name of the team.
     * @return the name of the team
     */
    public String getTeamName(){
        return this.teamName;
    }

    /**
     * Add a game to the team's game list.
     * @param game the game to add
     */
    public void addGame(Game game){
        this.games.add(game);
    }

    /**
     * Get the list of games the team has played.
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
    public Game getGameByOpp(Team Opp){
        for (Game game : this.games){
            if ((game.getAwayTeam() == Opp) || (game.getHomeTeam() == Opp)){
                return game;
            }
        }
        String errString = this.teamName.concat(" never played against ").concat(Opp.getTeamName());
        System.out.println(errString);
        return null;
    }

    /**
     * Add a player to the team's roster.
     * @param player the player to add
     */
    public void addPlayer(Player player){
        this.roster.add(player);
        if (player.getTeam() == null){
            player.setTeam(this);
        }
    }

    public void setCurrentOnCourt(onCourt curr){
        this.currentOnCourt = curr;
        if (!this.checkOnCourt(curr)){
            this.onCourts.add(curr);
        }
        this.currOnCrtPlayers = this.currentOnCourt.getPlayers();
    }

    public onCourt getCurrentOnCourt(){
        return this.currentOnCourt;
    }

    public boolean checkOnCourt(onCourt currOnCourt){
        for (onCourt temp : this.onCourts) {
            if (temp == currOnCourt){
                return true;
            }
        }
        return false;
    }

    public void subOut(Player player, int subTime){
        if (currOnCrtPlayers.size() == 5){
            this.currentOnCourt.addDuration(subTime - this.currentOnCourt.getCurrentTimeInSeconds());
        }
        this.currOnCrtPlayers.remove(player);
        if (currOnCrtPlayers.size() == 5){
            this.setCurrentOnCourt(currOnCrtPlayers.get(0).checkOnCourt(currOnCrtPlayers));
            this.currentOnCourt.setCurrentTimeInSeconds(subTime);
        }
    }

    public void subIn(Player player, int subTime){
        if (currOnCrtPlayers.size() == 5){
            this.currentOnCourt.addDuration(subTime - this.currentOnCourt.getCurrentTimeInSeconds());
        }
        this.currOnCrtPlayers.add(player);
        if (currOnCrtPlayers.size() == 5){
            this.setCurrentOnCourt(currOnCrtPlayers.get(0).checkOnCourt(currOnCrtPlayers));
            this.currentOnCourt.setCurrentTimeInSeconds(subTime);
        }
    }

    public ArrayList<onCourt> getOnCourts(){
        return this.onCourts;
    }

    public void setOpponent(Team Opponent){
        this.currentOpponent = Opponent;
    }

    public Team getCurrentOpponent(){
        return this.currentOpponent;
    }

}