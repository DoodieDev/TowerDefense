package doodieman.towerdefense;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import doodieman.towerdefense.game.GameHandler;
import doodieman.towerdefense.lobby.mapselector.MapSelectorHandler;
import doodieman.towerdefense.lobby.spawn.SetspawnCommand;
import doodieman.towerdefense.lobby.spawn.SpawnCommand;
import doodieman.towerdefense.mapgrid.MapGridHandler;
import doodieman.towerdefense.maps.MapHandler;
import doodieman.towerdefense.mapsetup.command.MapSetupCommand;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import doodieman.towerdefense.playerdata.PlayerDataHandler;
import doodieman.towerdefense.playerdata.objects.PlayerData;
import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TowerDefense extends JavaPlugin {

    @Getter
    private static TowerDefense instance;


    @Getter
    private MapGridHandler mapGridHandler;
    @Getter
    private MapSetupHandler mapSetupHandler;
    @Getter
    private MapHandler mapHandler;
    @Getter
    private GameHandler gameHandler;
    @Getter
    private MapSelectorHandler mapSelectorHandler;
    @Getter
    private PlayerDataHandler playerDataHandler;

    @Getter
    private WorldEditPlugin worldedit;
    @Getter
    private NPCRegistry npcRegistry;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        this.worldedit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        this.npcRegistry = CitizensAPI.createNamedNPCRegistry("towerdefense", new MemoryNPCDataStore());


        //Initialize handlers and commands
        this.loadHandlers();
        this.loadCommands();
    }

    @Override
    public void onDisable() {
        this.gameHandler.exitAllGames();
        this.npcRegistry.deregisterAll();
        HologramsAPI.getHolograms(this).forEach(Hologram::delete);
    }

    private void loadHandlers() {
        this.playerDataHandler = new PlayerDataHandler();
        this.mapGridHandler = new MapGridHandler();
        this.mapSetupHandler = new MapSetupHandler();
        this.mapHandler = new MapHandler();
        this.gameHandler = new GameHandler();
        this.mapSelectorHandler = new MapSelectorHandler();
    }

    private void loadCommands() {
        Bukkit.getPluginCommand("mapsetup").setExecutor(new MapSetupCommand(this.mapSetupHandler));
        Bukkit.getPluginCommand("test").setExecutor(new TestCommand());
        Bukkit.getPluginCommand("setspawn").setExecutor(new SetspawnCommand());
        Bukkit.getPluginCommand("spawn").setExecutor(new SpawnCommand());
    }

    public static void runSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(getInstance(), runnable);
    }

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(getInstance(), runnable);
    }
}
