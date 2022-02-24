package com.driga.jstatscore.factory;

import com.driga.jstatscore.JStatsCore;
import com.driga.jstatscore.api.JStatsCoreAPI;
import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.prototype.Form;
import com.driga.jstatscore.inventory.item.ItemBuilder;
import com.driga.jstatscore.prototype.JForm;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormFactory {

    private static FormFactory formFactory;
    private JStatsCore plugin;
    private JStatsCoreAPI api;
    private ConfigurationSection section;

    public FormFactory() {
        plugin = JStatsCore.getInstance();
        api = JStatsCoreAPI.getInstance();
        section = plugin.getFormsConfig().getConfig().getConfigurationSection("Forms");
    }

    public static FormFactory getInstance() {
        if (FormFactory.formFactory == null) {
            FormFactory.formFactory = new FormFactory();
        }
        return FormFactory.formFactory;
    }

    public void registerForms(){
        for (String formName : section.getKeys(false)){
            JStatsCoreAPI api = JStatsCoreAPI.getInstance();
            api.getForms().put(formName, find(formName));
        }
    }

    public Form find(String name) {
        ConfigurationSection formSection = section.getConfigurationSection(name);
        String formName = formSection.getString("Name");
        Map<Attribute, Double> attributeMultipliers = getAttributeMultipliers(name);
        Integer duration = formSection.getInt("Duration");
        Integer cooldown = formSection.getInt("Cooldown");
        String model = formSection.getString("Model");
        String permission = formSection.getString("Permission");
        Double energyCost = formSection.getDouble("EnergyCost");
        ItemStack item = getItem(name);

        return new JForm(formName, attributeMultipliers, duration, cooldown, model, permission, item, energyCost);
    }

    private Map<Attribute, Double> getAttributeMultipliers(String name){
        Map<Attribute, Double> attributeMultipliers = new HashMap<>();
        ConfigurationSection formSection = section.getConfigurationSection(name);
        ConfigurationSection attributeSection = formSection.getConfigurationSection("Multipliers");
        for (String attribute : attributeSection.getKeys(false)){
            double attributeMultiplier = attributeSection.getDouble(attribute);
            attributeMultipliers.put(api.getAttributes().find(attribute), attributeMultiplier);
        }
        return attributeMultipliers;
    }

    private ItemStack getItem(String name){
        ConfigurationSection formSection = section.getConfigurationSection(name);
        ConfigurationSection itemSection = formSection.getConfigurationSection("Item");
        String itemId = itemSection.getString("ItemId");
        String[] split = itemId.split(":");
        int id = Integer.parseInt(split[0]);
        int data = Integer.parseInt(split[1]);
        String itemName = itemSection.getString("ItemName").replaceAll("&", "§");
        List<String> itemLore = splitLore(itemSection.getString("ItemLore"));

        ItemBuilder builder = new ItemBuilder(id, 1, (byte)data);
        builder.name(itemName).lore(itemLore);

        ItemStack item = builder.hasEnchant(false).build();
        return item;
    }

    private List<String> splitLore(String loreString){
        List<String> lore = new ArrayList<>();
        String[] split = loreString.split("\\|");
        for(String line : split){
            lore.add(line.replaceAll("&", "§"));
        }
        return lore;
    }

    public void reloadConfig(){
        section = plugin.getFormsConfig().getConfig().getConfigurationSection("Forms");
        registerForms();
    }
}
