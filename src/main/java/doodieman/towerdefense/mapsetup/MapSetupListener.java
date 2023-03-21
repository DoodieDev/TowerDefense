package doodieman.towerdefense.mapsetup;

import doodieman.towerdefense.TowerDefense;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MapSetupListener implements Listener {

    final MapSetupHandler handler;

    public MapSetupListener(MapSetupHandler handler) {
        this.handler = handler;
        Bukkit.getPluginManager().registerEvents(this, TowerDefense.getInstance());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!handler.getEditingPath().containsKey(player)) return;
        handler.getEditingPath().remove(player);
        player.getInventory().clear();
    }

}
