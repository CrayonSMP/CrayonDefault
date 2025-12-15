package com.crayonsmp.api.twitch;

import java.io.IOException;

public interface ITwitchAPI {
    IStreamer getStreamer(String loginName) throws IOException;
    boolean isStreamerExists(String loginName);
}
