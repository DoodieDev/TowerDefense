package doodieman.towerdefense;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import doodieman.towerdefense.maps.MapHandler;
import doodieman.towerdefense.mapsetup.command.MapSetupCommand;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TowerDefense extends JavaPlugin {

    @Getter
    private static TowerDefense instance;

    @Getter
    private MapSetupHandler mapSetupHandler;
    @Getter
    private MapHandler mapHandler;

    @Getter
    private WorldEditPlugin worldedit;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        this.worldedit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

        //Initialize handlers and commands
        this.loadHandlers();
        this.loadCommands();
    }

    @Override
    public void onDisable() {}

    private void loadHandlers() {
        this.mapSetupHandler = new MapSetupHandler();
        this.mapHandler = new MapHandler();
    }

    private void loadCommands() {
        Bukkit.getPluginCommand("mapsetup").setExecutor(new MapSetupCommand(this.mapSetupHandler));
        Bukkit.getPluginCommand("test").setExecutor(new TestCommand());
    }
}
