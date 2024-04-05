package doodieman.towerdefense.game.objects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.game.interactive.GameInteractive;
import doodieman.towerdefense.game.TurretUtil;
import doodieman.towerdefense.game.turrets.GameTurret;
import doodieman.towerdefense.game.values.Difficulty;
import doodieman.towerdefense.game.values.GameSetting;
import doodieman.towerdefense.game.values.TurretType;
import doodieman.towerdefense.mapgrid.MapGridHandler;
import doodieman.towerdefense.mapgrid.objects.GridLocation;
import doodieman.towerdefense.maps.MapUtil;
import doodieman.towerdefense.maps.enums.MapDifficulty;
import doodieman.towerdefense.maps.objects.Map;
import doodieman.towerdefense.playerdata.PlayerDataUtil;
import doodieman.towerdefense.playerdata.objects.PlayerData;
import doodieman.towerdefense.sheetsdata.SheetsDataUtil;
import doodieman.towerdefense.sheetsdata.dataobjects.SheetMobCluster;
import doodieman.towerdefense.sheetsdata.dataobjects.SheetMobType;
import doodieman.towerdefense.sheetsdata.dataobjects.SheetRound;
import doodieman.towerdefense.utils.InventoryUtil;
import doodieman.towerdefense.utils.LocationUtil;
import doodieman.towerdefense.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Game {

    @Getter
    private final OfflinePlayer player;
    @Getter
    private final Map map;
    @Getter
    private final GridLocation gridLocation;
    @Getter
    private final GameInteractive gameInteractive;
    @Getter
    private final MapDifficulty mapDifficulty;
    @Getter
    private final World world;
    @Getter
    private final Location zeroLocation;
    @Getter
    private final List<Location> mobPath;
    BukkitTask mobPathLoop;


    //Normal game values being used globally
    @Getter
    private double health;
    @Getter
    private double gold;
    @Getter
    private final Difficulty difficulty;
    @Getter //List of active mobs on the path
    private final List<GameMob> aliveMobs;
    @Getter
    private final List<GameTurret> turrets;
    @Getter //Check if the round is active
    private boolean roundActive;
    @Getter
    private boolean isGameActive;
    @Getter
    private boolean mobsSpawning;
    private final java.util.Map<GameSetting, Boolean> gameSettings;
    @Getter @Setter
    private boolean isPastingTurret;
    @Getter @Setter
    private boolean doubleRoundSpeed;

    @Getter //Current round number
    private int currentRound;

    private Hologram startHologram;
    private TextLine roundTextLine;
    private TextLine goldTextLine;
    private TextLine healthTextLine;


    public Game(OfflinePlayer player, Map map, Difficulty difficulty) {
        this.player = player;
        this.map = map;
        this.gameInteractive = new GameInteractive(player, this);
        this.mobPath = new ArrayList<>();
        this.world = MapUtil.getInstance().getGameWorld();
        this.mapDifficulty = MapUtil.getInstance().getMapSlot(map.getMapID()).getDifficulty();

        //Grid location
        this.gridLocation = MapGridHandler.getInstance().generateLocation();
        this.gridLocation.register();
        this.zeroLocation = this.gridLocation.getLocation(this.world);

        this.difficulty = difficulty;
        this.health = difficulty.getHealth();
        this.gold = difficulty.getStartingGold();
        this.aliveMobs = new ArrayList<>();
        this.turrets = new ArrayList<>();
        this.roundActive = false;
        this.mobsSpawning = false;
        this.isGameActive = false;
        this.currentRound = 0;
        this.startHologram = null;
        this.mobPathLoop = null;
        this.gameSettings = new HashMap<>();
        this.isPastingTurret = false;
        this.doubleRoundSpeed = false;
    }

    //Prepares the game, pasting schematic, etc
    public void prepare(BukkitRunnable onFinish) {

        //Paste schematic async
        TowerDefense.runAsync(new BukkitRunnable() {
            @Override
            public void run() {
                map.pasteSchematic(zeroLocation);

                if (player.isOnline())
                    TowerDefense.runSync(onFinish);
            }
        });

        //Load the default Game Settings
        for (GameSetting setting : GameSetting.values())
            this.gameSettings.put(setting,setting.isDefaultValue());

        //Load the mob path
        for (Location location : map.getPath())
            this.mobPath.add(getRealLocation(location));

        //Create the hologram at mob starting point
        this.updateStartHologram();
    }

    //Starts the game. Teleports player, etc.
    public void start() {
        this.isGameActive = true;
        this.player.getPlayer().teleport(mobPath.get(0));
        this.gameInteractive.register();
        this.startLoop();
    }

    //Stops the game. Removing schematic, teleport player to spawn, etc.
    public void stop(boolean removeSchematic) {

        //Fix turret on head
        ItemStack helmet = player.getPlayer().getEquipment().getHelmet();
        if (helmet != null && helmet.getType() != Material.AIR) {
            player.getPlayer().getInventory().addItem(helmet.clone());
            player.getPlayer().getEquipment().setHelmet(new ItemStack(Material.AIR));
            player.getPlayer().updateInventory();
        }

        this.saveGame();

        this.roundActive = false;
        this.isGameActive = false;
        if (this.mobPathLoop != null)
            this.mobPathLoop.cancel();
        this.gridLocation.unregister();
        this.gameInteractive.unregister();
        if (this.startHologram != null)
            this.startHologram.delete();

        //Remove schematic
        if (removeSchematic) {
            TowerDefense.runAsync(new BukkitRunnable() {
                @Override
                public void run() {
                    com.sk89q.worldedit.world.World editWorld = new BukkitWorld(world);
                    Location corner1 = getRealLocation(map.getCorner1());
                    Location corner2 = getRealLocation(map.getCorner2());
                    Vector pos1 = new Vector(corner1.getX(), corner1.getY(), corner1.getZ());
                    Vector pos2 = new Vector(corner2.getX(), corner2.getY(), corner2.getZ());
                    CuboidRegion region = new CuboidRegion(editWorld, pos1, pos2);
                    EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(editWorld, -1);
                    editSession.setBlocks(region, new BaseBlock(0));
                    editSession.flushQueue();
                }
            });
        }
    }

    //Starts a wave of monsters
    private void startLoop() {

        List<GameMob> mobsToRemove = new ArrayList<>();
        this.mobPathLoop = new BukkitRunnable() {

            int tick = 0;

            @Override
            public void run() {

                //While round is active
                if (roundActive) {

                    //Move all the mobs on path
                    for (GameMob mob : aliveMobs) {
                        mob.move();

                        //If mob is in goal. Remove the mob and damage player
                        if (mob.isInGoal())
                            mobsToRemove.add(mob);
                    }

                    //Remove all mobs in goal
                    for (GameMob mob : mobsToRemove) {
                        double damage = Math.ceil(mob.getHealth());

                        gameInteractive.getGameAnimations().mobFinished( mob, damage);
                        damage(damage);
                        mob.remove(false);
                    }
                    mobsToRemove.clear();

                    //If round is over. Finish the round.
                    if (aliveMobs.size() <= 0 && !mobsSpawning && health > 0)
                        finishRound();
                }

                gameInteractive.doTick(tick);
                tick++;
            }
        }.runTaskTimer(TowerDefense.getInstance(), 0L, 1L);
    }

    //Spawns a mob that automatically will go on the path
    public void spawnMob(SheetMobType mobType) {

        GameMob mob = new GameMob(this, mobType);
        mob.spawn();

        aliveMobs.add(mob);
        gameInteractive.getGameAnimations().mobSpawned(mob.getEntity().getLocation());
    }

    //Starts the round
    public void startRound() {

        this.saveGame();

        this.roundActive = true;
        this.mobsSpawning = true;
        this.currentRound++;

        this.gameInteractive.updateRoundItemSlot();
        this.gameInteractive.getGameAnimations().newRoundStarted();

        SheetRound round = SheetsDataUtil.getInstance().getRound(currentRound);

        this.updateStartHologram();


        //The timer when the round is active
        new BukkitRunnable() {

            long roundTick = 0L;
            final List<SheetMobCluster> mobsLeftToSpawn = new ArrayList<>(round.getMobClusters());

            @Override
            public void run() {

                //Round not active anymore. Cancel the timer
                if (!roundActive) {
                    this.cancel();
                    return;
                }

                //Spawn mob
                double spawnDelay = isDoubleRoundSpeed() ? round.getSpawnDelay() / 2 : round.getSpawnDelay();
                if (roundTick % spawnDelay == 0 && mobsLeftToSpawn.size() > 0) {

                    SheetMobCluster cluster = mobsLeftToSpawn.get(0);
                    cluster.setAmount(cluster.getAmount() - 1);

                    SheetMobType mobType = cluster.getSheetMobType();
                    spawnMob(mobType);

                    //No mobs left in cluster. Remove cluser from list.
                    if (cluster.getAmount() <= 0) {
                        mobsLeftToSpawn.remove(cluster);

                        //No more clusters. Set status to no longer spawning.
                        if (mobsLeftToSpawn.size() <= 0) {
                            mobsSpawning = false;
                        }
                    }
                }

                //Update all turrets
                for (GameTurret turret : getTurrets())
                    turret.update(roundTick);

                roundTick++;
            }
        }.runTaskTimer(TowerDefense.getInstance(),0L,1L);
    }

    //Called when the round is finished
    public void finishRound() {
        this.roundActive = false;
        this.wipeMobs();
        this.gameInteractive.updateRoundItemSlot();
        this.gameInteractive.getGameAnimations().roundFinished();

        for (GameTurret turret : getTurrets())
            turret.roundFinished();

        if (this.getGameSetting(GameSetting.AUTO_START) && !this.hasWonGame())
            this.startRound();

        //If player has won or is dead, delete the save.
        if (this.hasWonGame() || !this.isAlive()) {
            GameUtil.getInstance().deleteSavedGame(player,map);
        }
    }

    //Removes all the active mobs
    public void wipeMobs() {
        for (GameMob mob : new ArrayList<>(this.aliveMobs))
            mob.remove(false);
        this.aliveMobs.clear();
    }

    //When a mobs damages the player
    public void damage(double amount) {
        this.health -= amount;

        //Game over
        if (this.health <= 0) {
            this.health = 0;
            this.updateStartHologram();
            this.gameDeath();
        }
    }

    //Called when the player dies
    public void gameDeath() {
        this.mobPathLoop.cancel();
        this.wipeMobs();
        this.roundActive = false;
        this.health = 0;

        this.gameInteractive.getGameAnimations().onDie();
        this.gameInteractive.updateRoundItemSlot();
    }

    //Create or update the hologram at the start
    public void updateStartHologram() {
        Location location = this.mobPath.get(0).clone().add(0,3,0);
        //Create the hologram
        if (this.startHologram == null) {
            this.startHologram = HologramsAPI.createHologram(TowerDefense.getInstance(),location);
            startHologram.appendTextLine("§a§nStartlinje");
            startHologram.appendTextLine("");
            startHologram.appendTextLine("§7Map: "+mapDifficulty.getColor()+map.getMapName());
            startHologram.appendTextLine("§7Sværhedsgrad: "+difficulty.getTextColor()+difficulty.getName());
            startHologram.appendTextLine("");
            this.roundTextLine = startHologram.appendTextLine("§7Runde: §f"+currentRound+"§7/§f"+difficulty.getRounds());
            this.healthTextLine = startHologram.appendTextLine("§7Liv: §f"+StringUtil.formatNum(health)+" §4❤");
            startHologram.appendTextLine("");
            this.goldTextLine = startHologram.appendTextLine("§7Guld: §e"+ StringUtil.formatNum(gold) +"g");
        }

        this.roundTextLine.setText("§7Runde: §f"+currentRound+"§7/§f"+difficulty.getRounds());
        this.healthTextLine.setText("§7Liv: §f"+StringUtil.formatNum(health)+"§4❤");
        this.goldTextLine.setText("§7Guld: §e"+ StringUtil.formatNum(gold) +"g");
    }

    //Save the game
    public void saveGame() {
        if (roundActive) return;
        if (!isAlive()) return;
        if (!isGameActive()) return;
        if (turrets.size() <= 0) return;

        PlayerData playerData = PlayerDataUtil.getData(player);
        FileConfiguration config = playerData.getFile();

        config.set("saves."+map.getMapID(), null);

        if (!config.contains("saves."+map.getMapID()))
            config.createSection("saves."+map.getMapID());
        ConfigurationSection section = config.getConfigurationSection("saves."+map.getMapID());

        //Save basic stats
        section.set("health", this.health);
        section.set("round", this.currentRound);
        section.set("difficulty",this.difficulty.getId());
        section.set("gold", this.gold);
        section.set("inventory", InventoryUtil.toBase64(player.getPlayer().getInventory()));
        section.set("date", new Date().getTime());

        //Save turrets
        int id = 0;
        for (GameTurret turret : this.turrets) {
            String turretPath = "turrets."+id+".";

            String offsetString = LocationUtil.vectorToString(this.getOffset(turret.getLocation()));

            section.set(turretPath+"offset", offsetString);
            section.set(turretPath+"turretType", turret.getTurretType().getId());

            //TODO save more stats

            id++;
        }

        playerData.save();
    }

    //Load the game
    public void loadGame(BukkitRunnable onFinish) {

        PlayerData playerData = PlayerDataUtil.getData(player);
        FileConfiguration config = playerData.getFile();

        ConfigurationSection section = config.getConfigurationSection("saves."+map.getMapID());

        this.health = section.getDouble("health");
        this.currentRound = section.getInt("round");
        this.gold = section.getDouble("gold");

        //Load inventory
        Inventory inventory = InventoryUtil.fromBase64(section.getString("inventory"));
        for (int i = 0; i < inventory.getSize(); i++)
            player.getPlayer().getInventory().setItem(i,inventory.getItem(i));

        //Load turrets
        for (String id : section.getConfigurationSection("turrets").getKeys(false)) {
            ConfigurationSection turretSection = section.getConfigurationSection("turrets."+id);

            org.bukkit.util.Vector offset = LocationUtil.stringToVector(turretSection.getString("offset"));
            String turretTypeID = turretSection.getString("turretType");

            TurretType turretType = TurretType.getByID(turretTypeID);
            Location turretLocation = this.getLocationFromOffset(offset);

            GameTurret turret = TurretUtil.getInstance().createTurret(this,turretType,turretLocation);
            turret.setRotation(0);
            turretsToRender.add(turret);
        }

        this.updateStartHologram();

        //Render all the turrets with delay between
        this.setPastingTurret(true);
        this.turretsToRenderLoop(onFinish);
    }

    //Render all the turrets from save
    List<GameTurret> turretsToRender = new ArrayList<>();
    public void turretsToRenderLoop(BukkitRunnable onFinish) {

        if (turretsToRender.size() <= 0) {
            this.setPastingTurret(false);
            onFinish.run();
            return;
        }

        GameTurret turret = turretsToRender.get(0);
        turretsToRender.remove(turret);

        turret.render(false, new BukkitRunnable() {
            @Override
            public void run() {
                //Call the function again
                turretsToRenderLoop(onFinish);
            }
        });
    }

    /*
        SIMPLE UTILITIES
    */

    //Get real location from location in config.
    //Example: The map is built and saved with different coordinates in a different world.
    //When its pasted in the real game world. The location varies.
    public Location getRealLocation(Location mapLocation) {
        Location mapZero = map.getCorner1();
        double xDiffer = -(mapZero.getX() - mapLocation.getX());
        double yDiffer = -(mapZero.getY() - mapLocation.getY());
        double zDiffer = -(mapZero.getZ() - mapLocation.getZ());
        Location newLocation = zeroLocation.clone().add(xDiffer,yDiffer,zDiffer);
        newLocation.setYaw(mapLocation.getYaw());
        newLocation.setPitch(mapLocation.getPitch());
        return newLocation;
    }

    //Get the offset from in-game location. Used to save locations.
    public org.bukkit.util.Vector getOffset(Location ingameLocation) {
        double xDiffer = -(zeroLocation.getX() - ingameLocation.getX());
        double yDiffer = -(zeroLocation.getY() - ingameLocation.getY());
        double zDiffer = -(zeroLocation.getZ() - ingameLocation.getZ());
        return new org.bukkit.util.Vector(xDiffer,yDiffer,zDiffer);
    }
    public Location getLocationFromOffset(org.bukkit.util.Vector offset) {
        return zeroLocation
            .clone()
            .add(offset.getX(), offset.getY(), offset.getZ());
    }

    //Change the game gold value
    public void setGold(double amount) {
        this.gold = amount;
        this.updateStartHologram();
    }
    public void addGold(double amount) {
        this.setGold(this.getGold() + amount);
    }
    public void removeGold(double amount) {
        this.setGold(this.getGold() - amount);
    }

    public int getMobYLevel() {
        return this.mobPath.get(0).getBlockY();
    }
    public boolean isAlive() {
        return this.health > 0;
    }
    public boolean hasWonGame() {
        return !this.roundActive && this.currentRound >= difficulty.getRounds();
    }
    public boolean getGameSetting(GameSetting setting) {
        return this.gameSettings.get(setting);
    }
    public void setGameSetting(GameSetting setting, boolean value) {
        this.gameSettings.put(setting,value);
    }

}