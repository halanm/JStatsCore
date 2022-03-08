package com.driga.jstatscore;

import com.driga.jstatscore.api.JStatsCoreAPI;
import com.driga.jstatscore.booster.BoosterManager;
import com.driga.jstatscore.command.FormCommand;
import com.driga.jstatscore.command.MainComand;
import com.driga.jstatscore.command.StatCommand;
import com.driga.jstatscore.config.ConfigManager;
import com.driga.jstatscore.database.DatabaseProvider;
import com.driga.jstatscore.factory.AttributeFactory;
import com.driga.jstatscore.factory.FormFactory;
import com.driga.jstatscore.form.FormManager;
import com.driga.jstatscore.inventory.sustainer.InventorySustainer;
import com.driga.jstatscore.listener.CustomEventListener;
import com.driga.jstatscore.listener.EventListener;
import com.driga.jstatscore.listener.MagicSpellsEventListener;
import com.driga.jstatscore.packet.PacketManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class JStatsCore extends JavaPlugin {

    private DatabaseProvider databaseProvider;
    private ConfigManager statsConfig;
    private ConfigManager formsConfig;
    private ConfigManager boostersConfig;

    public static JStatsCore getInstance() {
        return (JStatsCore)getPlugin((Class)JStatsCore.class);
    }

    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        statsConfig = new ConfigManager("stats");
        statsConfig.saveDefaultConfig();
        statsConfig.customConfigLoad();

        formsConfig = new ConfigManager("forms");
        formsConfig.saveDefaultConfig();
        formsConfig.customConfigLoad();

        boostersConfig = new ConfigManager("boosters");
        boostersConfig.saveDefaultConfig();
        boostersConfig.customConfigLoad();

        (databaseProvider = new DatabaseProvider()).openConnection();

        AttributeFactory.getInstance().registerAttributes();
        FormFactory.getInstance().registerForms();
        FormManager.getInstance().startTask();
        BoosterManager.getInstance().registerBoosters();
        BoosterManager.getInstance().loadFromDatabase();
        BoosterManager.getInstance().startTask();

        InventorySustainer.register(this);

        PacketManager.getInstance().registerHearthRemoveListener();
        Bukkit.getPluginManager().registerEvents(new EventListener() , this);
        Bukkit.getPluginManager().registerEvents(new CustomEventListener() , this);
        if(getServer().getPluginManager().getPlugin("MagicSpells") != null){
            Bukkit.getPluginManager().registerEvents(new MagicSpellsEventListener() , this);
        }

        getCommand("stats").setExecutor(new StatCommand());
        getCommand("forms").setExecutor(new FormCommand());
        getCommand("jsc").setExecutor(new MainComand());
    }

    public void onDisable() {
        BoosterManager.getInstance().saveToDatabase();
        JStatsCoreAPI.getInstance().getSubjects().getMap().values().forEach(subject ->
            databaseProvider.update("UPDATE players SET tp = ?, attributes = ? WHERE name = ?",
            subject.getTrainingPoints().toString(), subject.getAttributesLevel().toString(), subject.getName()));
        this.databaseProvider.closeConnection();
    }

    public DatabaseProvider getDatabaseProvider() {
        return databaseProvider;
    }

    public ConfigManager getStatsConfig() {
        return statsConfig;
    }

    public ConfigManager getFormsConfig() {
        return formsConfig;
    }

    public ConfigManager getBoostersConfig() {
        return boostersConfig;
    }
}
