package com.driga.jstatscore.prototype;

import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.nbt.NbtHandler;
import com.driga.jstatscore.provider.SubjectProvider;
import com.driga.jstatscore.util.StatsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class JSubject implements Subject {

    private String name;
    private Double trainingPoints;
    private Map<String, Double> attributesLevel;

    public JSubject(String name, Double trainingPoints, String attributesLevel) {
        this.name = name;
        this.trainingPoints = trainingPoints;
        this.attributesLevel = SubjectProvider.getInstance().fromString(attributesLevel);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(this.name);
    }

    @Override
    public Double getTrainingPoints() {
        return trainingPoints;
    }

    @Override
    public Map<String, Double> getAttributesLevel() {
        return attributesLevel;
    }

    @Override
    public Double getAttributeLevel(String key){
        if(attributesLevel.containsKey(key)){
            return attributesLevel.get(key);
        }
        return 0.0;
    }

    @Override
    public void setAttributeLevel(String key, Double value){
        attributesLevel.put(key, value);
        NbtHandler.getInstance().setValue(getPlayer(), key, value);
        if(key.equals("CONSTITUTION")){
            NbtHandler.getInstance().setValue(getPlayer(), "HP_MAX", SubjectProvider.getInstance().getAttributeValue(this, "CONSTITUTION"));
            NbtHandler.getInstance().setValue(getPlayer(), "HP_REGEN", getAttributeLevel("CONSTITUTION") * StatsUtils.getInstance().getRegenHP());
        }
        if(key.equals("ENERGY")){
            NbtHandler.getInstance().setValue(getPlayer(), "SP_MAX", SubjectProvider.getInstance().getAttributeValue(this, "ENERGY"));
            NbtHandler.getInstance().setValue(getPlayer(), "SP_REGEN", getAttributeLevel("ENERGY") * StatsUtils.getInstance().getRegenSP());
        }
    }

    @Override
    public void addAttributeLevel(String key, Double value){
        Double current = 0.0;
        if(attributesLevel.containsKey(key)){
            current = attributesLevel.get(key);
        }
        Double now = current + value;
        setAttributeLevel(key, now);
    }

    @Override
    public void removeAttributeLevel(String key, Double value){
        Double current = 0.0;
        if(attributesLevel.containsKey(key)){
            current = attributesLevel.get(key);
        }
        Double now = current - value;
        setAttributeLevel(key, now);
    }

    @Override
    public void setTrainingPoints(Double value){
        trainingPoints = value;
        NbtHandler.getInstance().setValue(getPlayer(), "TP", value);
    }

    @Override
    public void addTrainingPoints(Double value){
        Double now = trainingPoints + value;
        setTrainingPoints(now);
    }

    @Override
    public void removeTrainingPoints(Double value){
        Double now = trainingPoints - value;
        setTrainingPoints(now);
    }
}
