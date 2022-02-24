package com.driga.jstatscore.listener;

import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.prototype.Form;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.booster.BoosterManager;
import com.driga.jstatscore.cooldown.CooldownManager;
import com.driga.jstatscore.event.SubjectChangeAttributeLevelEvent;
import com.driga.jstatscore.event.SubjectReceiveTpEvent;
import com.driga.jstatscore.event.SubjectUseFormEvent;
import com.driga.jstatscore.factory.AttributeFactory;
import com.driga.jstatscore.form.FormManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CustomEventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChangeAttribute(SubjectChangeAttributeLevelEvent event){
        if(event.isCancelled()){
            return;
        }
        Subject subject = event.getSubject();
        Player player = subject.getPlayer();
        Attribute attribute = event.getAttribute();
        Double level = event.getLevel();

        subject.setAttributeLevel(AttributeFactory.getInstance().getByAttribute(attribute), level);
        player.sendMessage("§aSeu Nivel de " + attribute.getName() + " §amudou para " + String.format("%.0f", level));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onReceiveTp(SubjectReceiveTpEvent event){
        if(event.isCancelled()){
            return;
        }
        Subject subject = event.getSubject();
        Player player = subject.getPlayer();
        Double value = event.getValue();

        if(BoosterManager.getInstance().hasBooster(subject)){
            Double multiplier = BoosterManager.getInstance().getBoostersTotal(subject);
            value = value * multiplier;
        }

        subject.addTrainingPoints(value);
        player.sendMessage("§aVocê recebeu " + String.format("%.0f", value) + " de TP");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUseForm(SubjectUseFormEvent event){
        if(event.isCancelled()){
            return;
        }
        Subject subject = event.getSubject();
        Player player = subject.getPlayer();
        Form form = event.getForm();

        if(CooldownManager.getInstance().isOnCooldown(subject, form.getName())){
            long current = System.currentTimeMillis();
            long release = CooldownManager.getInstance().getTime(subject, form.getName());
            long millis = release - current;
            String cd = CooldownManager.getInstance().getRemainingTime(millis);

            player.sendMessage("§cVocê só pode usar essa Forma depois de:");
            player.sendMessage(cd);
            return;
        }

        if(FormManager.getInstance().isUsing(subject)){
            player.sendMessage("§cVocê ja esta usando uma Forma");
            return;
        }

        FormManager.getInstance().useForm(subject, form);
        player.sendMessage("§aVocê usou a forma " + form.getName());
    }
}
