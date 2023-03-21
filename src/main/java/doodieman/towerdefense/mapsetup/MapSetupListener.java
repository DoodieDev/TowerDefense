package doodieman.towerdefense.mapsetup;

import doodieman.towerdefense.TowerDefense;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
public class MapSetupListener implements Listener {

    final MapSetupHandler handler;

    public MapSetupListener(MapSetupHandler handler) {
        this.handler = handler;
        Bukkit.getPluginManager().registerEvents(this, TowerDefense.getInstance());
    }

}
