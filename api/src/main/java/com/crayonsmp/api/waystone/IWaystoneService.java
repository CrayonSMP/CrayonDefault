package com.crayonsmp.api.waystone;

import com.crayonsmp.api.ICrayonDefault;

public interface IWaystoneService {
    void init(ICrayonDefault instance);
    void initConfig();
    void readWaystones();
    void saveWaystones();
    void addWaystone(IWaystone waystone);
    void removeWaystone(IWaystone waystone);
}
