package com.driga.jstatscore.api.prototype;

import org.bukkit.entity.Player;

import java.util.Map;

public interface Subject {

    String getName();

    Player getPlayer();

    Double getTrainingPoints();

    Map<String, Double> getAttributesLevel();

    Double getAttributeLevel(String p0);

    void setAttributeLevel(String p0, Double p1);

    void addAttributeLevel(String p0, Double p1);

    void removeAttributeLevel(String p0, Double p1);

    void setTrainingPoints(Double p0);

    void addTrainingPoints(Double p0);

    void removeTrainingPoints(Double p0);
}
