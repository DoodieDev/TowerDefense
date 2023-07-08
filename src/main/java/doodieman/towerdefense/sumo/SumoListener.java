package doodieman.towerdefense.sumo;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.lobby.bench.BenchSpot;
import doodieman.towerdefense.lobby.bench.events.BenchLeaveEvent;
import doodieman.towerdefense.lobby.bench.events.BenchSitEvent;
import doodieman.towerdefense.simpleevents.region.RegionEnterEvent;
import doodieman.towerdefense.simpleevents.region.RegionLeaveEvent;
import doodieman.towerdefense.sumo.objects.SumoPlayer;
import doodieman.towerdefense.sumo.objects.SumoState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SumoListener implements Listener {

    final SumoHandler handler;

    public SumoListener(SumoHandler handler) {
        this.handler = handler;
        Bukkit.getPluginManager().registerEvents(this, TowerDefense.getInstance());
    }

    @EventHandler
    public void onBenchSit(BenchSitEvent event) {
        Player player = event.getPlayer();
        BenchSpot benchSpot = event.getBenchSpot();

        if (!handler.getSumoBenchIDS().contains(benchSpot.getBenchID())) return;
        if (handler.getWaitingPlayers().contains(player)) return;

        handler.getWaitingPlayers().add(player);
        player.sendMessage("§aDu er nu med i sumo-køen! Afvent det bliver din tur.");
    }

    @EventHandler
    public void onBenchLeave(BenchLeaveEvent event) {
        Player player = event.getPlayer();
        if (!handler.getWaitingPlayers().contains(player)) return;

        handler.getWaitingPlayers().remove(player);
        player.sendMessage("§cDu er ikke længere med i sumo-køen!");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (handler.getState() != SumoState.COUNTDOWN) return;
        if (!SumoUtil.getInstance().isInGame(player)) return;

        SumoPlayer sumoPlayer = SumoUtil.getInstance().getSumoPlayer(player);

        if (player.getLocation().getX() != sumoPlayer.getSpawnLocation().getX() ||
            player.getLocation().getY() != sumoPlayer.getSpawnLocation().getY() ||
            player.getLocation().getZ() != sumoPlayer.getSpawnLocation().getZ())
        {
            Location teleportLoc = sumoPlayer.getSpawnLocation().clone();
            teleportLoc.setYaw(player.getLocation().getYaw());
            teleportLoc.setPitch(player.getLocation().getPitch());
            player.teleport(teleportLoc);
        }

    }

    @EventHandler
    public void onRegionLeave(RegionLeaveEvent event) {
        if (!event.getRegion().getId().equalsIgnoreCase("sumo")) return;

        Player player = event.getPlayer();
        if (!SumoUtil.getInstance().isInGame(player)) return;
        if (handler.getState() != SumoState.FIGHTING) return;

        SumoUtil.getInstance().stopGame(player);
    }

    @EventHandler
    public void onRegionEnter(RegionEnterEvent event) {
        if (!event.getRegion().getId().equalsIgnoreCase("sumo")) return;

        Player player = event.getPlayer();
        if (SumoUtil.getInstance().isInGame(player) || player.isOp()) return;

        event.setCancelled(true);
        player.sendMessage("§cDu kan ikke bevæge dig ind i sumo zonen!");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!SumoUtil.getInstance().isInGame(player)) return;

        SumoUtil.getInstance().stopGame(player);
    }

}
