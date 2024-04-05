package doodieman.towerdefense.discord;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.buycraft.menu.BuycraftMenu;
import doodieman.towerdefense.utils.WorldGuardUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class DiscordHandler implements Listener {

    public DiscordHandler() {
        Bukkit.getPluginManager().registerEvents(this, TowerDefense.getInstance());
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Location location = event.getRightClicked().getLocation();

        if (!WorldGuardUtil.isAtRegion(location,"discord")) return;

        Bukkit.dispatchCommand(player,"discord");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null) return;
        Location location = event.getClickedBlock().getLocation();

        if (!WorldGuardUtil.isAtRegion(location,"discord")) return;

        Bukkit.dispatchCommand(player,"discord");
    }

}
