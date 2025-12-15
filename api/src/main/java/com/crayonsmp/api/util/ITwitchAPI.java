package com.crayonsmp.api.util;

import com.crayonsmp.api.twitch.IStreamer;

import java.io.IOException;

public interface ITwitchAPI {
    IStreamer getStreamer(String loginName) throws IOException;
    boolean isStreamerExists(String loginName);
}
