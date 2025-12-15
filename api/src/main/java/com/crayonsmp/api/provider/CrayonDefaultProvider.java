package com.crayonsmp.api.provider;

import com.crayonsmp.api.ICrayonDefault;

public class CrayonDefaultProvider {
    private static ICrayonDefault instance;

    public static void register(ICrayonDefault instance) {
        if (CrayonDefaultProvider.instance != null) {
            System.err.println("CrayDefault Instance has been already declared.");
            return;
        }
        CrayonDefaultProvider.instance = instance;
    }

    public static ICrayonDefault get() {
        return CrayonDefaultProvider.instance;
    }
}
