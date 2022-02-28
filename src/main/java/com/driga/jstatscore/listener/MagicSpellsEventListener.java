package com.driga.jstatscore.listener;

import com.driga.jstatscore.api.JStatsCoreAPI;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.provider.SubjectProvider;
import com.driga.jstatscore.util.MagicSpellsUtil;
import com.nisovin.magicspells.events.SpellApplyDamageEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class MagicSpellsEventListener implements Listener {

    private JStatsCoreAPI api;

    public MagicSpellsEventListener() {
        api = JStatsCoreAPI.getInstance();
    }

    @EventHandler
    public void onSpell(SpellApplyDamageEvent e){
        String attr = "STRENGTH";
        if(e.getCause().equals(EntityDamageEvent.DamageCause.MAGIC)){
            attr = "MAGIC_POWER";
        }
        if(e.getTarget() instanceof Player && ((Player) e.getTarget()).getGameMode() == GameMode.SURVIVAL){
            Player damager = (Player) e.getCaster();
            Player vitim = (Player) e.getTarget();
            Subject subjectDamager = api.getSubjects().find(damager.getUniqueId());
            Subject subjectVitim = api.getSubjects().find(vitim.getUniqueId());
            double str = SubjectProvider.getInstance().getAttributeValue(subjectDamager, attr);
            double def = SubjectProvider.getInstance().getAttributeValue(subjectVitim, "DEFENSE");
            double dmg = e.getDamage();
            dmg = (dmg + str) - def;
            if(dmg < 0){
                dmg = 0;
            }else{
                MagicSpellsUtil.getInstance().playerList.add(e.getCaster());
            }
            double life = subjectVitim.getAttributeLevel("HP") - dmg;
            life = life + e.getDamage();
            if(life <= 0){
                vitim.setHealth(0);
                life = SubjectProvider.getInstance().getAttributeValue(subjectVitim, "CONSTITUTION");
            }
            subjectVitim.setAttributeLevel("HP", life);
        }
        if(!(e.getTarget() instanceof Player)){
            Player damager = (Player) e.getCaster();
            Subject subjectDamager = api.getSubjects().find(damager.getUniqueId());
            double dmg = e.getDamage();
            double health = e.getTarget().getHealth() + dmg;
            dmg = dmg * SubjectProvider.getInstance().getAttributeValue(subjectDamager, attr);
            health = health - dmg;
            if(health < 0){
                health = 0;
            }else{
                MagicSpellsUtil.getInstance().playerList.add(e.getCaster());
            }
            e.getTarget().setHealth(health);
        }
    }
}
