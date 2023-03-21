package doodieman.towerdefense.game.objects;

import lombok.Generated;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

public class GameMob {

    @Getter
    private final Game game;
    @Getter
    private final MobType mobType;
    @Getter
    private final List<Location> path;

    final double speed;
    final double pathLength;
    double currentLength;

    int pointIndex;
    Location currentPoint;
    Location nextPoint;

    @Getter
    private Entity entity;

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
    }

    public void spawn() {
        this.entity = game.getWorld().spawnEntity(path.get(0), mobType.getEntityType());
        //NO AI

        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) this.entity).getHandle();
        NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) tag = new NBTTagCompound();
        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        nmsEntity.f(tag);
    }

    public void move() {
        currentLength += speed;
        double distanceLeft = entity.getLocation().distance(nextPoint);
        Location closerLocation;

        //Distance left to move is less than the speed.
        if (distanceLeft < speed) {
            this.setNextPoint();
            closerLocation = moveCloser(entity.getLocation(), nextPoint, speed - distanceLeft);
            this.fixHeadRotation();
        } else {
            closerLocation = moveCloser(entity.getLocation(), nextPoint, speed);
        }

        closerLocation.setYaw(currentPoint.getYaw());
        entity.teleport(closerLocation);
    }

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

    private void setNextPoint() {
        pointIndex++;
        if (pointIndex >= path.size()-1) return;
        this.currentPoint = path.get(pointIndex);
        this.nextPoint = path.get(pointIndex+1);
    }

    public boolean isInGoal() {
        return currentLength >= pathLength;
    }


    private Location moveCloser(Location loc1, Location loc2, double distance) {
        double dx = loc2.getX() - loc1.getX(); // Calculate the difference on the X-Axis
        double dy = loc2.getY() - loc1.getY(); // Calculate the difference on the Y-Axis
        double dz = loc2.getZ() - loc1.getZ(); // Calculate the difference on the Z-Axis

        double length = Math.sqrt(dx * dx + dy * dy + dz * dz); // Calculate the length of the vector between the two points

        if (length <= distance) { // If the distance is greater than or equal to the distance we want to move
            return loc2.clone(); // Return a copy of the second location
        }

        // Calculate the new coordinates
        double newX = loc1.getX() + dx * (distance / length);
        double newY = loc1.getY() + dy * (distance / length);
        double newZ = loc1.getZ() + dz * (distance / length);
        return new Location(loc1.getWorld(), newX, newY, newZ);
    }



}
