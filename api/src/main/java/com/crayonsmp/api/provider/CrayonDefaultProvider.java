package com.crayonsmp.api.provider;

import com.crayonsmp.api.ICrayonDefault;

public class CrayonDefaultProvider {
    private static ICrayonDefault instance;

    public static void register(ICrayonDefault instance) {
        CrayonDefaultProvider.instance = instance;
    }

    public static ICrayonDefault get() {
        return CrayonDefaultProvider.instance;
    }
}
