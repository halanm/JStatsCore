package com.driga.jstatscore.util;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MagicSpellsUtil {


    public List<Player> playerList = new ArrayList<>();

    private static MagicSpellsUtil magicSpellsUtil;

    public static MagicSpellsUtil getInstance() {
        if (MagicSpellsUtil.magicSpellsUtil == null) {
            MagicSpellsUtil.magicSpellsUtil = new MagicSpellsUtil();
        }
        return MagicSpellsUtil.magicSpellsUtil;
    }
}
