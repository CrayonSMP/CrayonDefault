package com.crayonsmp.utils;

import com.crayonsmp.objects.Streamer;

import java.io.IOException;

public interface TwitchAPI {
    Streamer getStreamer(String loginName) throws IOException;
    boolean isStreamerExists(String loginName);
}
