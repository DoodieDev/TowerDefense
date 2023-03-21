package doodieman.towerdefense.maps;

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

}
