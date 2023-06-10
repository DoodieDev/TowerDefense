package doodieman.towerdefense.maps;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.lobby.spawn.SpawnUtil;
import doodieman.towerdefense.maps.objects.Map;
import doodieman.towerdefense.utils.FileUtils;
import doodieman.towerdefense.utils.VoidWorldGenerator;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
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

        this.deleteGameWorld();
        this.createGameWorld();
        this.loadMaps();
    }

    public void loadMaps() {
        FileConfiguration config = TowerDefense.getInstance().getConfig();
        ConfigurationSection section = config.getConfigurationSection("maps");

        for (String mapID : section.getKeys(false)) {
            Map map = new Map(mapID);
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
        this.world.setGameRuleValue("doDaylightCycle", "false");
    }

    public void deleteGameWorld() {
        World gameworld = Bukkit.getWorld("gameworld");

        if (gameworld != null) {
            for (Player player : gameworld.getPlayers())
                player.teleport(SpawnUtil.getSpawn());
        }

        try {
            Bukkit.unloadWorld("gameworld", false);
        } catch (ArrayIndexOutOfBoundsException ignored) {
            System.out.println("Failed unloading the world!");
        }

        FileUtils.deleteDir(new File("gameworld"));
    }

    private boolean doesWorldExist() {
        File file = new File("gameworld");
        return file.exists();
    }

}
