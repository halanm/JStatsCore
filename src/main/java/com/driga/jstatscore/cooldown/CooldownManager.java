package com.driga.jstatscore.cooldown;

import com.driga.jstatscore.api.prototype.Subject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private static CooldownManager cooldownManager;
    private Map<String, Map<UUID,Long>> playerCooldown = new HashMap<String,Map<UUID,Long>>();
    private Map<String, Map<UUID,Long>> cooldownTime = new HashMap<String,Map<UUID,Long>>();

    public static CooldownManager getInstance() {
        if (CooldownManager.cooldownManager == null) {
            CooldownManager.cooldownManager = new CooldownManager();
        }
        return CooldownManager.cooldownManager;
    }

    public void applyCooldown(Subject subject, long seconds, String cd){
        Map<UUID, Long> cooldown = playerCooldown.get(cd);
        if(cooldown == null){
            cooldown = new HashMap<UUID, Long>();
        }
        Map<UUID, Long> cooldownApplied = cooldownTime.get(cd);
        if(cooldownApplied == null){
            cooldownApplied = new HashMap<UUID, Long>();
        }
        long millis = seconds * 1000;
        cooldown.put(subject.getPlayer().getUniqueId(), System.currentTimeMillis() + millis);
        playerCooldown.put(cd, cooldown);
        cooldownApplied.put(subject.getPlayer().getUniqueId(), System.currentTimeMillis());
        cooldownTime.put(cd, cooldownApplied);
    }

    public boolean isOnCooldown(Subject subject, String cd) {
        long current = System.currentTimeMillis();
        long millis = getTime(subject, cd);
        return millis > current;
    }

    public long getTime(Subject subject, String cd) {
        Map<UUID, Long> cooldown = playerCooldown.get(cd);
        if(cooldown == null){
            cooldown = new HashMap<UUID, Long>();
            cooldown.put(subject.getPlayer().getUniqueId(), System.currentTimeMillis());
        }
        if(!cooldown.containsKey(subject.getPlayer().getUniqueId())){
            cooldown.put(subject.getPlayer().getUniqueId(), System.currentTimeMillis());
        }
        return cooldown.get(subject.getPlayer().getUniqueId());
    }

    public String getRemainingTime(long millis) {
        long seconds = millis/1000;
        long minutes = 0;
        while(seconds > 60) {
            seconds-=60;
            minutes++;
        }
        return "§e" + minutes + " Minuto(s) " + seconds + " Segundo(s)";
    }
}
