package org.agabsk.statorganizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Code {

    public static void main(String[] args) {

        ArrayList<Player> playerPool = new ArrayList<>();
        ArrayList<Team> teamPool = new ArrayList<>();
        ArrayList<Game> gamePool = new ArrayList<>();
        String urlHead = "https://eapi.web.prod.cloud.atriumsports.com/v1/embed/106/fixture_detail?state=";

        String fixtureURLString = "https://eapi.web.prod.cloud.atriumsports.com/v1/embed/106/entity_detail?state=eJxNjEkKwzAMAL8SdK6gsi0vfUbJByTHgkJPSQohpX8vuuU4wzBf2OAxQVAuZemG2Tgh0TCU0CJqjBLEysLEcJvg7fG-4vx0Op3sdeyfdWwuhotKVUbucnmpVsZcut5TbSk1gt8fdRgfSA";

        playGames(fixtureURLString, urlHead, playerPool, teamPool, gamePool);

        Team ankAkademi;
        for (Team team : teamPool) {
            if (team.getTeamName().equals("ANKARA GENÇ AKADEMİ")){
                ankAkademi = team;
                writeOnCourtStatsToFile(ankAkademi.getOnCourts(), "onCourtStats");
                break;
            }
        }

    }

    /**
     * Play games based on the fixture URL.
     * @param fixtureURLString the URL string for the fixture
     * @param urlHead the base URL for fetching game data
     * @param playerPool the pool of players
     * @param teamPool the pool of teams
     * @param gamePool the pool of games
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void playGames(String fixtureURLString, String urlHead, ArrayList<Player> playerPool, ArrayList<Team> teamPool, ArrayList<Game> gamePool){
        try{
            @SuppressWarnings("deprecation")
            URL fixtureURL = new URL(fixtureURLString);

            HttpURLConnection fixtureConnection = (HttpsURLConnection) fixtureURL.openConnection();
            fixtureConnection.setRequestMethod("GET");
            fixtureConnection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = fixtureConnection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Failed to fetch data. Response code: " + responseCode);
                return;
            }

            StringBuilder content;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(fixtureConnection.getInputStream()))) {
                String inputLine;
                content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }            

            String response = content.toString();
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();

            JsonArray gameList = jsonResponse.getAsJsonObject("data").getAsJsonObject("team").getAsJsonObject("fixtures").getAsJsonArray("items");
            
            for (int i=0 ; i<gameList.size() ; i++){
                JsonObject temp = gameList.get(i).getAsJsonObject();
                if (!("CONFIRMED".equals(temp.getAsJsonObject("status").get("value").getAsString()))){
                    System.out.println("Game ".concat(String.valueOf(i+1)).concat(" is not played yet."));
                    break;
                }
                String boxScoreURL = urlHead.concat(temp.get("link").getAsString().substring(6));
                Game game = getPlayers(boxScoreURL, playerPool, teamPool, urlHead);
                gamePool.add(game);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Get players from the player list URL.
     * @param playerListUrlString the URL string for the player list
     * @param playerPool the pool of players
     * @param teamPool the pool of teams
     * @param urlHead the base URL for fetching game data
     * @return the game object
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static Game getPlayers(String playerListUrlString, ArrayList<Player> playerPool, ArrayList<Team> teamPool, String urlHead){

        ArrayList<Player> homePlayerList = new ArrayList<>();
        ArrayList<Player> awayPlayerList = new ArrayList<>();

        ArrayList<Player> homeFive = new ArrayList<>();
        ArrayList<Player> awayFive = new ArrayList<>();

        onCourt homeOnCourt, awayOnCourt;

        String gameFlowURL = null;

        try {
            
            // Fetch player list data from the URL
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

            // Get the URL for the game flow data
            gameFlowURL = urlHead.concat(jsonResponse.getAsJsonObject("data").getAsJsonArray("tabs").get(1).getAsJsonObject().get("link").getAsString().substring(6));

            JsonObject awayPersons = (JsonObject) jsonResponse.getAsJsonObject("data").getAsJsonObject("statistics").getAsJsonObject("away").getAsJsonArray("persons").get(0);
            JsonArray awayPlayers = awayPersons.getAsJsonArray("rows");

            JsonObject homePersons = (JsonObject) jsonResponse.getAsJsonObject("data").getAsJsonObject("statistics").getAsJsonObject("home").getAsJsonArray("persons").get(0);
            JsonArray homePlayers = homePersons.getAsJsonArray("rows");

            // Process away players
            for (int i = 0 ; i < awayPlayers.size() ; i++){
                boolean newPlayerCheck = true;
                String ID = awayPlayers.get(i).getAsJsonObject().get("personId").getAsString();
                String starter = awayPlayers.get(i).getAsJsonObject().get("starter").getAsString();
                for (Player player : playerPool) {
                    if (player.getPlayerID().equals(ID)){
                        awayPlayerList.add(player);
                        newPlayerCheck = false;
                        if (starter.equals("true")){
                            awayFive.add(player);
                        }
                        break;
                    }
                }
                if (newPlayerCheck){
                    Player newPlayer = new Player(ID);
                    newPlayer.setPlayerName(awayPlayers.get(i).getAsJsonObject().get("personName").getAsString());
                    newPlayer.setPlayerNum(awayPlayers.get(i).getAsJsonObject().get("bib").getAsInt());
                    playerPool.add(newPlayer);
                    awayPlayerList.add(newPlayer);
                    if (starter.equals("true")){
                        awayFive.add(newPlayer);
                    }
                }
            }

            awayOnCourt = awayFive.get(0).checkOnCourt(awayFive);

            // Process home players
            for (int i = 0 ; i < homePlayers.size() ; i++){
                boolean newPlayerCheck = true;
                String ID = homePlayers.get(i).getAsJsonObject().get("personId").getAsString();
                String starter = homePlayers.get(i).getAsJsonObject().get("starter").getAsString();
                for (Player player : playerPool) {
                    if (player.getPlayerID().equals(ID)){
                        homePlayerList.add(player);
                        newPlayerCheck = false;
                        if (starter.equals("true")){
                            homeFive.add(player);
                        }
                        break;
                    }
                }
                if (newPlayerCheck){
                    Player newPlayer = new Player(ID);
                    newPlayer.setPlayerName(homePlayers.get(i).getAsJsonObject().get("personName").getAsString());
                    newPlayer.setPlayerNum(homePlayers.get(i).getAsJsonObject().get("bib").getAsInt());
                    playerPool.add(newPlayer);
                    homePlayerList.add(newPlayer);
                    if (starter.equals("true")){
                        homeFive.add(newPlayer);
                    }
                }
            }

            homeOnCourt = homeFive.get(0).checkOnCourt(homeFive);

        }   catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
        // Create a tuple of home and away players
        ArrayList<Player>[] playerTuple = new ArrayList[2];
        playerTuple[0] = homePlayerList;
        playerTuple[1] = awayPlayerList;

        onCourt[] onCourtTuple = new onCourt[2];
        onCourtTuple[0] = homeOnCourt;
        onCourtTuple[1] = awayOnCourt;

        // Create a new game with the player lists and team pool
        Game game = newGame(gameFlowURL, playerTuple, teamPool, onCourtTuple);
        return game;
    }
    
    /**
     * Create a new game from the game URL.
     * @param gameUrlString the URL string for the game
     * @param players an array of player lists for home and away teams
     * @param teamPool the pool of teams
     * @return the game object
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static Game newGame(String gameUrlString, ArrayList<Player>[] players, ArrayList<Team> teamPool, onCourt[] startingFives){
        try {

            // Fetch game data from the URL
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

            // Set home and away teams
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
                    game.setAwayTeam(team);
                    Team awayTeam = team;
                    awayTeam.addGame(game);
                }
            }

            // Add new home team if not found
            if (homeBool){
                Team homeTeam = new Team(homeJSON.get("name").getAsString());
                game.setHomeTeam(homeTeam);
                homeTeam.addGame(game);
                teamPool.add(homeTeam);
            }

            // Add new away team if not found
            if (awayBool){
                Team awayTeam = new Team(awayJSON.get("name").getAsString());
                game.setAwayTeam(awayTeam);
                awayTeam.addGame(game);
                teamPool.add(awayTeam);
            }

            game.setGameName();
            game.getHomeTeam().setCurrentOnCourt(startingFives[0]);
            game.getAwayTeam().setCurrentOnCourt(startingFives[1]);

            game.getHomeTeam().getCurrentOnCourt().setCurrentTimeInSeconds(0);
            game.getAwayTeam().getCurrentOnCourt().setCurrentTimeInSeconds(0);

            // Set events for each quarter
            String[] qtrKeys = {"1", "2", "3", "4", "11", "12", "13", "14"};
            JsonObject pbp = gameJson.getAsJsonObject("pbp");
            for (String key : qtrKeys) {
                if (!pbp.has(key)){
                    break;
                }
                game.setQtr(pbp.getAsJsonObject(key).getAsJsonArray("events"), Integer.parseInt(key));
            }

            game.gameOver();

            return game;

        } catch (JsonSyntaxException | IOException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeOnCourtStatsToFile(List<onCourt> onCourts, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Sort and filter onCourt objects based on total play time (ascending order)
            List<onCourt> sortedOnCourts = onCourts.stream()
                    .filter(five -> five.getTotalPlayTime() > 0) // Optional: only include those with play time > 0
                    .sorted(Comparator.comparingInt(onCourt::getTotalPlayTime).reversed()) // Sort by total play time
                    .collect(Collectors.toList());

            int courtIndex = 1;

            for (onCourt five : sortedOnCourts) {
                writer.write("----------------------------------------\n");
                writer.write("On-Court " + courtIndex + "\n");
                writer.write("----------------------------------------\n");

                // Get player stats structure
                Map<Player, Map<String, Double>> playersStruct = five.getPlayersStruct();

                // Write player names as column headers
                writer.write(String.format("%-25s", "Stat Type"));
                playersStruct.keySet().stream()
                    .map(Player::getPlayerName)
                    .forEach(playerName -> {
                        try {
                            writer.write(String.format("%-25s", playerName));
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
                writer.write("\n");

                // Collect all unique stat types
                Set<String> statTypes = playersStruct.values().stream()
                    .flatMap(stats -> stats.keySet().stream())
                    .sorted()
                    .collect(Collectors.toCollection(LinkedHashSet::new));

                // Write stats row by row
                statTypes.forEach(statType -> {
                    try {
                        writer.write(String.format("%-25s", statType));
                        playersStruct.values().stream()
                            .map(stats -> stats.getOrDefault(statType, 0.0))
                            .forEach(statValue -> {
                                try {
                                    writer.write(String.format("%-25.2f", statValue));
                                } catch (IOException e) {
                                    throw new UncheckedIOException(e);
                                }
                            });
                        writer.write("\n");
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });

                // Write total stats header
                writer.write("----------------------------------------\n");
                statTypes.stream()
                    .map(statType -> statType.contains("-") ? statType.substring(0, 7) : statType)
                    .forEach(statHeader -> {
                        try {
                            writer.write(String.format("%-9s", statHeader));
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
                writer.write("\n");

                // Write total stats values
                Map<String, Double> onCourtStats = five.getTotalStats();
                statTypes.stream()
                    .map(statType -> onCourtStats.getOrDefault(statType, 0.0))
                    .forEach(totalValue -> {
                        try {
                            writer.write(String.format("%-9.2f", totalValue));
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
                writer.write("\n");

                writer.write("Total Play Time:\t");
                writer.write(String.format("%-6s", five.getTotalPlayTime()));
                writer.write("s\n");

                writer.write("----------------------------------------\n");
                writer.write("************************\n");

                courtIndex++;
            }

            System.out.println("Stats successfully written to " + fileName);

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

}
