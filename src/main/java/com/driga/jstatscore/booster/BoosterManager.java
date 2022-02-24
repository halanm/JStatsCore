package com.driga.jstatscore.booster;

import com.driga.jstatscore.JStatsCore;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.prototype.JSubject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class BoosterManager {

    private static BoosterManager boosterManager;
    private JStatsCore plugin;
    private Map<String, List<String>> subjectBoosterMap;
    private Map<String, Double> boosterMap;
    private ConfigurationSection section;

    public static BoosterManager getInstance() {
        if (BoosterManager.boosterManager == null) {
            BoosterManager.boosterManager = new BoosterManager();
        }
        return BoosterManager.boosterManager;
    }

    public BoosterManager() {
        this.plugin = JStatsCore.getInstance();
        this.subjectBoosterMap = new HashMap<>();
        this.boosterMap = new HashMap<>();
        section = plugin.getBoostersConfig().getConfig().getConfigurationSection("Boosters");
    }

    public void registerBoosters(){
        for (String boost : section.getKeys(false)){
            boosterMap.put(boost.toUpperCase(), section.getDouble(boost));
        }
    }

    public void useBooster(Subject subject, String booster, Long seconds){
        Long millis = seconds * 1000;
        Long time = System.currentTimeMillis() + millis;
        List<String> boosterList = new ArrayList<>();
        if(subjectBoosterMap.containsKey(subject.getName())){
            boosterList = subjectBoosterMap.get(subject.getName());
        }
        String toPut = booster + ":" + time.toString();
        boosterList.add(toPut);
        subjectBoosterMap.put(subject.getName(), boosterList);
    }

    public Double getBoostersTotal(Subject subject){
        Double total = 0.0;
        List<String> boosterList = new ArrayList<>();
        if(subjectBoosterMap.containsKey(subject.getName())){
            boosterList = subjectBoosterMap.get(subject.getName());
        }
        for(String boost : boosterList){
            String[] split = boost.split(":");
            String booster = split[0];
            Double multiplier = boosterMap.get(booster);
            total += multiplier;
        }
        return total;
    }

    public boolean exists(String booster){
        if(boosterMap.containsKey(booster)){
            return true;
        }
        return false;
    }

    public boolean hasBooster(Subject subject){
        if(subjectBoosterMap.containsKey(subject.getName())){
            return true;
        }
        return false;
    }

    public void startTask(){
        new BukkitRunnable(){
            @Override
            public void run() {
                for(String subject : subjectBoosterMap.keySet()){
                    long current = System.currentTimeMillis();
                    for(String booster : subjectBoosterMap.get(subject)){
                        if(!booster.equals("")){
                            String[] split = booster.split(":");
                            long millis = Long.valueOf(split[1]);
                            if(millis <= current){
                                subjectBoosterMap.remove(subject);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(JStatsCore.getInstance(), 20, 20);
    }

    public void saveToDatabase(){
        plugin.getDatabaseProvider().execute("UPDATE booster SET map = ? WHERE name = ?",
                mapToString(subjectBoosterMap), "BOOSTER");
    }

    public void loadFromDatabase(){
        String mapString = loadMapString();
        if(loadMapString() != null){
            String[] pairs = mapString.replace("{", "").replace("}", "").replaceAll(" ", "").split(",");
            for (String pair : pairs) {
                if(!pair.equals("")){
                    String[] keyValue = pair.split("=");
                    if(!keyValue[1].equals("[]")){
                        List<String> list = stringToList(keyValue[1]);
                        subjectBoosterMap.put(keyValue[0], list);
                    }
                }
            }
        }
    }

    private String loadMapString(){
        Optional<String> optional = plugin.getDatabaseProvider().query(String.class, "SELECT * FROM booster WHERE name = ?", resultSet -> {
            String string = resultSet.getString("map");
            resultSet.close();
            return string;
        }, "BOOSTER");
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    private String mapToString(Map<String, List<String>> map){
        Map<String, String> stringMap = new HashMap<>();
        for(String key : map.keySet()){
            String list = map.get(key).toString().replaceAll("," , "|");
            stringMap.put(key, list);
        }
        return stringMap.toString();
    }

    private List<String> stringToList(String string){
        List<String> toReturn = new ArrayList<>();
        String replaced = string.replace("[","").replace("]","");
        String[] split = replaced.split("\\|");
        for(String str : split){
            toReturn.add(str);
        }
        return toReturn;
    }

    public void reloadConfig(){
        section = plugin.getBoostersConfig().getConfig().getConfigurationSection("Boosters");
    }
}
