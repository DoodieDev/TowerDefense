package doodieman.towerdefense.lobby.spawn;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class SpawnUtil {

    public static void setSpawn(Location location) {
        FileConfiguration config = TowerDefense.getInstance().getConfig();
        config.set("spawn", LocationUtil.locationToString(location));
        TowerDefense.getInstance().saveConfig();
    }

    public static Location getSpawn() {
        FileConfiguration config = TowerDefense.getInstance().getConfig();
        return LocationUtil.stringToLocation(config.getString("spawn"));
    }

}
