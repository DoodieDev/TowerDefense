package doodieman.towerdefense.lobby.spawn;

import doodieman.towerdefense.TowerDefense;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SpawnHandler implements Listener {

    public SpawnHandler() {
        Bukkit.getPluginManager().registerEvents(this, TowerDefense.getInstance());
    }


}
