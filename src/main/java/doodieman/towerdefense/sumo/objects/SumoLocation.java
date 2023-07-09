package doodieman.towerdefense.sumo.objects;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum SumoLocation {

    LOSERLOBBY(new Location(Bukkit.getWorld("world"),1237, 56, 1024, -90, 0)),
    WINLOBBY(new Location(Bukkit.getWorld("world"),1224.5, 61, 1036.5, -135, 10)),

    CENTER(new Location(Bukkit.getWorld("world"),1217.5, 61, 1021.5)),
    PLAYER1(new Location(Bukkit.getWorld("world"),1220.5,61,1024.5, 135, 0)),
    PLAYER2(new Location(Bukkit.getWorld("world"),1214.5,61,1018.5, -45, 0));

    @Getter
    private final Location location;

    SumoLocation(Location location) {
        this.location = location;
    }

}
