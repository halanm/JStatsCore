package com.driga.jstatscore.listener;

import com.driga.jstatscore.JStatsCore;
import com.driga.jstatscore.api.JStatsCoreAPI;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.booster.BoosterManager;
import com.driga.jstatscore.factory.SubjectFactory;
import com.driga.jstatscore.nbt.NbtHandler;
import com.driga.jstatscore.provider.SubjectProvider;
import com.driga.jstatscore.util.MagicSpellsUtil;
import com.driga.jstatscore.util.StatsUtils;
import com.nisovin.magicspells.events.MagicSpellsEntityDamageByEntityEvent;
import com.nisovin.magicspells.events.SpellApplyDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventListener implements Listener {

    private JStatsCoreAPI api;
    private List<String> preventDefault = new ArrayList<>();

    public EventListener() {
        api = JStatsCoreAPI.getInstance();
    }

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
        StatsUtils.getInstance().startRecoverTask(subject);
        StatsUtils.getInstance().startSpeedTask(subject);

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "modelapply none " + player.getName());
            }
        }.runTaskLater(JStatsCore.getInstance(), 10);
        NbtHandler.getInstance().setAllValues(player);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        boolean isSkill = false;
        if(e.getDamager() instanceof Player && MagicSpellsUtil.getInstance().playerList.contains((Player) e.getDamager())){
            MagicSpellsUtil.getInstance().playerList.remove((Player) e.getDamager());
            return;
        }
        if(e.getCause().equals(EntityDamageEvent.DamageCause.CUSTOM)){
            isSkill = true;
        }
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player && ((Player) e.getEntity()).getGameMode() == GameMode.SURVIVAL && !e.getEntity().isDead()){
            Player damager = (Player) e.getDamager();
            Player vitim = (Player) e.getEntity();
            Subject subjectDamager = api.getSubjects().find(damager.getUniqueId());
            Subject subjectVitim = api.getSubjects().find(vitim.getUniqueId());
            double str = SubjectProvider.getInstance().getAttributeValue(subjectDamager, "STRENGTH");
            double def = SubjectProvider.getInstance().getAttributeValue(subjectVitim, "DEFENSE");
            double dmg = e.getDamage();
            e.setDamage(0);
            if(isSkill){
                LivingEntity entity = vitim;
                entity.damage(1, damager);
                vitim.setHealth(20);
                dmg = dmg - def;
            }else{
                dmg = (dmg + str) - def;
            }
            if(dmg < 0){
                dmg = 0;
            }
            double life = subjectVitim.getAttributeLevel("HP") - dmg;
            if(life <= 0){
                if(isSkill){
                    vitim.setHealth(0.0);
                }
                e.setDamage(99999);
                life = SubjectProvider.getInstance().getAttributeValue(subjectVitim, "CONSTITUTION");
            }
            subjectVitim.setAttributeLevel("HP", life);
        }
        if(e.getDamager() instanceof Player && !(e.getEntity() instanceof Player)){
            Player damager = (Player) e.getDamager();
            if(preventDefault.contains(damager.getName())){
                preventDefault.remove(damager.getName());
                return;
            }
            Subject subjectDamager = api.getSubjects().find(damager.getUniqueId());
            double dmg = e.getDamage();
            e.setDamage(0);
            if(isSkill){
                Damageable damageable = (Damageable) e.getEntity();
                preventDefault.add(damager.getName());
                damageable.damage(dmg, damager);
            }else{
                dmg = dmg + SubjectProvider.getInstance().getAttributeValue(subjectDamager, "STRENGTH");
                e.setDamage(dmg);
            }
        }
        if(!(e.getDamager() instanceof Player) && e.getEntity() instanceof Player && ((Player) e.getEntity()).getGameMode() == GameMode.SURVIVAL && !e.getEntity().isDead()){
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
                e.setDamage(99999);
                life = SubjectProvider.getInstance().getAttributeValue(subjectVitim, "CONSTITUTION");
            }
            subjectVitim.setAttributeLevel("HP", life);

        }
        if(e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player && e.getEntity() instanceof Player
                && ((Player) e.getEntity()).getGameMode() == GameMode.SURVIVAL && !e.getEntity().isDead()){
            Player damager = (Player) ((Projectile) e.getDamager()).getShooter();
            Player vitim = (Player) e.getEntity();
            Subject subjectDamager = api.getSubjects().find(damager.getUniqueId());
            Subject subjectVitim = api.getSubjects().find(vitim.getUniqueId());
            double def = SubjectProvider.getInstance().getAttributeValue(subjectVitim, "DEFENSE");
            double dmg = subjectDamager.getAttributeLevel("STRENGTH") * api.getAttributes().find("DEFENSE").getMultiplier();
            dmg = dmg - def;
            if(dmg < 0){
                dmg = 0;
            }
            double life = subjectVitim.getAttributeLevel("HP") - dmg;
            if(life <= 0){
                e.setDamage(99999);
                life = SubjectProvider.getInstance().getAttributeValue(subjectVitim, "CONSTITUTION");
            }
            subjectVitim.setAttributeLevel("HP", life);
        }
        if(e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player && !(e.getEntity() instanceof Player)){
            Player damager = (Player) ((Projectile) e.getDamager()).getShooter();
            Subject subjectDamager = api.getSubjects().find(damager.getUniqueId());
            double dmg = subjectDamager.getAttributeLevel("STRENGTH") * api.getAttributes().find("DEFENSE").getMultiplier();
            Damageable damageable = (Damageable) e.getEntity();
            double health = damageable.getHealth();
            health -= dmg;
            if(health < 0){
                health = 0;
            }
            damageable.setHealth(health);
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
                    e.setDamage(vitim.getHealth());
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
                    e.setDamage(vitim.getHealth());
                    life = SubjectProvider.getInstance().getAttributeValue(subject, "CONSTITUTION");
                }
                subject.setAttributeLevel("HP", life);
            }
        }
    }
}
