package com.driga.jstatscore.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldUtils;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.driga.jstatscore.JStatsCore;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PacketManager {

    private static PacketManager packetManager;
    private ProtocolManager protocolManager;

    public PacketManager(){
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public static PacketManager getInstance() {
        if (PacketManager.packetManager == null) {
            PacketManager.packetManager = new PacketManager();
        }
        return PacketManager.packetManager;
    }

    public void registerHearthRemoveListener(){
        protocolManager.addPacketListener(new PacketAdapter(JStatsCore.getInstance(), ListenerPriority.HIGH, PacketType.Play.Server.WORLD_PARTICLES) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                if (event.getPacketType() == PacketType.Play.Server.WORLD_PARTICLES) {
                    for(EnumWrappers.Particle i : event.getPacket().getParticles().getValues()){
                        if(i.getId() == 44){
                            event.setCancelled(true);
                        }
                    }
                }
            }
        });
    }
}
