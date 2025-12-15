package com.crayonsmp.paper.twitch;

import com.crayonsmp.api.twitch.IStreamer;
import com.crayonsmp.paper.CrayonDefault;

import java.io.IOException;

public class TwitchAPI implements Twitch {
    @Override
    public IStreamer getStreamer(String loginName) throws IOException {
        return CrayonDefault.twitchService.twitchAPI.getStreamer(loginName);
    }

    @Override
    public boolean isStreamerExists(String loginName) {
        return CrayonDefault.twitchService.twitchAPI.isStreamerExists(loginName);
    }
}
