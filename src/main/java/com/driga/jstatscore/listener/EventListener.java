package com.driga.jstatscore.listener;

import com.driga.jstatscore.api.JStatsCoreAPI;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.booster.BoosterManager;
import com.driga.jstatscore.factory.SubjectFactory;
import com.driga.jstatscore.provider.SubjectProvider;
import com.driga.jstatscore.util.StatsUtils;
import com.nisovin.magicspells.events.MagicSpellsEntityDamageByEntityEvent;
import com.nisovin.magicspells.events.SpellApplyDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventListener implements Listener {

    private JStatsCoreAPI api;

    public EventListener() {
        api = JStatsCoreAPI.getInstance();
    }

    private List<Player> playerList = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Subject subject = null;
        if (api.getSubjects().find(player.getUniqueId()) == null) {
            subject = SubjectFactory.getInstance().find(player);
            api.getSubjects().put(player.getUniqueId(), subject);
        }else{
            subject = api.getSubjects().find(player.getUniqueId());
        }
        StatsUtils.getInstance().startUpdateStats(subject);
        StatsUtils.getInstance().startRecoverTask(subject);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "modelremove " + player.getName());
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && playerList.contains((Player) e.getDamager())){
            playerList.remove((Player) e.getDamager());
            return;
        }
        if(e.getCause().equals(EntityDamageEvent.DamageCause.CUSTOM)){
            return;
        }
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
            Player damager = (Player) e.getDamager();
            Player vitim = (Player) e.getEntity();
            Subject subjectDamager = api.getSubjects().find(damager.getUniqueId());
            Subject subjectVitim = api.getSubjects().find(vitim.getUniqueId());
            double str = SubjectProvider.getInstance().getAttributeValue(subjectDamager, "STRENGTH");
            double def = SubjectProvider.getInstance().getAttributeValue(subjectVitim, "DEFENSE");
            double dmg = e.getDamage();
            e.setDamage(0);
            dmg = (dmg + str) - def;
            if(dmg < 0){
                dmg = 0;
            }
            double life = subjectVitim.getAttributeLevel("HP") - dmg;
            if(life <= 0){
                vitim.setHealth(0);
                life = SubjectProvider.getInstance().getAttributeValue(subjectVitim, "CONSTITUTION");
            }
            subjectVitim.setAttributeLevel("HP", life);
        }
        if(e.getDamager() instanceof Player && !(e.getEntity() instanceof Player)){
            Player damager = (Player) e.getDamager();
            Subject subjectDamager = api.getSubjects().find(damager.getUniqueId());
            double dmg = e.getDamage();
            e.setDamage(0);
            dmg = dmg + SubjectProvider.getInstance().getAttributeValue(subjectDamager, "STRENGTH");
            e.setDamage(dmg);
        }
        if(!(e.getDamager() instanceof Player) && e.getEntity() instanceof Player){
            Player vitim = (Player) e.getEntity();
            Subject subjectVitim = api.getSubjects().find(vitim.getUniqueId());
            double def = SubjectProvider.getInstance().getAttributeValue(subjectVitim, "DEFENSE");
            double dmg = e.getDamage() - def;
            if(dmg < 0){
                dmg = 0;
            }
            double life = subjectVitim.getAttributeLevel("HP") - dmg;
            e.setDamage(0);
            if(life <= 0){
                vitim.setHealth(0);
                life = SubjectProvider.getInstance().getAttributeValue(subjectVitim, "CONSTITUTION");
            }
            subjectVitim.setAttributeLevel("HP", life);

        }
    }

    @EventHandler
    public void onSpell(SpellApplyDamageEvent e){
        String attr = "STRENGTH";
        if(e.getCause().equals(EntityDamageEvent.DamageCause.MAGIC)){
            attr = "MAGIC_POWER";
        }
        if(e.getTarget() instanceof Player){
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
                playerList.add(e.getCaster());
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
                playerList.add(e.getCaster());
            }
            e.getTarget().setHealth(health);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(!e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) &&
                !e.getCause().equals(EntityDamageEvent.DamageCause.STARVATION)&&
                !e.getCause().equals(EntityDamageEvent.DamageCause.VOID)&&
                !e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)&&
                !e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)){
            if(e.getEntity() instanceof Player){
                Player vitim = (Player) e.getEntity();
                Subject subject = api.getSubjects().find(vitim.getUniqueId());
                double def = SubjectProvider.getInstance().getAttributeValue(subject, "DEFENSE");
                double dmg = (int) (e.getDamage() - def);
                if(dmg < 0){
                    dmg = 0;
                }
                e.setDamage(0);
                double life = subject.getAttributeLevel("HP") - dmg;
                if(life <= 0){
                    vitim.setHealth(0);
                    life = SubjectProvider.getInstance().getAttributeValue(subject, "CONSTITUTION");
                }
                subject.setAttributeLevel("HP", life);
            }
        }
        if(e.getCause().equals(EntityDamageEvent.DamageCause.STARVATION)||
                e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)||
                e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)){
            if(e.getEntity() instanceof Player){
                Player vitim = (Player) e.getEntity();
                Subject subject = api.getSubjects().find(vitim.getUniqueId());
                double life = subject.getAttributeLevel("HP") - 200;
                e.setDamage(0);
                if(life <= 0){
                    vitim.setHealth(0);
                    life = SubjectProvider.getInstance().getAttributeValue(subject, "CONSTITUTION");
                }
                subject.setAttributeLevel("HP", life);
            }
        }
    }
}
