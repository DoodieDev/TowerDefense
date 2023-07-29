package doodieman.towerdefense;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import doodieman.towerdefense.chat.ChatHandler;
import doodieman.towerdefense.game.GameHandler;
import doodieman.towerdefense.lobby.bench.BenchHandler;
import doodieman.towerdefense.lobby.mapselector.MapSelectorHandler;
import doodieman.towerdefense.lobby.spawn.SetspawnCommand;
import doodieman.towerdefense.lobby.spawn.SpawnCommand;
import doodieman.towerdefense.lobby.spawn.SpawnHandler;
import doodieman.towerdefense.lobby.water.WaterHandler;
import doodieman.towerdefense.mapgrid.MapGridHandler;
import doodieman.towerdefense.maps.MapHandler;
import doodieman.towerdefense.mapsetup.command.MapSetupCommand;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import doodieman.towerdefense.playerdata.PlayerDataHandler;
import doodieman.towerdefense.sheetsdata.SheetsDataManager;
import doodieman.towerdefense.simplecommands.discord.DiscordCommand;
import doodieman.towerdefense.simpleevents.GlobalListener;
import doodieman.towerdefense.simpleevents.region.RegionListener;
import doodieman.towerdefense.sumo.SumoHandler;
import doodieman.towerdefense.turretsetup.TurretSetupHandler;
import doodieman.towerdefense.turretsetup.command.TurretSetupCommand;
import doodieman.towerdefense.utils.StringUtil;
import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.luckperms.api.LuckPerms;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class TowerDefense extends JavaPlugin {

    @Getter
    private static TowerDefense instance;


    @Getter
    private MapGridHandler mapGridHandler;
    @Getter
    private MapSetupHandler mapSetupHandler;
    @Getter
    private ChatHandler chatHandler;
    @Getter
    private MapHandler mapHandler;
    @Getter
    private GameHandler gameHandler;
    @Getter
    private MapSelectorHandler mapSelectorHandler;
    @Getter
    private TurretSetupHandler turretSetupHandler;
    @Getter
    private PlayerDataHandler playerDataHandler;
    @Getter
    private SpawnHandler spawnHandler;
    @Getter
    private SumoHandler sumoHandler;
    @Getter
    private SheetsDataManager sheetsDataManager;

    /*
        external plugin dependencies
    */
    @Getter
    private WorldEditPlugin worldedit;
    @Getter
    private NPCRegistry npcRegistry;
    @Getter
    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        //Pure visual for admins
        long startTime = System.currentTimeMillis();
        this.announceForAdmins("§aEnabling the plugin..");

        instance = this;
        this.saveDefaultConfig();

        this.worldedit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        this.npcRegistry = CitizensAPI.createNamedNPCRegistry("towerdefense", new MemoryNPCDataStore());
        RegisteredServiceProvider<LuckPerms> luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        this.luckPerms = luckPermsProvider.getProvider();
        this.sheetsDataManager = new SheetsDataManager();

        Bukkit.getPluginManager().registerEvents(new GlobalListener(),this);

        //Download the sheets data (Turrets, Mobs, Rounds, etc)
        this.announceForAdmins("§aDownloading sheets data..");
        try {
            sheetsDataManager.download();
            sheetsDataManager.loadMobs();
            sheetsDataManager.loadRounds();
        } catch (IOException | InvalidFormatException exception) {
            exception.printStackTrace();
        }

        //Initialize handlers and commands
        this.announceForAdmins("§aLoading all handlers and commands..");
        this.loadHandlers();
        this.loadCommands();

        //Pure visual for admins
        long finishTime = System.currentTimeMillis();
        String formattedTime = StringUtil.formatNum((finishTime - startTime) / 1000f);
        this.announceForAdmins("§aPlugin has successfully been enabled! (Took "+formattedTime+"s)");
    }

    @Override
    public void onDisable() {
        //Pure visual for admins
        long startTime = System.currentTimeMillis();
        this.announceForAdmins("§cDisabling the plugin..");

        this.gameHandler.exitAllGames();
        this.npcRegistry.deregisterAll();

        HologramsAPI.getHolograms(this).forEach(Hologram::delete);

        //Pure visual for admins
        long finishTime = System.currentTimeMillis();
        String formattedTime = StringUtil.formatNum((finishTime - startTime) / 1000f);
        this.announceForAdmins("§cPlugin has been disabled! (Took "+formattedTime+"s)");
    }

    private void loadHandlers() {
        this.playerDataHandler = new PlayerDataHandler();
        this.chatHandler = new ChatHandler();
        this.mapGridHandler = new MapGridHandler();
        this.mapSetupHandler = new MapSetupHandler();
        this.turretSetupHandler = new TurretSetupHandler();
        this.mapHandler = new MapHandler();
        this.gameHandler = new GameHandler();
        this.mapSelectorHandler = new MapSelectorHandler();
        this.spawnHandler = new SpawnHandler();
        this.sumoHandler = new SumoHandler();

        new WaterHandler();
        new BenchHandler();
        new RegionListener();
    }

    private void loadCommands() {
        Bukkit.getPluginCommand("mapsetup").setExecutor(new MapSetupCommand(this.mapSetupHandler));
        Bukkit.getPluginCommand("turretsetup").setExecutor(new TurretSetupCommand(this.turretSetupHandler));
        Bukkit.getPluginCommand("admin").setExecutor(new AdminGameCommand());
        Bukkit.getPluginCommand("setspawn").setExecutor(new SetspawnCommand());
        Bukkit.getPluginCommand("spawn").setExecutor(new SpawnCommand());
        Bukkit.getPluginCommand("discord").setExecutor(new DiscordCommand());
    }

    public static void runSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(getInstance(), runnable);
    }

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(getInstance(), runnable);
    }

    public static void doodieDebug(OfflinePlayer player, String message) {
        if (!player.getName().equals("DoodieMan")) return;
        player.getPlayer().sendMessage("§4[DEBUG] §c"+message);
    }

    public void announceForAdmins(String message) {
        Bukkit.getOnlinePlayers().stream()
            .filter(ServerOperator::isOp)
            .forEach(player -> player.sendMessage("§6§l[TD]§r "+message));
    }
}
