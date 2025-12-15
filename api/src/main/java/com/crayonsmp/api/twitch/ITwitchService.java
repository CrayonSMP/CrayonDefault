package com.crayonsmp.api.twitch;

import com.crayonsmp.api.ICrayonDefault;
import com.crayonsmp.api.config.Configuration;

import java.util.Map;

public interface ITwitchService {
    void init(ICrayonDefault instance);
    Configuration getTwitchConfig();
    ITwitchServiceProvider getTwitchServiceProvider();
    Map<String, String> getStreamers();
}
