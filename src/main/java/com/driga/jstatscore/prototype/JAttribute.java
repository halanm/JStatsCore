package com.driga.jstatscore.prototype;

import com.driga.jstatscore.api.prototype.Attribute;

import java.util.Map;

public class JAttribute implements Attribute {

    private String name;
    private Double multiplier;
    private Double maxValue;
    private Double tpCostMultiplier;

    public JAttribute(String name, Double multiplier, Double maxValue, Double tpCostMultiplier) {
        this.name = name.replaceAll("&", "§");
        this.multiplier = multiplier;
        this.maxValue = maxValue;
        this.tpCostMultiplier = tpCostMultiplier;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Double getMultiplier() {
        return multiplier;
    }

    @Override
    public Double getMaxValue() {
        return maxValue;
    }

    @Override
    public Double getTpCostMultiplier() {
        return tpCostMultiplier;
    }
}
