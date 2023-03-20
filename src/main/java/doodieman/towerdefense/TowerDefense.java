package doodieman.towerdefense;

import doodieman.towerdefense.mapsetup.MapSetupHandler;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class TowerDefense extends JavaPlugin {

    @Getter
    private static TowerDefense instance;

    @Getter
    private MapSetupHandler mapSetupHandler;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
