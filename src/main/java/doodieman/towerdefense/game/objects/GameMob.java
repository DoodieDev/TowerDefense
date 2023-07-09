package doodieman.towerdefense.game.objects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.values.MobType;
import doodieman.towerdefense.utils.StringUtil;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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

    private ArmorStand healthArmorStand;

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
        this.healthArmorStand = null;
    }

    //Spawn the entity at the first path location
    public void spawn() {

        Location location = this.path.get(0);

        this.entity = game.getWorld().spawnEntity(location, mobType.getEntityType());
        mobType.getRunnable().run(this.entity);

        //Add NBT values to the entity
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) this.entity).getHandle();
        NBTTagCompound tag = new NBTTagCompound();
        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        nmsEntity.f(tag);

        this.updateHealthBar();
    }

    //Destroy the GameMob
    public void remove(boolean deathEffect) {
        if (!deathEffect) {
            this.entity.remove();
            this.healthArmorStand.remove();
            this.game.getAliveMobs().remove(this);
        } else {
            entity.playEffect(EntityEffect.DEATH);
            this.healthArmorStand.remove();
            this.game.getAliveMobs().remove(this);
            new BukkitRunnable() {
                @Override
                public void run() {
                    entity.remove();
                }
            }.runTaskLater(TowerDefense.getInstance(),5L);
        }

    }

    public void damage(double amount) {
        //Dead
        if (amount >= this.health) {
            this.remove(true);
            this.game.addGold(this.mobType.getGold());
        } else {
            entity.playEffect(EntityEffect.HURT);
            this.health -= amount;
            this.updateHealthBar();
        }
    }

    //Updates the health bar, and teleports it to the Entity
    public void updateHealthBar() {

        String text = "§4["+StringUtil.progressBar(health,mobType.getHealth(),20)+"§4]";
        Location location = this.entity.getLocation().clone().add(0,mobType.getHologramOffset(),0);

        if (healthArmorStand == null) {
            this.healthArmorStand = game.getWorld().spawn(location,ArmorStand.class);
            this.healthArmorStand.setCustomNameVisible(true);
            this.healthArmorStand.setVisible(false);
            this.healthArmorStand.setMarker(true);
            this.healthArmorStand.setGravity(false);
        }

        this.healthArmorStand.setCustomName(text);
        this.healthArmorStand.teleport(location);
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

        //Only fix head rotation every ~3 blocks, so it doesn't bug
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

    public Location getLocation() {
        return this.entity.getLocation();
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
