package com.driga.jstatscore.nbt;

import com.driga.jstatscore.api.JStatsCoreAPI;
import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.provider.SubjectProvider;
import com.driga.jstatscore.util.StatsUtils;
import me.dpohvar.powernbt.PowerNBT;
import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTManager;
import org.bukkit.entity.Player;

public class NbtHandler
{
    private static NbtHandler nbtHandler;
    private final NBTManager manager;

    public NbtHandler() {
        manager = PowerNBT.getApi();
    }

    public static NbtHandler getInstance() {
        if (NbtHandler.nbtHandler == null) {
            NbtHandler.nbtHandler = new NbtHandler();
        }
        return NbtHandler.nbtHandler;
    }
    
    public void setValue(Player player, String key, Object value) {
        NBTCompound forgeData = manager.readForgeData(player);
        NBTCompound playerPersisted = forgeData.getCompound("PlayerPersisted");
        if(playerPersisted == null){
            playerPersisted = new NBTCompound();
        }
        playerPersisted.put(key, value);
        forgeData.put("PlayerPersisted", playerPersisted);
        manager.writeForgeData(player, forgeData);
    }

    public void setAllValues(Player player){
        Subject subject = JStatsCoreAPI.getInstance().getSubjects().find(player.getUniqueId());
        for(String attr : subject.getAttributesLevel().keySet()){
            setValue(player, attr, subject.getAttributeLevel(attr));
        }
        setValue(player, "TP", subject.getTrainingPoints());
        setValue(player, "HP_MAX", SubjectProvider.getInstance().getAttributeValue(subject, "CONSTITUTION"));
        setValue(player, "SP_MAX", SubjectProvider.getInstance().getAttributeValue(subject, "ENERGY"));
        setValue(player, "HP_REGEN", subject.getAttributeLevel("CONSTITUTION") * StatsUtils.getInstance().getRegenHP());
        setValue(player, "SP_REGEN", subject.getAttributeLevel("ENERGY") * StatsUtils.getInstance().getRegenSP());
    }
}