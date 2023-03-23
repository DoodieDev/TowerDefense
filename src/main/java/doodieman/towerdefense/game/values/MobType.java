package doodieman.towerdefense.game.values;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public enum MobType {

    ZOMBIE1(EntityType.ZOMBIE, 10, 1, "{IsBaby:0,IsVillager:0}", null, null, null, null, null),
    ZOMBIE2(EntityType.ZOMBIE, 15, 3, "{IsBaby:0,IsVillager:0}", new ItemStack(Material.LEATHER_HELMET), null, null, null, null),
    ZOMBIE3(EntityType.ZOMBIE, 0.5, 5, "{IsBaby:0,IsVillager:0}", null, new ItemStack(Material.LEATHER_CHESTPLATE), null, null, null),
    ZOMBIE4(EntityType.ZOMBIE, 0.2, 25, "{IsBaby:0,IsVillager:0}", new ItemStack(Material.IRON_HELMET), new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.STONE_SWORD)),

    TEST(EntityType.ZOMBIE, 0.1, 1, "{isBaby:1,IsVillager:0}", null, null, null, null, null);

    @Getter
    private final EntityType entityType;
    @Getter
    private final String nbt;
    @Getter
    private final double speed;
    @Getter
    private final double health;

    //EQUIPMENT
    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;
    private final ItemStack weapon;

    MobType(EntityType entityType, double speed, double health, String nbt, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack weapon) {
        this.entityType = entityType;
        this.speed = speed/20; //Converts from blocks a tick, to blocks a second
        this.health = health;
        this.nbt = nbt;

        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.weapon = weapon;
    }

    public ItemStack getHelmet() {
        return helmet == null ? new ItemStack(Material.AIR) : helmet;
    }
    public ItemStack getChestplate() {
        return chestplate == null ? new ItemStack(Material.AIR) : chestplate;
    }
    public ItemStack getLeggings() {
        return leggings == null ? new ItemStack(Material.AIR) : leggings;
    }
    public ItemStack getBoots() {
        return boots == null ? new ItemStack(Material.AIR) : boots;
    }
    public ItemStack getWeapon() {
        return weapon == null ? new ItemStack(Material.AIR) : weapon;
    }
}
