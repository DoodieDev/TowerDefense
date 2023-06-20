package doodieman.towerdefense.game.values;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public enum MobType {

    ZOMBIE1(EntityType.ZOMBIE, 1, 1, 1, entity -> {
        Zombie zombie = (Zombie) entity;
        zombie.getEquipment().setItemInHand(new ItemStack(Material.STICK));
        zombie.setBaby(false);
        zombie.setVillager(false);
        if (zombie.isInsideVehicle()) zombie.getVehicle().remove();
    }),

    ZOMBIE2(EntityType.ZOMBIE, 1, 2, 1, entity -> {
        Zombie zombie = (Zombie) entity;
        zombie.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        zombie.getEquipment().setItemInHand(new ItemStack(Material.CARROT_ITEM));
        zombie.setBaby(false);
        zombie.setVillager(false);
        if (zombie.isInsideVehicle()) zombie.getVehicle().remove();
    }),

    ZOMBIE3(EntityType.ZOMBIE, 1, 3, 1, entity -> {
        Zombie zombie = (Zombie) entity;
        zombie.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        zombie.getEquipment().setItemInHand(new ItemStack(Material.WOOD_SPADE));
        zombie.setBaby(false);
        zombie.setVillager(false);
        if (zombie.isInsideVehicle()) zombie.getVehicle().remove();
    }),

    SKELETON1(EntityType.SKELETON, 3.25, 1, 1, entity -> {
        Skeleton skeleton = (Skeleton) entity;
        skeleton.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        skeleton.getEquipment().setItemInHand(new ItemStack(Material.AIR));
        if (skeleton.isInsideVehicle()) skeleton.getVehicle().remove();
    });
    
    @Getter
    private final EntityType entityType;
    @Getter
    private final MobRunnable runnable;
    @Getter
    private final double speed;
    @Getter
    private final double health;
    @Getter
    private final double damage;

    MobType(EntityType entityType, double speed, double health, double damage, MobRunnable runnable) {
        this.entityType = entityType;
        this.speed = speed/20; //Converts from blocks a tick, to blocks a second
        this.health = health;
        this.runnable = runnable;
        this.damage = damage;
    }

    public interface MobRunnable {
        void run(Entity entity);
    }
}
