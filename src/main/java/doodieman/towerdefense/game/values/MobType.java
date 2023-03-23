package doodieman.towerdefense.game.values;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public enum MobType {

    ZOMBIE(EntityType.ENDERMAN, 0.4, 1, "{IsBaby:0,IsVillager:0}", new ItemStack(Material.DIAMOND_HELMET), null, null, new ItemStack(Material.DIAMOND_BOOTS), null),
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
        this.speed = speed;
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
