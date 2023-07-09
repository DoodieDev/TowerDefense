package doodieman.towerdefense.simpleevents.region;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.utils.WorldGuardUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Set;

public class RegionListener implements Listener {

    public RegionListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, TowerDefense.getInstance());
    }

    @EventHandler
    public void onAnyMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location loc_to = event.getTo();
        Location loc_from = event.getFrom();

        Set<ProtectedRegion> regions_to = WorldGuardUtil.getRegionsAt(loc_to);
        Set<ProtectedRegion> regions_from = WorldGuardUtil.getRegionsAt(loc_from);

        if (!regions_to.equals(regions_from)) {

            for (ProtectedRegion region : regions_to) {

                //Moved into new region
                if (!regions_from.contains(region)) {
                    //TODO call event
                    RegionEnterEvent regionEnterEvent= new RegionEnterEvent(player, region);
                    Bukkit.getPluginManager().callEvent(regionEnterEvent);

                    if (regionEnterEvent.isCancelled())
                        event.setCancelled(true);

                } else regions_from.remove(region);
            }

            //Left region
            if (regions_from.size() > 0) {
                for (ProtectedRegion region : regions_from) {
                    //TODO call event
                    RegionLeaveEvent regionLeaveEvent= new RegionLeaveEvent(player, region);
                    Bukkit.getPluginManager().callEvent(regionLeaveEvent);

                    if (regionLeaveEvent.isCancelled()) event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        Location loc_to = event.getTo();
        Location loc_from = event.getFrom();

        Set<ProtectedRegion> regions_to = WorldGuardUtil.getRegionsAt(loc_to);
        Set<ProtectedRegion> regions_from = WorldGuardUtil.getRegionsAt(loc_from);

        if (!regions_to.equals(regions_from)) {

            for (ProtectedRegion region : regions_to) {

                //Moved into new region
                if (!regions_from.contains(region)) {
                    //TODO call event
                    RegionEnterEvent regionEnterEvent= new RegionEnterEvent(player, region);
                    Bukkit.getPluginManager().callEvent(regionEnterEvent);

                    if (regionEnterEvent.isCancelled())
                        event.setCancelled(true);

                } else regions_from.remove(region);
            }

            //Left region
            if (regions_from.size() > 0) {
                for (ProtectedRegion region : regions_from) {
                    //TODO call event
                    RegionLeaveEvent regionLeaveEvent= new RegionLeaveEvent(player, region);
                    Bukkit.getPluginManager().callEvent(regionLeaveEvent);

                    if (regionLeaveEvent.isCancelled()) event.setCancelled(true);
                }
            }
        }
    }

}
