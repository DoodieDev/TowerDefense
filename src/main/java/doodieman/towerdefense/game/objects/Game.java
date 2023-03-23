package doodieman.towerdefense.game.objects;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.values.Difficulty;
import doodieman.towerdefense.game.values.MobType;
import doodieman.towerdefense.mapgrid.MapGridHandler;
import doodieman.towerdefense.mapgrid.objects.GridLocation;
import doodieman.towerdefense.maps.MapUtil;
import doodieman.towerdefense.maps.objects.Map;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Game {

    @Getter
    private final OfflinePlayer player;
    @Getter
    private final Map map;
    @Getter
    private final Difficulty difficulty;
    @Getter
    private double health;
    @Getter
    private final GridLocation gridLocation;
    @Getter
    private final World world;
    @Getter
    private final Location zeroLocation;
    @Getter
    private final List<Location> mobPath;

    public Game(OfflinePlayer player, Map map, Difficulty difficulty) {
        this.player = player;
        this.map = map;
        this.difficulty = difficulty;
        this.health = difficulty.getHealth();

        this.mobPath = new ArrayList<>();
        this.world = MapUtil.getInstance().getGameWorld();
        this.gridLocation = MapGridHandler.getInstance().generateLocation();
        this.gridLocation.register();
        this.zeroLocation = this.gridLocation.getLocation(this.world);
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
        player.getPlayer().teleport(mobPath.get(0));
    }

    //Stops the game. Removing schematic, teleport player to spawn, etc.
    public void stop() {
        gridLocation.unregister();


        //Remove schematic
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

    //Starts a wave of monsters
    public void startRound() {

        GameMob mob = new GameMob(this, MobType.ZOMBIE);
        mob.spawn();

        new BukkitRunnable() {
            @Override
            public void run() {
                mob.move();
                if (mob.isInGoal()) {
                    this.cancel();
                    mob.kill();
                }
            }
        }.runTaskTimer(TowerDefense.getInstance(), 0L, 1L);
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
