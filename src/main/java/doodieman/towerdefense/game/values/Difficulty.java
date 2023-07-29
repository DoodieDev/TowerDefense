package doodieman.towerdefense.game.values;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Difficulty {

    EASY("EASY","Let", 200, 250, 40, 1, "§e", new ItemStack(Material.GOLD_RECORD)),
    MEDIUM("MEDIUM","Medium", 150, 250, 60, 1, "§6", new ItemStack(Material.RECORD_3)),
    HARD("HARD","Svær", 100, 250, 80, 1, "§c", new ItemStack(Material.RECORD_4));

    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private final double health;
    @Getter
    private final int rounds;
    @Getter
    private final double startingGold;
    @Getter
    private final double priceModifier;
    @Getter
    private final String textColor;
    @Getter
    private final ItemStack item;

    Difficulty(String id, String name, double health, double startingGold, int rounds, double priceModifier, String textColor, ItemStack item) {
        this.id = id;
        this.name = name;
        this.health = health;
        this.rounds = rounds;
        this.priceModifier = priceModifier;
        this.startingGold = startingGold;
        this.textColor = textColor;
        this.item = item;
    }


}
