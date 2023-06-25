package doodieman.towerdefense.game.values;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public enum MobType {

    ZOMBIE1(EntityType.ZOMBIE, 3, 1, 1, 3, 2.3, entity -> {
        Zombie zombie = (Zombie) entity;
        zombie.getEquipment().setItemInHand(new ItemStack(Material.STICK));
        zombie.setBaby(false);
        zombie.setVillager(false);
        if (zombie.isInsideVehicle()) zombie.getVehicle().remove();
    }),

    ZOMBIE2(EntityType.ZOMBIE, 3, 2, 1, 5, 2.3, entity -> {
        Zombie zombie = (Zombie) entity;
        zombie.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        zombie.getEquipment().setItemInHand(new ItemStack(Material.CARROT_ITEM));
        zombie.setBaby(false);
        zombie.setVillager(false);
        if (zombie.isInsideVehicle()) zombie.getVehicle().remove();
    }),

    ZOMBIE3(EntityType.ZOMBIE, 3, 3, 1, 8, 2.3, entity -> {
        Zombie zombie = (Zombie) entity;
        zombie.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        zombie.getEquipment().setItemInHand(new ItemStack(Material.WOOD_SPADE));
        zombie.setBaby(false);
        zombie.setVillager(false);
        if (zombie.isInsideVehicle()) zombie.getVehicle().remove();
    }),

    ZOMBIE4(EntityType.ZOMBIE, 2, 30, 10, 40, 2.3, entity -> {
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

    SKELETON1(EntityType.SKELETON, 5, 1, 1, 4, 2.3, entity -> {
        Skeleton skeleton = (Skeleton) entity;
        skeleton.getEquipment().setItemInHand(new ItemStack(Material.BONE));
        if (skeleton.isInsideVehicle()) skeleton.getVehicle().remove();
    }),

    SKELETON2(EntityType.SKELETON, 8, 3, 1, 10, 2.3, entity -> {
        Skeleton skeleton = (Skeleton) entity;
        skeleton.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
        skeleton.getEquipment().setItemInHand(new ItemStack(Material.RED_ROSE,1,(short) 8));
        if (skeleton.isInsideVehicle()) skeleton.getVehicle().remove();
    }),

    SKELETON3(EntityType.SKELETON, 5, 5, 1, 9, 2.3, entity -> {
        Skeleton skeleton = (Skeleton) entity;
        skeleton.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
        skeleton.getEquipment().setItemInHand(new ItemStack(Material.BONE));
        if (skeleton.isInsideVehicle()) skeleton.getVehicle().remove();
    }),

    SKELETON4(EntityType.SKELETON, 4, 15, 1, 20, 2.3, entity -> {
        Skeleton skeleton = (Skeleton) entity;
        skeleton.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
        skeleton.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        skeleton.getEquipment().setItemInHand(new ItemStack(Material.IRON_HOE));
        if (skeleton.isInsideVehicle()) skeleton.getVehicle().remove();
    }),

    SKELETON5(EntityType.SKELETON, 3.5, 75, 25, 70, 2.3, entity -> {
        //SKELETON BOSS
        Skeleton skeleton = (Skeleton) entity;
        skeleton.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
        skeleton.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        skeleton.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        skeleton.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
        skeleton.getEquipment().setItemInHand(new ItemStack(Material.STONE_SWORD));
        if (skeleton.isInsideVehicle()) skeleton.getVehicle().remove();
    }),

    PIGMAN1(EntityType.PIGMAN, 2, 15, 1, 10, 2.3, entity -> {
        Pigman pigman = (Pigman) entity;
        pigman.getEquipment().setItemInHand(new ItemStack(Material.BLAZE_ROD));
        if (pigman.isInsideVehicle()) pigman.getVehicle().remove();
    }),

    PIGMAN2(EntityType.PIGMAN, 2, 25, 1, 15, 2.3, entity -> {
        Pigman pigman = (Pigman) entity;
        pigman.getEquipment().setHelmet(new ItemStack(Material.GOLD_HELMET));
        pigman.getEquipment().setItemInHand(new ItemStack(Material.FISHING_ROD));
        if (pigman.isInsideVehicle()) pigman.getVehicle().remove();
    }),

    PIGMAN3(EntityType.PIGMAN, 3.5, 20, 1, 20, 2.3, entity -> {
        Pigman pigman = (Pigman) entity;
        pigman.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
        pigman.getEquipment().setItemInHand(new ItemStack(Material.GOLD_BARDING));
        if (pigman.isInsideVehicle()) pigman.getVehicle().remove();
    }),

    PIGMAN4(EntityType.PIGMAN, 2.75, 30, 1, 30, 2.3, entity -> {
        Pigman pigman = (Pigman) entity;
        pigman.getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
        pigman.getEquipment().setItemInHand(new ItemStack(Material.GOLD_SPADE));
        if (pigman.isInsideVehicle()) pigman.getVehicle().remove();
    }),

    PIGMAN5(EntityType.PIGMAN, 1.5, 150, 50, 100, 2.3, entity -> {
        Pigman pigman = (Pigman) entity;
        pigman.getEquipment().setHelmet(new ItemStack(Material.GOLD_HELMET));
        pigman.getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
        pigman.getEquipment().setItemInHand(new ItemStack(Material.GOLD_SWORD ));
        if (pigman.isInsideVehicle()) pigman.getVehicle().remove();
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
    @Getter
    private final double gold;
    @Getter
    private final double hologramOffset;

    MobType(EntityType entityType, double speed, double health, double damage, double gold, double hologramOffset, MobRunnable runnable) {
        this.entityType = entityType;
        this.speed = speed/20; //Converts from blocks a tick, to blocks a second
        this.health = health;
        this.runnable = runnable;
        this.damage = damage;
        this.gold = gold;
        this.hologramOffset = hologramOffset;

    }

    public interface MobRunnable {
        void run(Entity entity);
    }
}
