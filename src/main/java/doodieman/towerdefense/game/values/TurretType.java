package doodieman.towerdefense.game.values;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import doodieman.towerdefense.game.objects.GameTurret;
import doodieman.towerdefense.game.objects.turrets.WoodTower;
import doodieman.towerdefense.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public enum TurretType {

    WOOD_TOWER(
        "woodtower",
        "Træ Tårn",
        "§6",
        "§fSkyder pile i én givet retning.%nl%§fenten nord, syd øst eller vest.",
        new ItemStack(Material.LOG),
        150,
        WoodTower.class
    ),

    STONE_TOWER(
        "stonetower",
        "Sten Tårn",
        "§7",
        "§fKaster sten i en af fire%nl%§fforskellige retninger, enten%nl%§fnord, syd, øst eller vest.",
        new ItemStack(Material.COBBLESTONE),
        200,
        WoodTower.class
    );

    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private final String textColor;
    @Getter
    private final String description;
    private final ItemStack item;
    @Getter
    private final double price;
    @Getter
    private final Class<? extends GameTurret> turretClass;

    private static final Map<String, TurretType> BY_ID = new HashMap<>();

    TurretType(String id, String name, String textColor, String description, ItemStack item, double price, Class<? extends GameTurret> turretClass) {
        this.id = id;
        this.name = name;
        this.textColor = textColor;
        this.description = description;
        this.item = item;
        this.price = price;
        this.turretClass = turretClass;
    }

    //Get the itemstack
    public ItemStack getItem() {
        return item.clone();
    }

    //Get the real itemstack, with name, lore, and NBT formatted
    public ItemStack getFormattedItem() {
        ItemBuilder builder = new ItemBuilder(this.getItem());

        //Set name and lore
        builder.name(textColor+"§n"+name);
        builder.addLore("");
        builder.addLore(description.split("%nl%"));

        //Add NBT
        NBTItem nbtItem = new NBTItem(builder.build());
        NBTCompound compound = nbtItem.addCompound("turret");
        compound.setString("type", id);

        return nbtItem.getItem().clone();
    }

    //Get the price of a turret, in a specific difficulty
    public double getRealPrice(Difficulty difficulty) {
        return difficulty.getPriceModifier() * price;
    }

    //Get a TurretType by its ID
    public static TurretType getByID(String id) {
        return BY_ID.get(id);
    }

    static {
        for (TurretType turret : values())
            BY_ID.put(turret.getId(), turret);
    }

}
