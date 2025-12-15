package com.crayonsmp.api.config;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Configuration extends YamlConfiguration {
    @Getter
    private final File file;
    private final String name;

    public Configuration(File file, String name) {
        super();
        this.file = file;
        this.name = name;
        try {
            super.load(file);
        } catch (IOException | InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        }
    }

    public void setDefault(String a, Object b) {
        if (!isSet(a)) {
            set(a, b);
            save();
        }
    }

    public void save() {
        try {
            super.save(file);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot save " + file, ex);
        }
    }

    public void delete() {
        file.delete();
        ConfigurationUtil.cachemap.remove(name);
    }
}
