package doodieman.towerdefense.maps;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.maps.objects.Map;
import doodieman.towerdefense.utils.VoidWorldGenerator;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MapHandler {

    @Getter
    private World world;

    @Getter
    private final MapUtil mapUtil;

    @Getter
    private final List<Map> loadedMaps = new ArrayList<>();

    public MapHandler() {
        this.mapUtil = new MapUtil(this);

        this.createGameWorld();
        this.loadMaps();
    }

    public void loadMaps() {
        FileConfiguration config = TowerDefense.getInstance().getConfig();
        ConfigurationSection section = config.getConfigurationSection("maps");

        for (String mapName : section.getKeys(false)) {
            Map map = new Map(mapName);
            map.load();
            this.loadedMaps.add(map);
        }
    }

    public void createGameWorld() {
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
