package com.crayonsmp.objects;

public class Streamer {
    private final String loginName;
    private String id;
    private boolean isLive;
    private String title;
    private String gameName;

    public Streamer(String loginName) {
        this.loginName = loginName;
        this.isLive = false;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getId() {
        return id;
    }

    public boolean isLive() {
        return isLive;
    }

    public String getTitle() {
        return title;
    }

    public String getGameName() {
        return gameName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsLive(boolean isLive) {
        this.isLive = isLive;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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