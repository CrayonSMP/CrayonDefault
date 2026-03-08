package com.crayonsmp.api.config;

// Entferne das @Getter oben am Feld
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Configuration extends YamlConfiguration {

    private final File file; // @Getter entfernt
    private final String name;

    public Configuration(File file, String name) {
        super();
        this.file = file;
        this.name = name;
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            super.load(file);
        } catch (IOException | InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        }
    }

    // Füge die Methode MANUELL hinzu
    public File getFile() {
        return this.file;
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
        if (file.exists()) {
            file.delete();
        }
        ConfigurationUtil.cachemap.remove(name);
    }
}