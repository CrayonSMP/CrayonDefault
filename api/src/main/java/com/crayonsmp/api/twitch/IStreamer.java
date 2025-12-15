package com.crayonsmp.api.twitch;

public interface IStreamer {

    String getLoginName();
    String getId();
    boolean isLive();
    String getTitle();
    String getGameName();
    void setId(String id);
    void setIsLive(boolean isLive);
    void setTitle(String title);
    void setGameName(String gameName);
}