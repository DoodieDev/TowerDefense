package doodieman.towerdefense;

import doodieman.towerdefense.mapsetup.MapSetupCommand;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
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

        //Initialize handlers and commands
        this.loadHandlers();
        this.loadCommands();
    }

    @Override
    public void onDisable() {}

    private void loadHandlers() {
        this.mapSetupHandler = new MapSetupHandler();
    }

    private void loadCommands() {
        Bukkit.getPluginCommand("mapsetup").setExecutor(new MapSetupCommand(this.mapSetupHandler));
    }
}
