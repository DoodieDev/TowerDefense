package doodieman.towerdefense.mapsetup;

import doodieman.towerdefense.TowerDefense;
import lombok.Getter;

public class MapSetupHandler {

    @Getter
    private final MapSetupListener listener;

    public MapSetupHandler() {
        this.listener = new MapSetupListener(this);
    }

    public boolean doesMapExist(String mapName) {
        return TowerDefense.getInstance().getConfig().contains("maps."+mapName);
    }

}
