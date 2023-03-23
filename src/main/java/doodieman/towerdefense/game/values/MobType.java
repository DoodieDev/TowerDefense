package doodieman.towerdefense.game.values;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public enum MobType {

    ZOMBIE1(EntityType.ZOMBIE, 1.25, 1, entity -> {
        Zombie zombie = (Zombie) entity;
        zombie.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        zombie.setBaby(false);
        zombie.setVillager(false);
    });

    @Getter
    private final EntityType entityType;
    @Getter
    private final MobRunnable runnable;
    @Getter
    private final double speed;
    @Getter
    private final double health;


    MobType(EntityType entityType, double speed, double health, MobRunnable runnable) {
        this.entityType = entityType;
        this.speed = speed/20; //Converts from blocks a tick, to blocks a second
        this.health = health;
        this.runnable = runnable;
    }

    public interface MobRunnable {
        void run(Entity entity);
    }
}
