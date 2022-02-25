package com.driga.jstatscore.form;

import com.driga.jstatscore.JStatsCore;
import com.driga.jstatscore.api.prototype.Form;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.cooldown.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class FormManager {

    private static FormManager formManager;
    private JStatsCore plugin;
    private Map<Subject, Form> formMap;
    private Map<Subject, Long> durationMap;

    public static FormManager getInstance() {
        if (FormManager.formManager == null) {
            FormManager.formManager = new FormManager();
        }
        return FormManager.formManager;
    }

    public FormManager() {
        this.plugin = JStatsCore.getInstance();
        this.formMap = new HashMap<>();
        this.durationMap = new HashMap<>();
    }

    public void useForm(Subject subject, Form form){
        int duration = form.getDuration() * 1000;
        int cooldown = form.getCooldown();

        long millis = System.currentTimeMillis() + duration;

        formMap.put(subject, form);
        durationMap.put(subject, millis);
        CooldownManager.getInstance().applyCooldown(subject, cooldown, form.getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "modelapply " + form.getModel() + " " + subject.getPlayer().getName());
    }

    public Form getUsingForm(Subject subject){
        if(formMap.containsKey(subject)){
            return formMap.get(subject);
        }
        return null;
    }

    public boolean isUsing(Subject subject){
        if(formMap.containsKey(subject)){
            return true;
        }
        return false;
    }

    public void startTask(){
        new BukkitRunnable(){
            @Override
            public void run() {
                for(Subject subject : durationMap.keySet()){
                    long current = System.currentTimeMillis();
                    long millis = durationMap.get(subject);
                    if(millis <= current){
                        if(subject.getPlayer() != null && subject.getPlayer().isOnline()){
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "modelapply none " + subject.getPlayer().getName());
                        }
                        formMap.remove(subject);
                        durationMap.remove(subject);
                    }else{
                        takeEnergy(subject);
                    }
                }
            }
        }.runTaskTimer(JStatsCore.getInstance(), 20, 20);
    }

    private void takeEnergy(Subject subject){
        if(subject.getPlayer() != null && subject.getPlayer().isOnline()){
            Form form = formMap.get(subject);
            if(subject.getAttributeLevel("SP") >= form.getEnergyCost()){
                double current = subject.getAttributeLevel("SP");
                double now = current - form.getEnergyCost();
                subject.setAttributeLevel("SP", now);
            }else{
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "modelapply none " + subject.getPlayer().getName());
                formMap.remove(subject);
                durationMap.remove(subject);
            }
        }
    }

}
