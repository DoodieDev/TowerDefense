package doodieman.towerdefense.game.objects;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.mapgrid.MapGridHandler;
import doodieman.towerdefense.mapgrid.objects.GridLocation;
import doodieman.towerdefense.maps.MapUtil;
import doodieman.towerdefense.maps.objects.Map;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

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
    private final World world;
    @Getter
    private final Location zeroLocation;
    @Getter
    private final List<Location> mobPath;

    public Game(OfflinePlayer player, Map map) {
        this.player = player;
        this.map = map;
        this.mobPath = new ArrayList<>();
        this.world = MapUtil.getInstance().getGameWorld();
        this.gridLocation = MapGridHandler.getInstance().generateLocation();
        this.gridLocation.register();
        this.zeroLocation = this.gridLocation.getLocation(this.world);
    }

    //Prepares the game, pasting schematic, etc
    public void prepare() {
        map.pasteSchematic(zeroLocation);
        for (Location location : map.getPath())
            this.mobPath.add(getRealLocation(location));
    }

    //Starts the game. Teleports player, etc.
    public void start() {
        player.getPlayer().teleport(getCenter());
    }

    //Stops the game. Removing schematic, teleport player to spawn, etc.
    public void stop() {
        gridLocation.unregister();
        //TODO delete schematic, teleport player to spawn
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
