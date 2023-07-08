package doodieman.towerdefense.lobby.water;

import com.boydti.fawe.bukkit.util.BukkitReflectionUtils;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.lobby.spawn.SpawnUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class WaterHandler implements Listener {

    @Getter
    private final Map<Player, Location> lastLocations = new HashMap<>();

    @Getter
    private static WaterHandler instance;

    public WaterHandler() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(this, TowerDefense.getInstance());

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    saveLocation(player);
                }
            }
        }.runTaskTimer(TowerDefense.getInstance(),0L,5L);
    }

    public void saveLocation(Player player) {
        if (!player.isOnGround()) return;
        if (player.getLocation().getY() < 50) return;
        if (player.getGameMode() != GameMode.ADVENTURE) return;
        if (!player.getLocation().getWorld().getName().equalsIgnoreCase("world")) return;

        Location location = player.getLocation();
        Material below = location.getWorld().getBlockAt(location.clone().add(0,-1,0)).getType();
        if (below == null || below == Material.AIR) return;

        lastLocations.put(player,location);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        lastLocations.remove(player);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        if (player.getGameMode() != GameMode.ADVENTURE) return;
        if (location.getY() > 50) return;
        if (!location.getWorld().getName().equalsIgnoreCase("world")) return;

        if (lastLocations.containsKey(player))
            player.teleport(lastLocations.get(player));

        else
            player.teleport(SpawnUtil.getSpawn());
    }

}
