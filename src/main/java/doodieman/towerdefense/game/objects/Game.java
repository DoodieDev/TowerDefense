package doodieman.towerdefense.game.objects;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.interactive.GameInteractive;
import doodieman.towerdefense.game.values.Difficulty;
import doodieman.towerdefense.game.values.MobType;
import doodieman.towerdefense.game.values.Round;
import doodieman.towerdefense.mapgrid.MapGridHandler;
import doodieman.towerdefense.mapgrid.objects.GridLocation;
import doodieman.towerdefense.maps.MapUtil;
import doodieman.towerdefense.maps.objects.Map;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
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
    private final World world;
    @Getter
    private final Location zeroLocation;
    @Getter
    private final List<Location> mobPath;
    BukkitTask mobPathLoop;

    //Normal game values being used globally
    @Getter
    private double health;
    @Getter @Setter
    private double gold;
    @Getter
    private final Difficulty difficulty;
    @Getter //List of active mobs on the path
    private final List<GameMob> aliveMobs;
    @Getter //Check if the round is active
    private boolean roundActive;
    @Getter //Current round number
    private int currentRound;

    public Game(OfflinePlayer player, Map map, Difficulty difficulty) {
        this.player = player;
        this.map = map;
        this.gameInteractive = new GameInteractive(player, this);
        this.mobPath = new ArrayList<>();
        this.world = MapUtil.getInstance().getGameWorld();
        this.gridLocation = MapGridHandler.getInstance().generateLocation();
        this.gridLocation.register();
        this.zeroLocation = this.gridLocation.getLocation(this.world);

        this.difficulty = difficulty;
        this.health = difficulty.getHealth();
        this.gold = difficulty.getStartingGold();
        this.aliveMobs = new ArrayList<>();
        this.roundActive = false;
        this.currentRound = 1;
    }

    //Prepares the game, pasting schematic, etc
    public void prepare() {
        //Paste schematic async
        TowerDefense.runAsync(new BukkitRunnable() {
            @Override
            public void run() {
                map.pasteSchematic(zeroLocation);
            }
        });
        //Load the mob path
        for (Location location : map.getPath())
            this.mobPath.add(getRealLocation(location));
    }

    //Starts the game. Teleports player, etc.
    public void start() {
        this.player.getPlayer().teleport(mobPath.get(0));
        this.gameInteractive.register();
        this.startLoop();
    }

    //Stops the game. Removing schematic, teleport player to spawn, etc.
    public void stop(boolean removeSchematic) {
        this.roundActive = false;
        this.mobPathLoop.cancel();
        this.gridLocation.unregister();
        this.gameInteractive.unregister();

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
            @Override
            public void run() {
                if (!roundActive) return;

                //Move all the mobs
                for (GameMob mob : aliveMobs) {
                    mob.move();
                    if (mob.isInGoal())
                        mobsToRemove.add(mob);
                }

                //Remove mobs in goal
                for (GameMob mob : mobsToRemove) {
                    damage(mob.getMobType().getDamage());
                    gameInteractive.getGameAnimations().mobFinished(mob.getEntity().getLocation(), mob.getMobType());
                    mob.kill();
                    aliveMobs.remove(mob);
                }

                mobsToRemove.clear();

                //Check if round is over
                if (aliveMobs.size() <= 0)
                    finishRound();

            }
        }.runTaskTimer(TowerDefense.getInstance(), 0L, 1L);
    }

    //Spawns a mob that automatically will go on the path
    public void spawnMob(MobType mobType) {
        GameMob mob = new GameMob(this, mobType);
        mob.spawn();
        aliveMobs.add(mob);
        gameInteractive.getGameAnimations().mobSpawned(mob.getEntity().getLocation());
    }

    //Starts the round
    public void startRound() {
        this.roundActive = true;
        this.gameInteractive.getGameAnimations().newRoundStarted();

        Round round = Round.getRound(currentRound);

        //Spawn the mobs slowly
        int i = 0;
        for (MobType mobType : round.getMobs()) {
            new BukkitRunnable() {
                @Override
                public void run() {

                    if (roundActive)
                        spawnMob(mobType);

                }
            }.runTaskLater(TowerDefense.getInstance(), i * round.getSpawnDelay());
            i++;
        }
    }

    //Called when the round is finished
    public void finishRound() {
        this.roundActive = false;
        this.wipeMobs();
        gameInteractive.updateRoundItemSlot();
        currentRound++;
    }

    //Removes all the active mobs
    public void wipeMobs() {
        for (GameMob mob : aliveMobs)
            mob.kill();
        aliveMobs.clear();
    }

    //When a mobs damages the player
    public void damage(double amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }


    //Gets the exact center of the map
    public Location getCenter() {
        Location corner1 = map.getCorner1();
        Location corner2 = map.getCorner2();
        double xDiffer = (corner2.getX() - corner1.getX())/2;
        double zDiffer = (corner2.getZ() - corner1.getZ())/2;
        return getRealLocation(new Location(world, corner1.getX() + xDiffer, 10, corner1.getZ() + zDiffer));
    }

    //Get real location from location in config.
    //Example: The map is built and saved with different coordinates in a different world.
    //When its pasted in the real game world. The location varies.
    public Location getRealLocation(Location location) {
        Location mapZero = map.getCorner1();
        double xDiffer = -(mapZero.getX() - location.getX());
        double yDiffer = -(mapZero.getY() - location.getY());
        double zDiffer = -(mapZero.getZ() - location.getZ());
        Location newLocation = zeroLocation.clone().add(xDiffer,yDiffer,zDiffer);
        newLocation.setYaw(location.getYaw());
        newLocation.setPitch(location.getPitch());
        return newLocation;
    }

}
