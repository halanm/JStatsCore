package com.driga.jstatscore.prototype;

import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.provider.SubjectProvider;
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
    }

    @Override
    public void addAttributeLevel(String key, Double value){
        Double current = 0.0;
        if(attributesLevel.containsKey(key)){
            current = attributesLevel.get(key);
        }
        Double now = current + value;
        attributesLevel.put(key, now);
    }

    @Override
    public void removeAttributeLevel(String key, Double value){
        Double current = 0.0;
        if(attributesLevel.containsKey(key)){
            current = attributesLevel.get(key);
        }
        Double now = current - value;
        attributesLevel.put(key, now);
    }

    @Override
    public void setTrainingPoints(Double value){
        trainingPoints = value;
    }

    @Override
    public void addTrainingPoints(Double value){
        Double now = trainingPoints + value;
        trainingPoints = now;
    }

    @Override
    public void removeTrainingPoints(Double value){
        Double now = trainingPoints - value;
        trainingPoints = now;
    }
}
