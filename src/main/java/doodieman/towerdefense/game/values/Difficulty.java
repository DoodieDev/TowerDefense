package doodieman.towerdefense.game.values;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Difficulty {

    EASY("Let", 800, "§e", new ItemStack(Material.GOLD_RECORD)),
    MEDIUM("Medium", 600, "§6", new ItemStack(Material.RECORD_3)),
    HARD("Svær", 400, "§c", new ItemStack(Material.RECORD_4));

    @Getter
    private final String name;
    @Getter
    private final double health;
    @Getter
    private final String textColor;
    @Getter
    private final ItemStack item;

    Difficulty(String name, double health, String textColor, ItemStack item) {
        this.name = name;
        this.health = health;
        this.textColor = textColor;
        this.item = item;
    }


}
