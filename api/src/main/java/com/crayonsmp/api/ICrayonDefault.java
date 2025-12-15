package com.crayonsmp.api;

import com.crayonsmp.api.artifact.IArtifactService;
import com.crayonsmp.api.util.IProtocolManager;
import com.crayonsmp.api.twitch.ITwitchService;
import com.crayonsmp.api.waystone.IWaystoneService;

public interface ICrayonDefault {
    IArtifactService getArtifactService();
    IWaystoneService getWaystoneService();
    ITwitchService getTwitchService();
    IProtocolManager getProtocolManager();
}
