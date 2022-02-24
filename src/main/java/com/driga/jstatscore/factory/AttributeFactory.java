package com.driga.jstatscore.factory;

import com.driga.jstatscore.JStatsCore;
import com.driga.jstatscore.api.JStatsCoreAPI;
import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.prototype.JAttribute;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.Objects;

public class AttributeFactory {

    private static AttributeFactory attributeFactory;
    private JStatsCore plugin;
    private JStatsCoreAPI api;
    private ConfigurationSection section;

    public AttributeFactory() {
        plugin = JStatsCore.getInstance();
        api = JStatsCoreAPI.getInstance();
        section = plugin.getStatsConfig().getConfig().getConfigurationSection("Stats");
    }

    public static AttributeFactory getInstance() {
        if (AttributeFactory.attributeFactory == null) {
            AttributeFactory.attributeFactory = new AttributeFactory();
        }
        return AttributeFactory.attributeFactory;
    }

    public void registerAttributes(){
        for (String attr : section.getKeys(false)){
            api.getAttributes().put(attr, find(attr));
        }
    }

    public Attribute find(String name) {
        ConfigurationSection attributeSection = section.getConfigurationSection(name);
        String attributeName = attributeSection.getString("Name");
        Double multiplier = attributeSection.getDouble("Multiplier");
        Double maxValue = attributeSection.getDouble("MaxValue");
        Double costMultiplier = attributeSection.getDouble("CostMultiplier");

        return new JAttribute(attributeName, multiplier, maxValue, costMultiplier);
    }

    public String getByAttribute(Attribute attribute) {
        for (Map.Entry<String, Attribute> entry : api.getAttributes().getMap().entrySet()) {
            if (Objects.equals(attribute, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void reloadConfig(){
        section = plugin.getStatsConfig().getConfig().getConfigurationSection("Stats");
        registerAttributes();
    }
}
