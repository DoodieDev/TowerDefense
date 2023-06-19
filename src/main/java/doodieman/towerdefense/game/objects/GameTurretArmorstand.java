package doodieman.towerdefense.game.objects;

import doodieman.towerdefense.game.values.TurretType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameTurretArmorstand {

    @Getter
    private final ArmorStand armorStand;
    @Getter
    private final TurretType turretType;
    @Getter
    private final String ID;
    @Getter
    private final List<String> tags;

    //This is the rotation from center to armorstand location. It is only set on armorstand spawn.
    @Getter @Setter
    private double staticRotation;
    @Getter @Setter
    private float staticYaw;

    public GameTurretArmorstand(ArmorStand armorStand, TurretType turretType, String ID, String tag) {
        this.armorStand = armorStand;
        this.turretType = turretType;
        this.ID = ID;
        this.staticRotation = 0;
        this.staticYaw = 0;
        this.tags = new ArrayList<>();
    }

    //Set tags example input: "norotate,cheese"
    public void setTags(String tagsString) {
        if (tagsString == null || tagsString.isEmpty()) return;
        String[] split = tagsString.split(",");
        this.tags.addAll(Arrays.asList(split));
    }

}
