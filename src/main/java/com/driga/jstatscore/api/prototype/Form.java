package com.driga.jstatscore.api.prototype;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface Form {

    String getName();

    Map<Attribute, Double> getAttributeMultipliers();

    Double getAttributeMultiplier(Attribute p0);

    Integer getDuration();

    Integer getCooldown();

    String getModel();

    String getPermission();

    Double getEnergyCost();

    ItemStack getItem();
}
