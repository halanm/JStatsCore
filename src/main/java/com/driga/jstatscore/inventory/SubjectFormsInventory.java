package com.driga.jstatscore.inventory;

import com.driga.jstatscore.JStatsCore;
import com.driga.jstatscore.api.JStatsCoreAPI;
import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.prototype.Form;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.event.SubjectChangeAttributeLevelEvent;
import com.driga.jstatscore.inventory.item.ItemBuilder;
import com.driga.jstatscore.inventory.sustainer.InventorySustainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SubjectFormsInventory extends InventorySustainer {

    private JStatsCore plugin;
    private Player player;
    private Subject subject;
    private JStatsCoreAPI api;

    public SubjectFormsInventory(Subject subject) {
        super("Suas Formas", MenuSize.SIX_LINES);
        this.plugin = JStatsCore.getInstance();
        this.api = JStatsCoreAPI.getInstance();
        this.subject = subject;
        this.player = subject.getPlayer();

        for(int i = 0; i <= 53; i++){
            this.setItem(i, new ItemBuilder(160, 1, (byte)15).name(" ").lore(""));
        }

        int slot = 0;
        for(String formName : api.getForms().getMap().keySet()){
            Form form = api.getForms().find(formName);
            if(player.hasPermission(form.getPermission())){
                this.setItem(slot, new ItemBuilder(form.getItem()), e ->{
                    player.performCommand("form " + formName);
                    player.closeInventory();
                });
                slot++;
            }
        }
    }
}
