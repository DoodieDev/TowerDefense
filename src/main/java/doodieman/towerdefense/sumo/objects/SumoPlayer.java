package doodieman.towerdefense.sumo.objects;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SumoPlayer {

    @Getter
    final Player player;
    @Getter
    final Location spawnLocation;

    public SumoPlayer(Player player, Location spawnLocation) {
        this.player = player;
        this.spawnLocation = spawnLocation;
    }

}
