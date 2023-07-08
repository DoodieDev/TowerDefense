package doodieman.towerdefense.lobby.bench;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import doodieman.towerdefense.lobby.bench.events.BenchLeaveEvent;
import doodieman.towerdefense.lobby.bench.events.BenchSitEvent;
import doodieman.towerdefense.utils.WorldGuardUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.Set;

public class BenchListener implements Listener {

    final BenchHandler handler;

    public BenchListener(BenchHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = event.getClickedBlock();
        Location clickedLocation = clickedBlock.getLocation();

        if (!handler.isBench(clickedLocation)) return;
        if (handler.getBenchSpot(player) != null) return;
        if (handler.getBenchSpot(clickedBlock) != null) return;

        String benchID = handler.getBenchID(clickedLocation);

        BenchSpot spot = new BenchSpot(player, clickedBlock, handler, benchID);
        BenchSitEvent sitEvent = new BenchSitEvent(player, spot);
        Bukkit.getPluginManager().callEvent(sitEvent);

        if (!sitEvent.isCancelled())
            spot.startSitting();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BenchSpot benchSpot = handler.getBenchSpot(player);
        if (benchSpot == null) return;

        BenchLeaveEvent leaveEvent = new BenchLeaveEvent(player,benchSpot);
        Bukkit.getPluginManager().callEvent(leaveEvent);

        benchSpot.stopSitting();
    }

    @EventHandler
    public void onBenchLeave(EntityDismountEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDismounted() instanceof ArmorStand)) return;

        Player player = (Player) event.getEntity();
        BenchSpot benchSpot = handler.getBenchSpot(player);
        if (benchSpot == null) return;

        BenchLeaveEvent leaveEvent = new BenchLeaveEvent(player,benchSpot);
        Bukkit.getPluginManager().callEvent(leaveEvent);

        benchSpot.stopSitting();
    }

}
