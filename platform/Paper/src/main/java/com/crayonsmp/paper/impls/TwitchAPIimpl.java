package com.crayonsmp.paper.impls;

import com.crayonsmp.objects.Streamer;
import com.crayonsmp.paper.services.TwitchService;
import com.crayonsmp.utils.TwitchAPI;

import java.io.IOException;

public class TwitchAPIimpl implements TwitchAPI {
    @Override
    public Streamer getStreamer(String loginName) throws IOException {
        return TwitchService.twitchAPI.getStreamer(loginName);
    }

    @Override
    public boolean isStreamerExists(String loginName) {
        return TwitchService.twitchAPI.isStreamerExists(loginName);
    }
}
