package com.driga.jstatscore.command;

import com.driga.jstatscore.api.JStatsCoreAPI;
import com.driga.jstatscore.api.prototype.Form;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.event.SubjectUseFormEvent;
import com.driga.jstatscore.inventory.SubjectFormsInventory;
import com.driga.jstatscore.inventory.SubjectStatsInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FormCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("§cVocê não pode executar esse comando");
            return false;
        }
        if(args.length == 0){
            Player player = (Player) sender;
            Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
            new SubjectFormsInventory(subject).open(player);
            return false;
        }
        if(args.length > 0){
            Player player = (Player) sender;
            Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
            String formName = args[0];
            if(JStatsCoreAPI.getInstance().getForms().getMap().containsKey(formName)){
                Form form = JStatsCoreAPI.getInstance().getForms().find(formName);
                new SubjectUseFormEvent(subject, form);
                return false;
            }
            player.sendMessage("§cEssa Forma não existe");
            return false;
        }

        return false;
    }
}
