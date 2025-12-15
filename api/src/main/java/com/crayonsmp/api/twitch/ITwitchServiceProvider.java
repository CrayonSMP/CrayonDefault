package com.crayonsmp.api.twitch;

import java.io.IOException;

public interface ITwitchServiceProvider {
    void authenticate() throws IOException;
    IStreamer getStreamer(String loginName);
    boolean isStreamerExists(String loginName);
}
