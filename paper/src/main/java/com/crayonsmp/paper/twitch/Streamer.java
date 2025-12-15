package com.crayonsmp.paper.twitch;

import com.crayonsmp.api.twitch.IStreamer;

public class Streamer implements IStreamer {
    private final String loginName;
    private String id;
    private boolean isLive;
    private String title;
    private String gameName;

    public Streamer(String loginName) {
        this.loginName = loginName;
        this.isLive = false;
    }

    @Override
    public String getLoginName() {
        return loginName;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isLive() {
        return isLive;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getGameName() {
        return gameName;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setIsLive(boolean isLive) {
        this.isLive = isLive;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public String toString() {
        return String.format(
                "Streamer [Login: %s, ID: %s, Live: %s, Titel: '%s', Spiel: %s]",
                loginName, id, isLive ? "JA" : "NEIN",
                title != null ? title : "N/A",
                gameName != null ? gameName : "N/A"
        );
    }
}
