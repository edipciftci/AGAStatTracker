package org.agabsk.statorganizer;

import java.util.ArrayList;

public class Team {
    private ArrayList<Player> roster;
    private String teamName;
    private ArrayList<Game> games;

    public Team(String teamName){
        this.roster = new ArrayList<>();
        this.teamName = teamName;
        this.games = new ArrayList<>();
    }

    public ArrayList<Player> getRoster(){
        return this.roster;
    }

    public Player getPlayerByID(String ID){
        for (Player player : this.roster) {
            if (ID.matches(player.getPlayerID())){
                return player;
            }
        }
        System.out.println("Player is not on the roster");
        return null;
    }

    public String getTeamName(){
        return this.teamName;
    }

    public void addGame(Game game){
        this.games.add(game);
    }

    public ArrayList<Game> getGames(){
        return this.games;
    }

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

}
