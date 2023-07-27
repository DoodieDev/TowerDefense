package doodieman.towerdefense.game.values;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import doodieman.towerdefense.game.objects.GameTurret;
import doodieman.towerdefense.game.objects.turrets.AngelTower;
import doodieman.towerdefense.game.objects.turrets.EyeTower;
import doodieman.towerdefense.game.objects.turrets.FireTower;
import doodieman.towerdefense.game.objects.turrets.LaserTower;
import doodieman.towerdefense.game.objects.turrets.SandTower;
import doodieman.towerdefense.game.objects.turrets.SnowballTower;
import doodieman.towerdefense.game.objects.turrets.StoneTower;
import doodieman.towerdefense.game.objects.turrets.TntTower;
import doodieman.towerdefense.game.objects.turrets.WoodTower;
import doodieman.towerdefense.utils.ItemBuilder;
import doodieman.towerdefense.utils.SkullCreator;
import doodieman.towerdefense.utils.StringUtil;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public enum TurretType {

    WOOD_TOWER(
        "woodtower",
        "Træ Tårn",
        "§6",
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTlkMDhjMGE4ZDAwMWE3ODhlMjYwZDZiYTFmNGYwOGFkYTBlYTcxOWEwZjRlNjExZDZjNGI3YWM4M2JiMDM1ZSJ9fX0="),
        1,
        1,
        6,
        125,
        WoodTower.class
    ),

    STONE_TOWER(
        "stonetower",
        "Sten Tårn",
        "§7",
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2M1ZjNjM2Q1ZDdiMWNlMGYzMjA0MGMxNDU0ZGFmODg3OGUxY2MyOTllMDI1YmFkMmU2OTE5ZWJhZTIxZmJiOCJ9fX0="),
        2.5,
        0.5,
        7,
        175,
        StoneTower.class
    ),

    EYE_TOWER(
        "eyetower",
        "Øje Tårn",
        "§d",
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJhOTY0N2VjN2M4ZjM1OWQ4ZDA5NTJiZGJmNzJjYmI0YjU3NDNjZjg0NTVkY2I3NjY0ZTJiZjliZGY4YjcxOCJ9fX0="),
        0.5,
        2,
        13,
        225,
        EyeTower.class
    ),

    TNT_TOWER(
        "tnttower",
        "TNT Tårn",
        "§4",
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU0MzUyNjgwZDBiYjI5YjkxMzhhZjc4MzMwMWEzOTFiMzQwOTBjYjQ5NDFkNTJjMDg3Y2E3M2M4MDM2Y2I1MSJ9fX0="),
        8,
        1,
        8,
        325,
        TntTower.class
    ),

    FIRE_TOWER(
        "firetower",
        "Ild Tårn",
        "§c",
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZiYjZlZWE1NzU0N2IyNTg4YmFmOGFmNjQ5ZDkxMTZmZjA4Y2FjNTZkZDIxNDBiM2M0OTU3Nzc5OWJhZDdjIn19fQ=="),
        1,
        10,
        8,
        400,
        FireTower.class
    ),

    LASER_TOWER(
        "lasertower",
        "Laser Tårn",
        "§b",
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjNiZTUzZDQ3YzYwNmM2NzhhZDkzYTM5MGRmZDZkMGIxNmI2NmMxYjY1OTA1YzYxNDE3NTRmMDU3MGY2NGJhMyJ9fX0="),
        3,
        5,
        10,
        550,
        LaserTower.class
    ),

    SNOWBALL_TOWER(
        "snowballtower",
        "Snebold Tårn",
        "§f",
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWRmZDc3MjRjNjlhMDI0ZGNmYzYwYjE2ZTAwMzM0YWI1NzM4ZjRhOTJiYWZiOGZiYzc2Y2YxNTMyMmVhMDI5MyJ9fX0="),
        0.3,
        10,
        7,
        425,
        SnowballTower.class
    ),

    SAND_TOWER(
        "sandtower",
        "Sand Tårn",
        "§e",
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGM0MGYzZDliNjg0YmZkYTFkODdjNTAwYmQzZjE0ZDc5ZWY4M2EwZWU3MGUzY2I1MzM5MGRlZGU3Mzc5OWU4NiJ9fX0="),
        6,
        0.8,
        9,
        650,
        SandTower.class
    ),

    ANGEL_TOWER(
        "angeltower",
        "Engel Tårn",
        "§f",
        SkullCreator.itemFromBase64("ewogICJ0aW1lc3RhbXAiIDogMTY4NzE4MjEwMjA1MSwKICAicHJvZmlsZUlkIiA6ICJlZDUzZGQ4MTRmOWQ0YTNjYjRlYjY1MWRjYmE3N2U2NiIsCiAgInByb2ZpbGVOYW1lIiA6ICI0MTQxNDE0MWgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2QxNzZlZWFmNDU5ZGNmOWY5MGMzZjA3NjBjNmE2M2IyMWNiYTA2ZTY4NDUyOTY4ZmFmNWJkOGE5NjUxMGVlMCIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9"),
        7,
        2.5,
        11,
        3275,
        AngelTower.class
    );


    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private final String textColor;
    private final ItemStack item;
    @Getter
    private final double damage;
    @Getter
    private final double attackSpeed;
    @Getter
    private final double range;
    @Getter
    private final double price;
    @Getter
    private final Class<? extends GameTurret> turretClass;

    private static final Map<String, TurretType> BY_ID = new HashMap<>();

    TurretType(
        String id,
        String name,
        String textColor,
        ItemStack item,
        double damage,
        double attackSpeed,
        double range,
        double price,
        Class<? extends GameTurret> turretClass
    ) {
        this.id = id;
        this.name = name;
        this.textColor = textColor;
        this.item = item;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
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
        builder.addLore(
            "",
            "§7Skade: §f"+StringUtil.formatNum(this.damage)+" §4❤",
            "§7Range: §f" + StringUtil.formatNum(this.range)+" blocks",
            "§7Attack speed: §f"+StringUtil.formatNum(this.attackSpeed)
        );

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
