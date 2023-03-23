package doodieman.towerdefense.game.objects;

import doodieman.towerdefense.game.values.MobType;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.MojangsonParseException;
import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class GameMob {

    @Getter
    private final Game game;
    @Getter
    private final MobType mobType;
    @Getter
    private final List<Location> path;

    //Variables used for the pathfinding
    final double speed; //How fast the Mob moves
    final double pathLength; //How long the whole path is on the map, from start to finish
    double currentLength; //How long has the mob traveled so far
    int pointIndex; //Index in the 'path' list of locations
    Location currentPoint; //Current path location
    Location nextPoint; //Next path location (The one it's moving towards)

    @Getter
    private Entity entity;

    //Health stuff
    @Getter
    private double health;

    public GameMob(Game game, MobType mobType) {
        this.game = game;
        this.mobType = mobType;
        this.path = game.getMobPath();
        this.speed = mobType.getSpeed();
        this.pathLength = game.getMap().getPathLength();
        this.currentLength = 0;
        this.pointIndex = 0;
        this.currentPoint = path.get(pointIndex);
        this.nextPoint = path.get(pointIndex+1);

        this.health = mobType.getHealth();
    }

    //Spawn the entity at the first path location
    public void spawn() {
        //Spawn the actual entity
        this.entity = game.getWorld().spawnEntity(path.get(0), mobType.getEntityType());

        //Add NBT values to the entity
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) this.entity).getHandle();
        NBTTagCompound tag = new NBTTagCompound();
        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        nmsEntity.f(tag);

        mobType.getRunnable().run(entity);
    }

    public void kill() {
        this.entity.remove();
    }

    //Updates the health bar, and teleports it to the Entity
    public void updateHealthBar() {
        this.entity.setCustomName("§7"+health+" §c❤");
    }

    //Moves the entity on the path, it moves (speed) blocks
    public void move() {
        currentLength += speed;
        double distanceLeft = entity.getLocation().distance(nextPoint);
        Location closerLocation;

        //Turn on the path
        if (distanceLeft < speed) {
            pointIndex++;
            if (pointIndex >= path.size()-1) return;
            this.currentPoint = path.get(pointIndex);
            this.nextPoint = path.get(pointIndex+1);

            closerLocation = moveCloser(entity.getLocation(), nextPoint, speed - distanceLeft);

        } else closerLocation = moveCloser(entity.getLocation(), nextPoint, speed);

        closerLocation.setYaw(currentPoint.getYaw());
        entity.teleport(closerLocation);
        if (Math.round(currentLength) % 3 == 0)
            this.fixHeadRotation();

        this.updateHealthBar();
    }

    //Fixes the head rotation, it bugs just teleporting to yaw
    private void fixHeadRotation() {
        PacketPlayOutEntityHeadRotation Rotation = new PacketPlayOutEntityHeadRotation();
        try {
            FieldUtils.writeField(Rotation, "a", ((CraftEntity) entity).getHandle().getId(), true);
            FieldUtils.writeField(Rotation, "b", (byte) (currentPoint.getYaw() * 256.0F / 360.0F), true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(Rotation);
        }
    }

    //Check if the mob is in goal
    public boolean isInGoal() {
        return currentLength >= pathLength;
    }

    //Moves location1 closer towards location2
    private Location moveCloser(Location loc1, Location loc2, double distance) {
        double dx = loc2.getX() - loc1.getX();
        double dy = loc2.getY() - loc1.getY();
        double dz = loc2.getZ() - loc1.getZ();
        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (length <= distance) return loc2.clone();
        double newX = loc1.getX() + dx * (distance / length);
        double newY = loc1.getY() + dy * (distance / length);
        double newZ = loc1.getZ() + dz * (distance / length);
        return new Location(loc1.getWorld(), newX, newY, newZ);
    }



}
