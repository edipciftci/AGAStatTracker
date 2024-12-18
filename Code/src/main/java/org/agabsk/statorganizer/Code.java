package org.agabsk.statorganizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Code {
    public static void main(String[] args) {
        ArrayList<Player> playerPool = new ArrayList<>();
        ArrayList<Team> teamPool = new ArrayList<>();
        String urlString = "https://eapi.web.prod.cloud.atriumsports.com/v1/embed/106/fixture_detail?state=eJwtjDEKwzAMAL8SNFcQyXZs9xklH5CiaMpgkk4t_XsQdLuD475wwXMC1lKrbY6Ll4xEu6NwT6gpCYtXK1TgMcER8fvE9RX2CRs6gj24unU2E2xzn_8b2hY00l2bm-TO8LsBt78d8g";
        String playerListURLString = "https://eapi.web.prod.cloud.atriumsports.com/v1/embed/106/fixture_detail?state=eJwtjEEKhDAMAL8iOW_ApNbafYb4gbQxIHja9rTi3yXgcYZhLmjwHYBLTEmr4WxxQqLdUDgHLCEIiyWNFOEzwOlx_-G2Ov2dWpd-tH7U5spcJdPMqoLLmMf3RnVGpbKXxVSmzHA_zb0g-w";
        Game game = newGame(urlString, playerListURLString, playerPool, teamPool);
        System.out.println(game.getGameName());
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static ArrayList<Player>[] getPlayers(String playerListUrlString, ArrayList<Player> playerPool){

        ArrayList<Player> homePlayerList = new ArrayList<>();
        ArrayList<Player> awayPlayerList = new ArrayList<>();

        try {
            
            @SuppressWarnings("deprecation")
                    URL playerListURL = new URL(playerListUrlString);
            
            HttpURLConnection connection = (HttpURLConnection) playerListURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Failed to fetch data. Response code: " + responseCode);
                return null;
            }
            
            StringBuilder content;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
            
            String response = content.toString();
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            
            JsonObject awayPersons = (JsonObject) jsonResponse.getAsJsonObject("data").getAsJsonObject("statistics").getAsJsonObject("away").getAsJsonArray("persons").get(0);
            JsonArray awayPlayers = awayPersons.getAsJsonArray("rows");

            JsonObject homePersons = (JsonObject) jsonResponse.getAsJsonObject("data").getAsJsonObject("statistics").getAsJsonObject("home").getAsJsonArray("persons").get(0);
            JsonArray homePlayers = homePersons.getAsJsonArray("rows");

            for (int i = 0 ; i < awayPlayers.size() ; i++){
                boolean newPlayerCheck = true;
                String ID = awayPlayers.get(i).getAsJsonObject().get("personId").getAsString();
                for (Player player : playerPool) {
                    if (player.getPlayerID().equals(ID)){
                        awayPlayerList.add(player);
                        newPlayerCheck = false;
                        break;
                    }
                }
                if (newPlayerCheck){
                    Player newPlayer = new Player(ID);
                    newPlayer.setPlayerName(awayPlayers.get(i).getAsJsonObject().get("personName").getAsString());
                    newPlayer.setPlayerNum(awayPlayers.get(i).getAsJsonObject().get("bib").getAsInt());
                    playerPool.add(newPlayer);
                    awayPlayerList.add(newPlayer);
                }
            }

            for (int i = 0 ; i < homePlayers.size() ; i++){
                boolean newPlayerCheck = true;
                String ID = homePlayers.get(i).getAsJsonObject().get("personId").getAsString();
                for (Player player : playerPool) {
                    if (player.getPlayerID().equals(ID)){
                        homePlayerList.add(player);
                        newPlayerCheck = false;
                        break;
                    }
                }
                if (newPlayerCheck){
                    Player newPlayer = new Player(ID);
                    newPlayer.setPlayerName(homePlayers.get(i).getAsJsonObject().get("personName").getAsString());
                    newPlayer.setPlayerNum(homePlayers.get(i).getAsJsonObject().get("bib").getAsInt());
                    playerPool.add(newPlayer);
                    homePlayerList.add(newPlayer);
                }
            }

        }   catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
        ArrayList<Player>[] playerTuple = new ArrayList[2];
        playerTuple[0] = homePlayerList;
        playerTuple[1] = awayPlayerList;
        return playerTuple;
    }
    
    @SuppressWarnings("CallToPrintStackTrace")
    public static Game newGame(String gameUrlString, String playerListUrlString, ArrayList<Player> playerPool, ArrayList<Team> teamPool){
        try {
            
            ArrayList<Player>[] players = getPlayers(playerListUrlString, playerPool);

            @SuppressWarnings("deprecation")
            URL gameURL = new URL(gameUrlString);

            HttpURLConnection connection = (HttpURLConnection) gameURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Failed to fetch data. Response code: " + responseCode);
                return null;
            }

            StringBuilder content;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }

            String response = content.toString();
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();

            JsonObject gameJson = jsonResponse.getAsJsonObject("data");
            Game game = new Game(players);

            JsonObject homeJSON = (JsonObject) gameJson.getAsJsonObject("banner").getAsJsonObject("fixture").getAsJsonArray("competitors").get(0).getAsJsonObject();
            JsonObject awayJSON = (JsonObject) gameJson.getAsJsonObject("banner").getAsJsonObject("fixture").getAsJsonArray("competitors").get(1).getAsJsonObject();

            boolean homeBool = true;
            boolean awayBool = true;

            for (Team team : teamPool) {
                if (team.getTeamName().matches(homeJSON.get("name").getAsString())){
                    homeBool = false;
                    game.setHomeTeam(team);
                    Team homeTeam = team;
                    homeTeam.addGame(game);
                    continue;
                }
                if (team.getTeamName().matches(awayJSON.get("name").getAsString())){
                    awayBool = false;
                    game.setHomeTeam(team);
                    Team awayTeam = team;
                    awayTeam.addGame(game);
                }
            }

            if (homeBool){
                Team homeTeam = new Team(homeJSON.get("name").getAsString());
                game.setHomeTeam(homeTeam);
                homeTeam.addGame(game);
                teamPool.add(homeTeam);
            }
            if (awayBool){
                Team awayTeam = new Team(awayJSON.get("name").getAsString());
                game.setAwayTeam(awayTeam);
                awayTeam.addGame(game);
                teamPool.add(awayTeam);
            }

            game.setGameName();

            String[] qtrKeys = {"1", "2", "3", "4", "11", "12", "13", "14"};
            JsonObject pbp = gameJson.getAsJsonObject("pbp");
            for (String key : qtrKeys) {
                if (!pbp.has(key)){
                    break;
                }
                game.setQtr(pbp.getAsJsonObject(key).getAsJsonArray("events"), Integer.parseInt(key));
            }

            return game;

        } catch (JsonSyntaxException | IOException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

}
