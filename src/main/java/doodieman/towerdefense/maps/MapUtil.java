package doodieman.towerdefense.maps;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.maps.objects.Map;
import doodieman.towerdefense.maps.objects.MapSlot;
import lombok.Getter;
import org.bukkit.World;

import java.util.List;

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

    public List<MapSlot> getMapSlots() {
        return this.handler.getMapSlotList();
    }

    public MapSlot getMapSlot(String mapID) { ;
        for (MapSlot mapSlot : handler.getMapSlotList()) {
            if (mapSlot.getMapID() != null && mapSlot.getMapID().equalsIgnoreCase(mapID))
                return mapSlot;
        }
        return null;
    }

    public MapSlot getMapSlot(int menuSlot) {
        return this.handler.getMapSlotList()
            .stream()
            .filter(mapSlot -> mapSlot.getMenuSlot() == menuSlot)
            .findFirst()
            .get();
    }

}
