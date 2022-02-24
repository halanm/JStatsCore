package com.driga.jstatscore.command;

import com.driga.jstatscore.api.JStatsCoreAPI;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.inventory.SubjectStatsInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("§cVocê não pode executar esse comando");
            return false;
        }
        Player player = (Player) sender;
        Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
        new SubjectStatsInventory(subject).open(player);

        return false;
    }
}
