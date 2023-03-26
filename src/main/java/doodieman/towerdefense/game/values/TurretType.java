package doodieman.towerdefense.game.values;

import doodieman.towerdefense.utils.ItemBuilder;
import jdk.internal.org.jline.utils.DiffHelper.Diff;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum TurretType {

    WOOD_TOWER(
        "Træ Tårn",
        "§6",
        "§fSkyder pile i én givet retning.%nl%§fenten nord, syd øst eller vest.",
        new ItemStack(Material.LOG),
        150
    ),

    STONE_TOWER(
        "Sten Tårn",
        "§7",
        "§fKaster sten i en af fire%nl%§fforskellige retninger, enten%nl%§fnord, syd, øst eller vest.",
        new ItemStack(Material.COBBLESTONE),
        200
    );

    @Getter
    private final String name;
    @Getter
    private final String textColor;
    @Getter
    private final String description;
    private final ItemStack item;
    @Getter
    private final double price;

    TurretType(String name, String textColor, String description, ItemStack item, double price) {
        this.name = name;
        this.textColor = textColor;
        this.description = description;
        this.item = item;
        this.price = price;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public ItemStack getFormattedItem() {
        ItemBuilder builder = new ItemBuilder(this.getItem());
        builder.name(textColor+"§n"+name);
        builder.addLore("");
        builder.addLore(description.split("%nl%"));
        return builder.build();
    }

    public double getRealPrice(Difficulty difficulty) {
        return difficulty.getPriceModifier() * price;
    }

}
