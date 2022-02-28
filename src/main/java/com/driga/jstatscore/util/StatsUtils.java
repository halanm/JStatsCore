package com.driga.jstatscore.util;

import com.driga.jstatscore.JStatsCore;
import com.driga.jstatscore.api.JStatsCoreAPI;
import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.provider.SubjectProvider;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class StatsUtils {

    private static StatsUtils statsUtils;
    private JStatsCore plugin;
    private ConfigurationSection section;

    public StatsUtils() {
        this.plugin = JStatsCore.getInstance();
        section = plugin.getStatsConfig().getConfig().getConfigurationSection("Recover");
    }

    public static StatsUtils getInstance() {
        if (StatsUtils.statsUtils == null) {
            StatsUtils.statsUtils = new StatsUtils();
        }
        return StatsUtils.statsUtils;
    }

    public void startRecoverTask(Subject subject){
        Long time = Long.valueOf(section.getInt("Time") * 20);
        new BukkitRunnable() {
            public void run() {
                if (subject.getPlayer() == null || !subject.getPlayer().isOnline()) {
                    this.cancel();
                }else{
                    double recoverHP = getRegenHP();
                    double recoverSP = getRegenSP();
                    double hp = subject.getAttributeLevel("HP") + subject.getAttributeLevel("CONSTITUTION")
                            * recoverHP;
                    double sp = subject.getAttributeLevel("SP") + subject.getAttributeLevel("ENERGY")
                            * recoverSP;

                    if(hp > SubjectProvider.getInstance().getAttributeValue(subject, "CONSTITUTION")){
                        hp = SubjectProvider.getInstance().getAttributeValue(subject, "CONSTITUTION");
                    }
                    if(sp > SubjectProvider.getInstance().getAttributeValue(subject, "ENERGY")){
                        sp = SubjectProvider.getInstance().getAttributeValue(subject, "ENERGY");
                    }
                    if(subject.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
                        hp = SubjectProvider.getInstance().getAttributeValue(subject, "CONSTITUTION");
                        sp = SubjectProvider.getInstance().getAttributeValue(subject, "ENERGY");
                        subject.setAttributeLevel("HP", hp);
                        subject.setAttributeLevel("SP", sp);
                    }
                    if(subject.getPlayer().getFoodLevel() > 6){
                        subject.setAttributeLevel("HP", hp);
                        subject.setAttributeLevel("SP", sp);
                    }
                }
            }
        }.runTaskTimer(JStatsCore.getInstance(), time, time);
    }

    public void startSpeedTask(Subject subject){
        new BukkitRunnable() {
            public void run() {
                if (subject.getPlayer() == null || !subject.getPlayer().isOnline()) {
                    this.cancel();
                }else{
                    subject.getPlayer().setWalkSpeed((float) SubjectProvider.getInstance().getSpeedValue(subject));
                }
            }
        }.runTaskTimer(JStatsCore.getInstance(), 0, 20);
    }

    public Double getRegenHP(){
        return section.getDouble("HP");
    }

    public Double getRegenSP(){
        return section.getDouble("SP");
    }

    public void reloadConfig(){
        section = plugin.getStatsConfig().getConfig().getConfigurationSection("Recover");
    }
}
