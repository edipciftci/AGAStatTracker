package org.agabsk.statorganizer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Code {
    public static void main(String[] args) {
        try {
            String urlString = "https://eapi.web.prod.cloud.atriumsports.com/v1/embed/106/fixture_detail?state=eJwtjDEKwzAMAL8SNFcQyXZs9xklH5CiaMpgkk4t_XsQdLuD475wwXMC1lKrbY6Ll4xEu6NwT6gpCYtXK1TgMcER8fvE9RX2CRs6gj24unU2E2xzn_8b2hY00l2bm-TO8LsBt78d8g";

            @SuppressWarnings("deprecation")
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Failed to fetch data. Response code: " + responseCode);
                return;
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

            JsonObject gameJson = jsonResponse.getAsJsonObject("data").getAsJsonObject("pbp");
            Game game = new Game();
            String[] qtrKeys = {"1", "2", "3", "4", "11", "12", "13", "14"};

            for (String key : qtrKeys) {
                if (!gameJson.has(key)){
                    break;
                }
                game.setQtr(gameJson.getAsJsonObject(key).getAsJsonArray("events"), Integer.parseInt(key));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
