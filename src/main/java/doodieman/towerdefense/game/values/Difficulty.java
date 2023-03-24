package doodieman.towerdefense.game.values;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Difficulty {

    EASY("Let", 800, 500, 40,"§e", new ItemStack(Material.GOLD_RECORD)),
    MEDIUM("Medium", 600, 500, 60, "§6", new ItemStack(Material.RECORD_3)),
    HARD("Svær", 400, 500, 80, "§c", new ItemStack(Material.RECORD_4));

    @Getter
    private final String name;
    @Getter
    private final double health;
    @Getter
    private final int rounds;
    @Getter
    private final double startingGold;
    @Getter
    private final String textColor;
    @Getter
    private final ItemStack item;

    Difficulty(String name, double health, double startingGold, int rounds, String textColor, ItemStack item) {
        this.name = name;
        this.health = health;
        this.rounds = rounds;
        this.startingGold = startingGold;
        this.textColor = textColor;
        this.item = item;
    }


}
