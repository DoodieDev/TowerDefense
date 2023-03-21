package doodieman.towerdefense.mapgrid;

import doodieman.towerdefense.mapgrid.objects.GridLocation;
import lombok.Getter;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class MapGridHandler {

    //How far apart should the grid locations be from eachother?
    @Getter
    private final int scale;
    @Getter
    private final List<GridLocation> gridLocations;

    @Getter
    private static MapGridHandler instance;

    public MapGridHandler() {
        this.scale = 1;
        this.gridLocations = new ArrayList<>();
        instance = this;
    }

    public boolean locationExists(int x, int z) {
        return gridLocations.stream()
            .anyMatch(gridLocation -> gridLocation.getX() == x && gridLocation.getZ() == z);
    }

    public GridLocation generateLocation() {
        int x = 0;
        int z = 0;
        while (locationExists(x, z)) {
            if (x == 0) {
                x = z + 1;
                z = 0;
            } else {
                z++;
                x--;
            }
        }
        return new GridLocation(x, z, this);
    }



}
