package com.crayonsmp.api.config;


import com.crayonsmp.api.ICrayonDefault;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigurationUtil {
    public static Map<String, Configuration> cachemap = new HashMap<>();
    private final ICrayonDefault instance;

    public ConfigurationUtil(ICrayonDefault instance) {
        this.instance = instance;
    }

    public static Configuration getConfig(String name, Plugin plugin) {
        if (cachemap.get(name) != null) {
            return cachemap.get(name);
        }
        Configuration configuration = new Configuration(new File(plugin.getDataFolder(), name + ".yml"), name);
        cachemap.put(name, configuration);
        return configuration;
    }

    public static void clearAllCache() {
        cachemap.clear();
    }


    public static void saveAll() {
        cachemap.forEach((a, b) -> {
            try {
                b.save();
            } catch (Exception e) {
                System.out.println("Error saving config: " + a);
                Bukkit.getLogger().log(Level.SEVERE, "Error saving config: " + a, e);
            }
        });
    }

    public void reloadAll() {
        JavaPlugin plugin = (JavaPlugin) this.instance;
        saveAll();
        clearAllCache();
        for (String configName : plugin.getDataFolder().list()) {
            if (configName.endsWith(".yml")) {
                getConfig(configName.replace(".yml", ""), plugin);
            }
        }
    }
}