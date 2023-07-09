package doodieman.towerdefense.game.values;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import doodieman.towerdefense.game.objects.GameTurret;
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
        150,
        WoodTower.class
    ),

    STONE_TOWER(
        "stonetower",
        "Sten Tårn",
        "§7",
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWU1NGFiYmM2NWIxM2E0MmMyOTU5MGEwY2Y5ZDNlMDA3MDJkMWU2MGQ5NzRmOTI4NmE3YzE3MjY3ZjIyODJjOSJ9fX0="),
        2,
        1.5,
        8,
        200,
        WoodTower.class
    ),

    EYE_TOWER(
        "eyetower",
        "Øje Tårn",
        "§3",
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODcyZDM0MWQ3N2RiZGU2ZDUzZGFkNjFiZjE5MjUyNGRiZGI5NmFmMTM1OGUwNzQ4ZmVlYTE0ODFiMWY4In19fQ=="),
        2,
        1,
        14,
        350,
        WoodTower.class
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
        WoodTower.class
    ),

    FIRE_TOWER(
        "firetower",
        "Ild Tårn",
        "§c",
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDE3YjhiNDNmOGM0YjVjZmViOTE5YzlmOGZlOTNmMjZjZWI2ZDJiMTMzYzJhYjFlYjMzOWJkNjYyMWZkMzA5YyJ9fX0="),
        1,
        10,
        8,
        400,
        WoodTower.class
    ),

    LASER_TOWER(
        "lasertower",
        "Laser Tårn",
        "§b",
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWNiN2MyMWNjNDNkYzE3Njc4ZWU2ZjE2NTkxZmZhYWIxZjYzN2MzN2Y0ZjZiYmQ4Y2VhNDk3NDUxZDc2ZGI2ZCJ9fX0="),
        3,
        5,
        10,
        550,
        WoodTower.class
    ),

    SNOWBALL_TOWER(
        "snowballtower",
        "Snebold Tårn",
        "§f",
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTExNWM3OTY4ZWMzNzcxZWU5ZmY2YWU2YmNhMmQ1YmEzOTYyYWE3MjdhNGZhOGQzNzYwOGU0YzliZjE1MTJiYiJ9fX0="),
        1,
        10,
        8,
        850,
        WoodTower.class
    ),

    SAND_TOWER(
        "sandtower",
        "Sand Tårn",
        "§e",
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGM0MGYzZDliNjg0YmZkYTFkODdjNTAwYmQzZjE0ZDc5ZWY4M2EwZWU3MGUzY2I1MzM5MGRlZGU3Mzc5OWU4NiJ9fX0="),
        5,
        3,
        9,
        950,
        WoodTower.class
    ),

    ANGEL_TOWER(
        "angeltower",
        "Engel Tårn",
        "§f",
        SkullCreator.itemFromBase64("ewogICJ0aW1lc3RhbXAiIDogMTY4NzE4MjEwMjA1MSwKICAicHJvZmlsZUlkIiA6ICJlZDUzZGQ4MTRmOWQ0YTNjYjRlYjY1MWRjYmE3N2U2NiIsCiAgInByb2ZpbGVOYW1lIiA6ICI0MTQxNDE0MWgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2QxNzZlZWFmNDU5ZGNmOWY5MGMzZjA3NjBjNmE2M2IyMWNiYTA2ZTY4NDUyOTY4ZmFmNWJkOGE5NjUxMGVlMCIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9"),
        10,
        5,
        12,
        3750,
        WoodTower.class
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
    private final double shotsPerSecond;
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
        double shotsPerSecond,
        double range,
        double price,
        Class<? extends GameTurret> turretClass
    ) {
        this.id = id;
        this.name = name;
        this.textColor = textColor;
        this.item = item;
        this.damage = damage;
        this.shotsPerSecond = shotsPerSecond;
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
            "§7DPS: §f" + StringUtil.formatNum(this.damage * this.shotsPerSecond)+" §4❤",
            "§7Range: §f" + StringUtil.formatNum(this.range)+" blocks"
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
