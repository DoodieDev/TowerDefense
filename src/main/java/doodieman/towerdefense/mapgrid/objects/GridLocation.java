package doodieman.towerdefense.mapgrid.objects;

import doodieman.towerdefense.mapgrid.MapGridHandler;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

public class GridLocation {

    @Getter
    private final int x, z;

    final MapGridHandler handler;

    public GridLocation(int x, int z, MapGridHandler handler) {
        this.x = x;
        this.z = z;
        this.handler = handler;
    }

    public Location getLocation(World world) {
        return new Location(world, this.x * handler.getScale(), 0, this.z * handler.getScale());
    }

    public void register() {
        handler.getGridLocations().add(this);
    }

    public void unregister() {
        handler.getGridLocations().remove(this);
    }

}
