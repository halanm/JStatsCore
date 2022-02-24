package com.driga.jstatscore.config;

import com.driga.jstatscore.JStatsCore;
import com.google.common.io.Files;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class ConfigManager {

    private JStatsCore plugin;
    private FileConfiguration dataConfig;
    private File configFile;
    private String config;
    private File fileToSave;

    public ConfigManager(String config) {
        plugin = JStatsCore.getInstance();
        dataConfig = null;
        configFile = null;
        this.config = config;
        fileToSave = plugin.getDataFolder();
        saveDefaultConfig();
        reloadConfig();
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(fileToSave, config + ".yml");
        }
        dataConfig = (FileConfiguration)YamlConfiguration.loadConfiguration(configFile);
        InputStream defaultStream = plugin.getResource(config + ".yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration((Reader)new InputStreamReader(defaultStream));
            dataConfig.setDefaults((Configuration)defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (dataConfig == null) {
            reloadConfig();
            customConfigLoad();
        }
        return dataConfig;
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(fileToSave, config + ".yml");
        }
        if (!configFile.exists()) {
            plugin.saveResource(config + ".yml", false);
        }
    }

    private void createFile() {
        try {
            configFile.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void customConfigLoad () {
        try {
            dataConfig.loadFromString (Files.toString (configFile, StandardCharsets.UTF_8));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace ();
        }
    }

    public void saveConfig() {
        if (dataConfig == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        }
        catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
            e.printStackTrace();
        }
    }
}
