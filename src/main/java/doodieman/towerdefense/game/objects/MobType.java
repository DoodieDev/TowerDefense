package doodieman.towerdefense.game.objects;

import lombok.Getter;
import org.bukkit.entity.EntityType;

public enum MobType {

    ZOMBIE(EntityType.SLIME, 0.4, 100);

    @Getter
    private final EntityType entityType;
    @Getter
    private final double speed;
    @Getter
    private final double health;

    MobType(EntityType entityType, double speed, double health) {
        this.entityType = entityType;
        this.speed = speed;
        this.health = health;
    }

}
