package doodieman.towerdefense.game.objects;

import lombok.Getter;
import org.bukkit.entity.EntityType;

public enum MobType {

    ZOMBIE(EntityType.CREEPER, 0.15);

    @Getter
    private final EntityType entityType;
    @Getter
    private final double speed;

    MobType(EntityType entityType, double speed) {
        this.entityType = entityType;
        this.speed = speed;
    }

}
