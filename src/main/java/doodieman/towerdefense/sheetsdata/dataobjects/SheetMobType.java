package doodieman.towerdefense.sheetsdata.dataobjects;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.List;

public class SheetMobType {

    @Getter
    private final String name;
    @Getter
    private final double health;
    @Getter
    private final double speed;
    @Getter
    private final double damage;
    @Getter
    private final double gold;
    @Getter
    private final Material hand;
    @Getter
    private final Material helmet;
    @Getter
    private final Material chestplate;
    @Getter
    private final Material leggings;
    @Getter
    private final Material boots;
    @Getter
    private final EntityType entityType;

    public SheetMobType(List<Object> mobData) {
        this.name = (String) mobData.get(0);
        this.health = (double) mobData.get(1);
        this.speed = (double) mobData.get(2);
        this.damage = (double) mobData.get(3);
        this.gold = (double) mobData.get(4);
        this.hand = Material.getMaterial((String) mobData.get(5));
        this.helmet = Material.getMaterial((String) mobData.get(6));
        this.chestplate = Material.getMaterial((String) mobData.get(7));
        this.leggings = Material.getMaterial((String) mobData.get(8));
        this.boots = Material.getMaterial((String) mobData.get(9));
        this.entityType = EntityType.fromName((String) mobData.get(10));
    }

}
