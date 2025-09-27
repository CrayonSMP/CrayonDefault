package com.crayonsmp.paper.utils.config;


import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigUtil {
    public static Map<String, SConfig> cachemap = new HashMap<>();
    private final Plugin plugin;

    public ConfigUtil(Plugin plugin) {
        this.plugin = plugin;
    }

    public static SConfig getConfig(String name, Plugin plugin) {
        if (cachemap.get(name) != null) {
            return cachemap.get(name);
        }

        SConfig sConfig = new SConfig(new File(plugin.getDataFolder(), name + ".yml"), name);
        cachemap.put(name, sConfig);
        return sConfig;
    }

    public static void clearAllCache() {
        cachemap.clear();
    }


    public static void saveALL() {
        cachemap.forEach((a, b) -> {
            try {
                b.save();
            } catch (Exception e) {
                System.out.println("Error saving config: " + a);
                e.printStackTrace();
            }

        });
    }

    public void reloadALL() {
        saveALL();
        clearAllCache();
        for (String configName : plugin.getDataFolder().list()) {
            if (configName.endsWith(".yml")) {
                getConfig(configName.replace(".yml", ""), plugin);
            }
        }
    }
}