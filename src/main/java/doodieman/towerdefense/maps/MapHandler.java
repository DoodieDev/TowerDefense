package doodieman.towerdefense.maps;

import lombok.Getter;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class MapHandler {

    @Getter
    private World world;

    public MapHandler() {
        this.initWorld();
    }

    public void initWorld() {
        WorldCreator worldCreator = new WorldCreator("gameworld");
        worldCreator.generator(new VoidWorldGenerator());
        worldCreator.type(WorldType.FLAT);
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.generateStructures(false);
        this.world = worldCreator.createWorld();
        this.world.setAutoSave(false);
        this.world.setGameRuleValue("doMobSpawning", "false");
        this.world.setGameRuleValue("randomTickSpeed", "0");
    }

}
