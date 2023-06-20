package doodieman.towerdefense.game.values;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public enum MobType {

    ZOMBIE1(EntityType.ZOMBIE, 3, 1, 1, entity -> {
        Zombie zombie = (Zombie) entity;
        zombie.getEquipment().setItemInHand(new ItemStack(Material.STICK));
        zombie.setBaby(false);
        zombie.setVillager(false);
        if (zombie.isInsideVehicle()) zombie.getVehicle().remove();
    }),

    ZOMBIE2(EntityType.ZOMBIE, 3, 2, 1, entity -> {
        Zombie zombie = (Zombie) entity;
        zombie.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        zombie.getEquipment().setItemInHand(new ItemStack(Material.CARROT_ITEM));
        zombie.setBaby(false);
        zombie.setVillager(false);
        if (zombie.isInsideVehicle()) zombie.getVehicle().remove();
    }),

    ZOMBIE3(EntityType.ZOMBIE, 3, 3, 1, entity -> {
        Zombie zombie = (Zombie) entity;
        zombie.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        zombie.getEquipment().setItemInHand(new ItemStack(Material.WOOD_SPADE));
        zombie.setBaby(false);
        zombie.setVillager(false);
        if (zombie.isInsideVehicle()) zombie.getVehicle().remove();
    }),

    ZOMBIE4(EntityType.ZOMBIE, 2, 30, 10, entity -> {
        //ZOMBIE BOSS
        Zombie zombie = (Zombie) entity;
        zombie.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
        zombie.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        zombie.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        zombie.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));
        zombie.getEquipment().setItemInHand(new ItemStack(Material.IRON_SWORD));
        zombie.setBaby(false);
        zombie.setVillager(false);
        if (zombie.isInsideVehicle()) zombie.getVehicle().remove();
    }),

    SKELETON1(EntityType.SKELETON, 5, 1, 1, entity -> {
        Skeleton skeleton = (Skeleton) entity;
        skeleton.getEquipment().setItemInHand(new ItemStack(Material.BONE));
        if (skeleton.isInsideVehicle()) skeleton.getVehicle().remove();
    }),

    SKELETON2(EntityType.SKELETON, 8, 3, 1, entity -> {
        Skeleton skeleton = (Skeleton) entity;
        skeleton.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
        skeleton.getEquipment().setItemInHand(new ItemStack(Material.RED_ROSE,1,(short) 8));
        if (skeleton.isInsideVehicle()) skeleton.getVehicle().remove();
    }),

    SKELETON3(EntityType.SKELETON, 5, 5, 1, entity -> {
        Skeleton skeleton = (Skeleton) entity;
        skeleton.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
        skeleton.getEquipment().setItemInHand(new ItemStack(Material.BONE));
        if (skeleton.isInsideVehicle()) skeleton.getVehicle().remove();
    }),

    SKELETON4(EntityType.SKELETON, 4, 15, 1, entity -> {
        Skeleton skeleton = (Skeleton) entity;
        skeleton.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
        skeleton.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        skeleton.getEquipment().setItemInHand(new ItemStack(Material.IRON_HOE));
        if (skeleton.isInsideVehicle()) skeleton.getVehicle().remove();
    }),

    SKELETON5(EntityType.SKELETON, 3.5, 75, 25, entity -> {
        //SKELETON BOSS
        Skeleton skeleton = (Skeleton) entity;
        skeleton.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
        skeleton.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        skeleton.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        skeleton.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
        skeleton.getEquipment().setItemInHand(new ItemStack(Material.STONE_SWORD));
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
