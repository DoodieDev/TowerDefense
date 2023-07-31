package doodieman.towerdefense.staff;

import doodieman.towerdefense.TowerDefense;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StaffListener implements Listener {

    final StaffHandler handler;

    public StaffListener(StaffHandler handler) {
        this.handler = handler;
        Bukkit.getPluginManager().registerEvents(this, TowerDefense.getInstance());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!handler.isInStaffMode(player)) return;
        handler.setStaffMode(player,false);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("staff.staffmode")) return;

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!handler.isInStaffMode(p)) continue;
            player.hidePlayer(p);
        }

    }

}
