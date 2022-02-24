package com.driga.jstatscore.inventory;

import com.driga.jstatscore.JStatsCore;
import com.driga.jstatscore.api.JStatsCoreAPI;
import com.driga.jstatscore.api.prototype.Attribute;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.event.SubjectChangeAttributeLevelEvent;
import com.driga.jstatscore.form.FormManager;
import com.driga.jstatscore.inventory.item.ItemBuilder;
import com.driga.jstatscore.inventory.sustainer.InventorySustainer;
import com.driga.jstatscore.prototype.JSubject;
import com.driga.jstatscore.provider.SubjectProvider;
import com.driga.jstatscore.util.StatsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SubjectStatsInventory extends InventorySustainer {

    private JStatsCore plugin;
    private Player player;
    private Subject subject;
    private JStatsCoreAPI api;
    private Double quantity;

    public SubjectStatsInventory(Subject subject) {
        super("Atributos", MenuSize.THREE_LINES);
        this.plugin = JStatsCore.getInstance();
        this.api = JStatsCoreAPI.getInstance();
        this.subject = subject;
        this.player = subject.getPlayer();
        this.quantity = 1.0;

        render();
    }

    private void render(){
        for(int i = 0; i <= 26; i++){
            setItem(i, new ItemBuilder(160, 1, (byte)15).name(" ").lore(""));
        }
        setItem(3, new ItemBuilder(Material.BOOK).name("§6Seu TP").lore(
        "§bVocê tem §a" + String.format("%.0f", subject.getTrainingPoints()) + " §bTP",
        "§6Multiplicador: §b" + String.format("%.0f", quantity)).hasEnchant(false), e ->{
            setQuantity();
            updateItems();
        });

        String form = "§cVocê não está usando nenhuma Forma";
        if(FormManager.getInstance().isUsing(subject)){
            String formName = FormManager.getInstance().getUsingForm(subject).getName();
            form = "§aVocê está usando a Forma: " + formName;
        }

        Attribute STRENGTH = api.getAttributes().find("STRENGTH");
        Attribute MAGIC_POWER = api.getAttributes().find("MAGIC_POWER");
        Attribute ENERGY = api.getAttributes().find("ENERGY");
        Attribute CONSTITUTION = api.getAttributes().find("CONSTITUTION");
        Attribute DEFENSE = api.getAttributes().find("DEFENSE");
        setItem(5, new ItemBuilder(Material.SKULL_ITEM, 1, (short)3).skullOwner(player.getName())
        .name("§4Informações").lore(form, "",
        "§cDano: " + "§6" + String.format("%.0f", SubjectProvider.getInstance().getAttributeValue(subject, "STRENGTH")),
        MAGIC_POWER.getName() + " - Dano: " + "§6" + String.format("%.0f", SubjectProvider.getInstance().getAttributeValue(subject, "MAGIC_POWER")),
        ENERGY.getName() + ": " + "§6" + String.format("%.0f", SubjectProvider.getInstance().getAttributeValue(subject, "ENERGY")),
        "§aVida: " + "§6" + String.format("%.0f", SubjectProvider.getInstance().getAttributeValue(subject, "CONSTITUTION")),
        "§bDefesa: " + "§6" + String.format("%.0f", SubjectProvider.getInstance().getAttributeValue(subject, "DEFENSE")),
        "§2Regeneração - §aVida: " + "§6" + String.format("%.0f", StatsUtils.getInstance().getRegenHP()),
        "§2Regeneração - " + ENERGY.getName() + ": " + "§6" + String.format("%.0f", StatsUtils.getInstance().getRegenHP()))
        .hasEnchant(false),
                e ->{});

        setItem(11, new ItemBuilder(Material.DIAMOND_SWORD).name(STRENGTH.getName()).lore(
                "§bLevel: §a" + String.format("%.0f", subject.getAttributeLevel("STRENGTH")),
                "§bCusto: §a" + String.format("%.0f", (subject.getAttributeLevel("STRENGTH") * STRENGTH.getTpCostMultiplier()) * quantity) + " TP",
                "§fClique para Melhorar " + STRENGTH.getName()).hasEnchant(false),
                e ->{
                    if(subject.getTrainingPoints() < (subject.getAttributeLevel("STRENGTH") * STRENGTH.getTpCostMultiplier()) * quantity){
                        player.sendMessage("§cVocê não tem TP Suficiente");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 0.1f);
                        player.closeInventory();
                    }else{
                        if((subject.getAttributeLevel("STRENGTH") + quantity) <= STRENGTH.getMaxValue()){
                            String subjectName = subject.getName();
                            subject.removeTrainingPoints((subject.getAttributeLevel("STRENGTH") * STRENGTH.getTpCostMultiplier()) * quantity);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 2.0f);
                            new SubjectChangeAttributeLevelEvent(subject, STRENGTH, subject.getAttributeLevel("STRENGTH") + quantity);
                            updateItems();
                        }else{
                            player.sendMessage("§cEsse Atributo já está no Level Máximo");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 0.1f);
                            player.closeInventory();
                        }
                    }
                });

        setItem(12, new ItemBuilder(Material.NETHER_STAR).name(MAGIC_POWER.getName()).lore(
                "§bLevel: §a" + String.format("%.0f", subject.getAttributeLevel("MAGIC_POWER")),
                "§bCusto: §a" + String.format("%.0f", (subject.getAttributeLevel("MAGIC_POWER") * MAGIC_POWER.getTpCostMultiplier())* quantity) + " TP",
                "§fClique para Melhorar " + MAGIC_POWER.getName()).hasEnchant(false),
                e ->{
                    if(subject.getTrainingPoints() < (subject.getAttributeLevel("MAGIC_POWER") * MAGIC_POWER.getTpCostMultiplier())* quantity){
                        player.sendMessage("§cVocê não tem TP Suficiente");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 0.1f);
                        player.closeInventory();
                    }else{
                        if((subject.getAttributeLevel("MAGIC_POWER") + quantity) <= MAGIC_POWER.getMaxValue()){
                            String subjectName = subject.getName();
                            subject.removeTrainingPoints((subject.getAttributeLevel("MAGIC_POWER") * MAGIC_POWER.getTpCostMultiplier())* quantity);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 2.0f);
                            new SubjectChangeAttributeLevelEvent(subject, MAGIC_POWER, subject.getAttributeLevel("MAGIC_POWER") + quantity);
                            updateItems();
                        }else{
                            player.sendMessage("§cEsse Atributo já está no Level Máximo");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 0.1f);
                            player.closeInventory();
                        }
                    }
                });

        setItem(13, new ItemBuilder(Material.DRAGONS_BREATH).name(ENERGY.getName()).lore(
                "§bLevel: §a" + String.format("%.0f", subject.getAttributeLevel("ENERGY")),
                "§bCusto: §a" + String.format("%.0f", (subject.getAttributeLevel("ENERGY") * MAGIC_POWER.getTpCostMultiplier())* quantity) + " TP",
                "§fClique para Melhorar " + ENERGY.getName()).hasEnchant(false),
                e ->{
                    if(subject.getTrainingPoints() < (subject.getAttributeLevel("ENERGY") * MAGIC_POWER.getTpCostMultiplier())* quantity){
                        player.sendMessage("§cVocê não tem TP Suficiente");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 0.1f);
                        player.closeInventory();
                    }else{
                        if((subject.getAttributeLevel("ENERGY") + quantity) <= ENERGY.getMaxValue()){
                            String subjectName = subject.getName();
                            subject.removeTrainingPoints((subject.getAttributeLevel("ENERGY") * MAGIC_POWER.getTpCostMultiplier())* quantity);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 2.0f);
                            new SubjectChangeAttributeLevelEvent(subject, ENERGY, subject.getAttributeLevel("ENERGY") + quantity);
                            updateItems();
                        }else{
                            player.sendMessage("§cEsse Atributo já está no Level Máximo");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 0.1f);
                            player.closeInventory();
                        }
                    }
                });

        setItem(14, new ItemBuilder(Material.REDSTONE).name(CONSTITUTION.getName()).lore(
                "§bLevel: §a" + String.format("%.0f", subject.getAttributeLevel("CONSTITUTION")),
                "§bCusto: §a" + String.format("%.0f", (subject.getAttributeLevel("CONSTITUTION") * MAGIC_POWER.getTpCostMultiplier())* quantity) + " TP",
                "§fClique para Melhorar " + CONSTITUTION.getName()).hasEnchant(false),
                e ->{
                    if(subject.getTrainingPoints() < (subject.getAttributeLevel("CONSTITUTION") * MAGIC_POWER.getTpCostMultiplier())* quantity){
                        player.sendMessage("§cVocê não tem TP Suficiente");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 0.1f);
                        player.closeInventory();
                    }else{
                        if((subject.getAttributeLevel("CONSTITUTION") + quantity) <= CONSTITUTION.getMaxValue()){
                            String subjectName = subject.getName();
                            subject.removeTrainingPoints((subject.getAttributeLevel("CONSTITUTION") * MAGIC_POWER.getTpCostMultiplier())* quantity);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 2.0f);
                            new SubjectChangeAttributeLevelEvent(subject, CONSTITUTION, subject.getAttributeLevel("CONSTITUTION") + quantity);
                            updateItems();
                        }else{
                            player.sendMessage("§cEsse Atributo já está no Level Máximo");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 0.1f);
                            player.closeInventory();
                        }
                    }
                });

        setItem(15, new ItemBuilder(Material.SHIELD).name(DEFENSE.getName()).lore(
                "§bLevel: §a" + String.format("%.0f", subject.getAttributeLevel("DEFENSE")),
                "§bCusto: §a" + String.format("%.0f", (subject.getAttributeLevel("DEFENSE") * MAGIC_POWER.getTpCostMultiplier())* quantity) + " TP",
                "§fClique para Melhorar " + DEFENSE.getName()).hasEnchant(false),
                e ->{
                    if(subject.getTrainingPoints() < (subject.getAttributeLevel("DEFENSE") * MAGIC_POWER.getTpCostMultiplier())* quantity){
                        player.sendMessage("§cVocê não tem TP Suficiente");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 0.1f);
                        player.closeInventory();
                    }else{
                        if((subject.getAttributeLevel("DEFENSE") + quantity) <= DEFENSE.getMaxValue()){
                            String subjectName = subject.getName();
                            subject.removeTrainingPoints((subject.getAttributeLevel("DEFENSE") * MAGIC_POWER.getTpCostMultiplier())* quantity);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 2.0f);
                            new SubjectChangeAttributeLevelEvent(subject, DEFENSE, subject.getAttributeLevel("DEFENSE") + quantity);
                            updateItems();
                        }else{
                            player.sendMessage("§cEsse Atributo já está no Level Máximo");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.1f, 0.1f);
                            player.closeInventory();
                        }
                    }
                });
    }

    private void setQuantity(){
        quantity = quantity * 10.0;
        if(quantity > 100.0){
            quantity = 1.0;
        }
    }

    private void updateItems(){

        String form = "§cVocê não está usando nenhuma Forma";
        if(FormManager.getInstance().isUsing(subject)){
            String formName = FormManager.getInstance().getUsingForm(subject).getName();
            form = "§aVocê está usando a Forma: " + formName;
        }

        Attribute STRENGTH = api.getAttributes().find("STRENGTH");
        Attribute MAGIC_POWER = api.getAttributes().find("MAGIC_POWER");
        Attribute ENERGY = api.getAttributes().find("ENERGY");
        Attribute CONSTITUTION = api.getAttributes().find("CONSTITUTION");
        Attribute DEFENSE = api.getAttributes().find("DEFENSE");

        player.getOpenInventory().getTopInventory().setItem(3,
                new ItemBuilder(Material.BOOK).name("§6Seu TP").lore(
                        "§bVocê tem §a" + String.format("%.0f", subject.getTrainingPoints()) + " §bTP",
                        "§6Multiplicador: §b" + String.format("%.0f", quantity)).hasEnchant(false).build());

        player.getOpenInventory().getTopInventory().setItem(5,
                new ItemBuilder(Material.SKULL_ITEM, 1, (short)3).skullOwner(player.getName())
                        .name("§4Informações").lore(form, "",
                        "§cDano: " + "§6" + String.format("%.0f", SubjectProvider.getInstance().getAttributeValue(subject, "STRENGTH")),
                        MAGIC_POWER.getName() + " - Dano: " + "§6" + String.format("%.0f", SubjectProvider.getInstance().getAttributeValue(subject, "MAGIC_POWER")),
                        ENERGY.getName() + ": " + "§6" + String.format("%.0f", SubjectProvider.getInstance().getAttributeValue(subject, "ENERGY")),
                        "§aVida: " + "§6" + String.format("%.0f", SubjectProvider.getInstance().getAttributeValue(subject, "CONSTITUTION")),
                        "§bDefesa: " + "§6" + String.format("%.0f", SubjectProvider.getInstance().getAttributeValue(subject, "DEFENSE")),
                        "§2Regeneração - §aVida: " + "§6" + String.format("%.0f", StatsUtils.getInstance().getRegenHP()),
                        "§2Regeneração - " + ENERGY.getName() + ": " + "§6" + String.format("%.0f", StatsUtils.getInstance().getRegenHP()))
                        .hasEnchant(false).build());

        player.getOpenInventory().getTopInventory().setItem(11,
                new ItemBuilder(Material.DIAMOND_SWORD).name(STRENGTH.getName()).lore(
                        "§bLevel: §a" + String.format("%.0f", subject.getAttributeLevel("STRENGTH")),
                        "§bCusto: §a" + String.format("%.0f", (subject.getAttributeLevel("STRENGTH") * STRENGTH.getTpCostMultiplier()) * quantity) + " TP",
                        "§fClique para Melhorar " + STRENGTH.getName()).hasEnchant(false).build());

        player.getOpenInventory().getTopInventory().setItem(12,
                new ItemBuilder(Material.NETHER_STAR).name(MAGIC_POWER.getName()).lore(
                        "§bLevel: §a" + String.format("%.0f", subject.getAttributeLevel("MAGIC_POWER")),
                        "§bCusto: §a" + String.format("%.0f", (subject.getAttributeLevel("MAGIC_POWER") * MAGIC_POWER.getTpCostMultiplier()) * quantity) + " TP",
                        "§fClique para Melhorar " + MAGIC_POWER.getName()).hasEnchant(false).build());

        player.getOpenInventory().getTopInventory().setItem(13,
                new ItemBuilder(Material.DRAGONS_BREATH).name(ENERGY.getName()).lore(
                        "§bLevel: §a" + String.format("%.0f", subject.getAttributeLevel("ENERGY")),
                        "§bCusto: §a" + String.format("%.0f", (subject.getAttributeLevel("ENERGY") * ENERGY.getTpCostMultiplier()) * quantity) + " TP",
                        "§fClique para Melhorar " + ENERGY.getName()).hasEnchant(false).build());

        player.getOpenInventory().getTopInventory().setItem(14,
                new ItemBuilder(Material.REDSTONE).name(CONSTITUTION.getName()).lore(
                        "§bLevel: §a" + String.format("%.0f", subject.getAttributeLevel("CONSTITUTION")),
                        "§bCusto: §a" + String.format("%.0f", (subject.getAttributeLevel("CONSTITUTION") * CONSTITUTION.getTpCostMultiplier()) * quantity) + " TP",
                        "§fClique para Melhorar " + CONSTITUTION.getName()).hasEnchant(false).build());

        player.getOpenInventory().getTopInventory().setItem(15,
                new ItemBuilder(Material.SHIELD).name(DEFENSE.getName()).lore(
                        "§bLevel: §a" + String.format("%.0f", subject.getAttributeLevel("DEFENSE")),
                        "§bCusto: §a" + String.format("%.0f", (subject.getAttributeLevel("DEFENSE") * DEFENSE.getTpCostMultiplier()) * quantity) + " TP",
                        "§fClique para Melhorar " + DEFENSE.getName()).hasEnchant(false).build());
    }
}
