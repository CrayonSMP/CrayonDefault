package com.crayonsmp.paper.utils;

import com.crayonsmp.objects.Streamer;
import com.crayonsmp.paper.Main;
import okhttp3.*;
import org.bukkit.Bukkit;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TwitchAPI {

    private final OkHttpClient client;
    private final String CLIENT_ID = Main.twitchcConfig.getString("twitch.client_id");
    private final String CLIENT_SECRET = Main.twitchcConfig.getString("twitch.client_secret");
    private String accessToken;

    private static final String TWITCH_AUTH_URL = "https://id.twitch.tv/oauth2/token";
    private static final String TWITCH_HELIX_BASE = "https://api.twitch.tv/helix/";

    public TwitchAPI() {
        this.client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public void authenticate() throws IOException {

        RequestBody body = new FormBody.Builder()
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("grant_type", "client_credentials")
                .build();

        Request request = new Request.Builder()
                .url(TWITCH_AUTH_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Fehler bei Authentifizierung: " + response);
            }

            JSONObject jsonObject = new JSONObject(response.body().string());
            this.accessToken = jsonObject.getString("access_token");
        }
    }

    public Streamer getStreamer(String loginName) {
        try {
        if (accessToken == null) {
                authenticate();
        }

        Streamer streamer = new Streamer(loginName);

        String userId = fetchUserId(loginName);
        if (userId == null) {
            Bukkit.getLogger().warning("❌ Streamer '" + loginName + "' nicht gefunden.");
            return streamer;
        }
        streamer.setId(userId);

        fetchStreamStatus(streamer);

        return streamer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isStreamerExists(String loginName) {
        try {
        if (accessToken == null) {
            authenticate();
        }
        String userId = fetchUserId(loginName);
        return userId != null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String fetchUserId(String loginName) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.get(TWITCH_HELIX_BASE + "users").newBuilder();
        urlBuilder.addQueryParameter("login", loginName);

        Request request = buildHelixRequest(urlBuilder.build().toString());

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                Bukkit.getLogger().warning("API-Fehler beim Abrufen der User ID: " + response);
                return null;
            }
            JSONObject jsonObject = new JSONObject(response.body().string());
            JSONArray dataArray = jsonObject.getJSONArray("data");

            if (dataArray.length() > 0) {
                return dataArray.getJSONObject(0).getString("id");
            }
            return null;
        }
    }

    private void fetchStreamStatus(Streamer streamer) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.get(TWITCH_HELIX_BASE + "streams").newBuilder();
        urlBuilder.addQueryParameter("user_id", streamer.getId());

        Request request = buildHelixRequest(urlBuilder.build().toString());

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                Bukkit.getLogger().warning("API-Fehler beim Abrufen des Stream-Status: " + response);
                return;
            }
            JSONObject jsonObject = new JSONObject(response.body().string());
            JSONArray dataArray = jsonObject.getJSONArray("data");

            if (dataArray.length() > 0) {
                JSONObject streamData = dataArray.getJSONObject(0);
                streamer.setIsLive(true);
                streamer.setTitle(streamData.getString("title"));
                streamer.setGameName(streamData.getString("game_name"));
            } else {
                streamer.setIsLive(false);
            }
        }
    }

    private Request buildHelixRequest(String url) {
        return new Request.Builder()
                .url(url)
                .header("Client-Id", CLIENT_ID)
                .header("Authorization", "Bearer " + accessToken)
                .build();
    }
}