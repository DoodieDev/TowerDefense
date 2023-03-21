package doodieman.towerdefense.maps;

import lombok.Getter;

public class MapUtil {

    final MapHandler handler;

    @Getter
    private static MapUtil instance;

    public MapUtil(MapHandler handler) {
        this.handler = handler;
        instance = this;
    }

}
