package org.agabsk.statorganizer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Code {
    public static void main(String[] args) {
        try {
            // The API endpoint URL
            String urlString = "https://eapi.web.prod.cloud.atriumsports.com/v1/embed/106/fixture_detail?state=eJwtjDEKwzAMAL8SNFcQyXZs9xklH5CiaMpgkk4t_XsQdLuD475wwXMC1lKrbY6Ll4xEu6NwT6gpCYtXK1TgMcER8fvE9RX2CRs6gj24unU2E2xzn_8b2hY00l2bm-TO8LsBt78d8g";

            // Create a URL object
            @SuppressWarnings("deprecation")
            URL url = new URL(urlString);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method (GET by default)
            connection.setRequestMethod("GET");

            // Optional: Set headers if needed (for example, User-Agent)
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            // Get the response code to ensure the request was successful
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Failed to fetch data. Response code: " + responseCode);
                return;
            }

            // Read the response from the input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            // Read line by line and append to the content
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            // Close the BufferedReader
            in.close();

            String response = content.toString();
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();

            JsonArray firstQtr = jsonResponse.getAsJsonObject("data").getAsJsonObject("pbp").getAsJsonObject("1").getAsJsonArray("events");

            System.out.println(firstQtr.get(0).getAsJsonObject().get("bib"));

            // Optionally, you could process the JSON here
            // For example, you can use a JSON library to parse the response

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
