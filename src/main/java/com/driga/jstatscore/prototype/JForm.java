package com.driga.jstatscore.prototype;

import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.prototype.Form;
import com.driga.jstatscore.api.prototype.Subject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class JForm implements Form {

    private String name;
    private Map<Attribute, Double> attributeMultipliers;
    private Integer duration;
    private Integer cooldown;
    private String model;
    private String permission;
    private Double energyCost;
    private ItemStack item;

    public JForm(String name, Map<Attribute, Double> attributeMultipliers, Integer duration, Integer cooldown, String model, String permission, ItemStack item, Double energyCost) {
        this.name = name;
        this.attributeMultipliers = attributeMultipliers;
        this.duration = duration;
        this.cooldown = cooldown;
        this.model = model;
        this.permission = permission;
        this.energyCost = energyCost;
        this.item = item;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<Attribute, Double> getAttributeMultipliers() {
        return attributeMultipliers;
    }

    @Override
    public Double getAttributeMultiplier(Attribute key) {
        if(attributeMultipliers.containsKey(key)){
            return attributeMultipliers.get(key);
        }
        return 0.0;
    }

    @Override
    public Integer getDuration() {
        return duration;
    }

    @Override
    public Integer getCooldown() {
        return cooldown;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public Double getEnergyCost() {
        return energyCost;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }
}
