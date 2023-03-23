package doodieman.towerdefense.maps;

import doodieman.towerdefense.maps.objects.Map;
import lombok.Getter;
import org.bukkit.World;

public class MapUtil {

    final MapHandler handler;

    @Getter
    private static MapUtil instance;

    public MapUtil(MapHandler handler) {
        this.handler = handler;
        instance = this;
    }

    public World getGameWorld() {
        return handler.getWorld();
    }

    public boolean doesMapExists(String mapID) {
        return handler.getLoadedMaps()
            .stream()
            .anyMatch(map -> map.getMapID().equalsIgnoreCase(mapID));
    }

    public Map getMap(String mapID) {
        return handler.getLoadedMaps()
            .stream()
            .filter(map -> map.getMapID().equalsIgnoreCase(mapID))
            .findAny().get();
    }

}
