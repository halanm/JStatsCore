package com.driga.jstatscore.command;

import com.driga.jstatscore.JStatsCore;
import com.driga.jstatscore.api.JStatsCoreAPI;
import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.booster.BoosterManager;
import com.driga.jstatscore.event.SubjectChangeAttributeLevelEvent;
import com.driga.jstatscore.event.SubjectReceiveTpEvent;
import com.driga.jstatscore.factory.AttributeFactory;
import com.driga.jstatscore.factory.FormFactory;
import com.driga.jstatscore.util.StatsUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainComand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(!sender.hasPermission("jstatscore.admin")){
            sender.sendMessage("§cVocê não tem permissão para executar esse comando");
            return false;
        }
        if(args.length == 1){
            if(args[0].equals("reload")){
                JStatsCore.getInstance().getStatsConfig().reloadConfig();
                JStatsCore.getInstance().getFormsConfig().reloadConfig();
                AttributeFactory.getInstance().reloadConfig();
                FormFactory.getInstance().reloadConfig();
                StatsUtils.getInstance().reloadConfig();
                BoosterManager.getInstance().reloadConfig();
                sender.sendMessage("§aAção Concluida");
                return false;
            }
            sender.sendMessage(commandList);
            return false;
        }
        if(args.length == 3){
            switch (args[0]) {
                case "tpadd": {
                    Player player = Bukkit.getPlayer(args[1]);
                    double quantity = Integer.parseInt(args[2]);
                    if (player != null && player.isOnline()) {
                        Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                        new SubjectReceiveTpEvent(subject, quantity);
                        sender.sendMessage("§aAção Concluida");
                    } else {
                        sender.sendMessage("§cPlayer offline");
                    }
                    return false;
                }
                case "tpset": {
                    Player player = Bukkit.getPlayer(args[1]);
                    int quantity = Integer.parseInt(args[2]);
                    if (player != null && player.isOnline()) {
                        Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                        double tp = quantity;
                        subject.setTrainingPoints(tp);
                        sender.sendMessage("§aAção Concluida");
                    } else {
                        sender.sendMessage("§cPlayer offline");
                    }
                    return false;
                }
                default:
                    sender.sendMessage(commandList);
                    return false;
            }
        }
        if(args.length == 4){
            if(args[0].equals("statadd")){
                Player player = Bukkit.getPlayer(args[1]);
                String stat = args[2];
                int quantity = Integer.parseInt(args[3]);
                switch (stat.toLowerCase()){
                    case "con":{
                        if(player != null && player.isOnline()){
                            Attribute attribute = JStatsCoreAPI.getInstance().getAttributes().find("CONSTITUTION");
                            Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                            double con = subject.getAttributeLevel("CONSTITUTION") + quantity;
                            if(con <= attribute.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, attribute, con);
                                sender.sendMessage("§aAção Concluida");
                            }else{
                                sender.sendMessage("§cEssa quantia excede o limite");
                            }
                        }else{
                            sender.sendMessage("§cPlayer offline");
                        }
                        return false;
                    }
                    case "str":{
                        if(player != null && player.isOnline()){
                            Attribute attribute = JStatsCoreAPI.getInstance().getAttributes().find("STRENGTH");
                            Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                            double str = subject.getAttributeLevel("STRENGTH") + quantity;
                            if(str <= attribute.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, attribute, str);
                                sender.sendMessage("§aAção Concluida");
                            }else{
                                sender.sendMessage("§cEssa quantia excede o limite");
                            }
                        }else{
                            sender.sendMessage("§cPlayer offline");
                        }
                        return false;
                    }
                    case "def":{
                        if(player != null && player.isOnline()){
                            Attribute attribute = JStatsCoreAPI.getInstance().getAttributes().find("DEFENSE");
                            Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                            double def = subject.getAttributeLevel("DEFENSE") + quantity;
                            if(def <= attribute.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, attribute, def);
                                sender.sendMessage("§aAção Concluida");
                            }else{
                                sender.sendMessage("§cEssa quantia excede o limite");
                            }
                        }else{
                            sender.sendMessage("§cPlayer offline");
                        }
                        return false;
                    }
                    case "energy":{
                        if(player != null && player.isOnline()){
                            Attribute attribute = JStatsCoreAPI.getInstance().getAttributes().find("ENERGY");
                            Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                            double energy = subject.getAttributeLevel("ENERGY") + quantity;
                            if(energy <= attribute.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, attribute, energy);
                                sender.sendMessage("§aAção Concluida");
                            }else{
                                sender.sendMessage("§cEssa quantia excede o limite");
                            }
                        }else{
                            sender.sendMessage("§cPlayer offline");
                        }
                        return false;
                    }
                    case "mp":{
                        if(player != null && player.isOnline()){
                            Attribute attribute = JStatsCoreAPI.getInstance().getAttributes().find("MAGIC_POWER");
                            Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                            double mp = subject.getAttributeLevel("MAGIC_POWER") + quantity;
                            if(mp <= attribute.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, attribute, mp);
                                sender.sendMessage("§aAção Concluida");
                            }else{
                                sender.sendMessage("§cEssa quantia excede o limite");
                            }
                        }else{
                            sender.sendMessage("§cPlayer offline");
                        }
                        return false;
                    }
                    case "all":{
                        if(player != null && player.isOnline()){
                            Attribute CONSTITUTION = JStatsCoreAPI.getInstance().getAttributes().find("CONSTITUTION");
                            Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                            double con = subject.getAttributeLevel("CONSTITUTION") + quantity;
                            if(con <= CONSTITUTION.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, CONSTITUTION, con);
                            }else{
                                sender.sendMessage("§cO Stat con não foi alterado pois essa quantia excede o limite");
                            }
                            Attribute STRENGTH = JStatsCoreAPI.getInstance().getAttributes().find("STRENGTH");
                            double str = subject.getAttributeLevel("STRENGTH") + quantity;
                            if(str <= STRENGTH.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, STRENGTH, str);
                            }else{
                                sender.sendMessage("§cO Stat str não foi alterado pois essa quantia excede o limite");
                            }
                            Attribute DEFENSE = JStatsCoreAPI.getInstance().getAttributes().find("DEFENSE");
                            double def = subject.getAttributeLevel("DEFENSE") + quantity;
                            if(def <= DEFENSE.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, DEFENSE, def);
                            }else{
                                sender.sendMessage("§cO Stat def não foi alterado pois essa quantia excede o limite");
                            }
                            Attribute ENERGY = JStatsCoreAPI.getInstance().getAttributes().find("ENERGY");
                            double energy = subject.getAttributeLevel("ENERGY") + quantity;
                            if(energy <= ENERGY.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, ENERGY, energy);
                            }else{
                                sender.sendMessage("§cO Stat energy não foi alterado pois essa quantia excede o limite");
                            }
                            Attribute MP = JStatsCoreAPI.getInstance().getAttributes().find("MAGIC_POWER");
                            double mp = subject.getAttributeLevel("MAGIC_POWER") + quantity;
                            if(mp <= MP.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, MP, mp);
                            }else{
                                sender.sendMessage("§cO Stat mp não foi alterado pois essa quantia excede o limite");
                            }
                            sender.sendMessage("§aAção Concluida");
                        }else{
                            sender.sendMessage("§cPlayer offline");
                        }
                        return false;
                    }
                    default:{
                        sender.sendMessage("§cEsse stat não existe");
                        return false;
                    }
                }
            }

            if(args[0].equals("statset")){
                Player player = Bukkit.getPlayer(args[1]);
                String stat = args[2];
                int quantity = Integer.parseInt(args[3]);
                switch (stat.toLowerCase()){
                    case "con":{
                        if(player != null && player.isOnline()){
                            Attribute attribute = JStatsCoreAPI.getInstance().getAttributes().find("CONSTITUTION");
                            Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                            double con = quantity;
                            if(con <= attribute.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, attribute, con);
                                sender.sendMessage("§aAção Concluida");
                            }else{
                                sender.sendMessage("§cEssa quantia excede o limite");
                            }
                        }else{
                            sender.sendMessage("§cPlayer offline");
                        }
                        return false;
                    }
                    case "str":{
                        if(player != null && player.isOnline()){
                            Attribute attribute = JStatsCoreAPI.getInstance().getAttributes().find("STRENGTH");
                            Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                            double str = quantity;
                            if(str <= attribute.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, attribute, str);
                                sender.sendMessage("§aAção Concluida");
                            }else{
                                sender.sendMessage("§cEssa quantia excede o limite");
                            }
                        }else{
                            sender.sendMessage("§cPlayer offline");
                        }
                        return false;
                    }
                    case "def":{
                        if(player != null && player.isOnline()){
                            Attribute attribute = JStatsCoreAPI.getInstance().getAttributes().find("DEFENSE");
                            Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                            double def = quantity;
                            if(def <= attribute.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, attribute, def);
                                sender.sendMessage("§aAção Concluida");
                            }else{
                                sender.sendMessage("§cEssa quantia excede o limite");
                            }
                        }else{
                            sender.sendMessage("§cPlayer offline");
                        }
                        return false;
                    }
                    case "energy":{
                        if(player != null && player.isOnline()){
                            Attribute attribute = JStatsCoreAPI.getInstance().getAttributes().find("ENERGY");
                            Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                            double energy = quantity;
                            if(energy <= attribute.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, attribute, energy);
                                sender.sendMessage("§aAção Concluida");
                            }else{
                                sender.sendMessage("§cEssa quantia excede o limite");
                            }
                        }else{
                            sender.sendMessage("§cPlayer offline");
                        }
                        return false;
                    }
                    case "mp":{
                        if(player != null && player.isOnline()){
                            Attribute attribute = JStatsCoreAPI.getInstance().getAttributes().find("MAGIC_POWER");
                            Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                            double mp = quantity;
                            if(mp <= attribute.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, attribute, mp);
                                sender.sendMessage("§aAção Concluida");
                            }else{
                                sender.sendMessage("§cEssa quantia excede o limite");
                            }
                        }else{
                            sender.sendMessage("§cPlayer offline");
                        }
                        return false;
                    }
                    case "all":{
                        if(player != null && player.isOnline()){
                            Attribute CONSTITUTION = JStatsCoreAPI.getInstance().getAttributes().find("CONSTITUTION");
                            Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                            double con = quantity;
                            if(con <= CONSTITUTION.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, CONSTITUTION, con);
                            }else{
                                sender.sendMessage("§cO Stat con não foi alterado pois essa quantia excede o limite");
                            }
                            Attribute STRENGTH = JStatsCoreAPI.getInstance().getAttributes().find("STRENGTH");
                            double str = quantity;
                            if(str <= STRENGTH.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, STRENGTH, str);
                            }else{
                                sender.sendMessage("§cO Stat str não foi alterado pois essa quantia excede o limite");
                            }
                            Attribute DEFENSE = JStatsCoreAPI.getInstance().getAttributes().find("DEFENSE");
                            double def = quantity;
                            if(def <= DEFENSE.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, DEFENSE, def);
                            }else{
                                sender.sendMessage("§cO Stat def não foi alterado pois essa quantia excede o limite");
                            }
                            Attribute ENERGY = JStatsCoreAPI.getInstance().getAttributes().find("ENERGY");
                            double energy = quantity;
                            if(energy <= ENERGY.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, ENERGY, energy);
                            }else{
                                sender.sendMessage("§cO Stat energy não foi alterado pois essa quantia excede o limite");
                            }
                            Attribute MP = JStatsCoreAPI.getInstance().getAttributes().find("MAGIC_POWER");
                            double mp = quantity;
                            if(mp <= MP.getMaxValue()){
                                new SubjectChangeAttributeLevelEvent(subject, MP, mp);
                            }else{
                                sender.sendMessage("§cO Stat mp não foi alterado pois essa quantia excede o limite");
                            }
                            sender.sendMessage("§aAção Concluida");
                        }else{
                            sender.sendMessage("§cPlayer offline");
                        }
                        return false;
                    }
                    default:{
                        sender.sendMessage("§cEsse stat não existe");
                        return false;
                    }
                }
            }
            if(args[0].equals("booster")){
                Player player = Bukkit.getPlayer(args[1]);
                String booster = args[2].toUpperCase();
                long duration = Long.parseLong(args[3]);
                if(player != null && player.isOnline()){
                    if(BoosterManager.getInstance().exists(booster)){
                        Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
                        BoosterManager.getInstance().useBooster(subject, booster, duration);
                    }else{
                        sender.sendMessage("§cEsse booster não existe");
                    }
                }else{
                    sender.sendMessage("§cPlayer offline");
                }
                return false;
            }
        }
        sender.sendMessage(commandList);
        return false;
    }

    private String[] commandList = {
            "§6/jsc reload // Recarrega a config",
            "§6/jsc tpadd <Player> <Quantia> // Adiciona training points ao player",
            "§6/jsc tpset <Player> <Quantia> // Seta training points ao player",
            "§6/jsc statadd <Player> <Stat> <Quantia> // Adiciona stats ao player",
            "§6/jsc statset <Player> <Stat> <Quantia> // Seta stats ao player",
            "§6/jsc booster <Player> <Booster> <Tempo> // Adiciona um booster ao player",
    };
}
