package com.crayonsmp.paper.impls;

import com.crayonsmp.objects.Streamer;
import com.crayonsmp.paper.Main;
import com.crayonsmp.utils.TwitchAPI;

import java.io.IOException;

public class TwitchAPIimpl implements TwitchAPI {

    @Override
    public Streamer getStreamer(String loginName) throws IOException {
        return Main.twitchAPI.getStreamer(loginName);
    }

    @Override
    public boolean isStreamerExists(String loginName) {
        return Main.twitchAPI.isStreamerExists(loginName);
    }
}
