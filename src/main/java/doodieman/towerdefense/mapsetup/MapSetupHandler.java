package doodieman.towerdefense.mapsetup;

import doodieman.towerdefense.TowerDefense;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
