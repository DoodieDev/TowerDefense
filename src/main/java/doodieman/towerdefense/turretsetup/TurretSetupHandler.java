package doodieman.towerdefense.turretsetup;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class TurretSetupHandler {

    public TurretSetupHandler() {

    }

    public void saveSchematic(String turretID) {

        FileConfiguration config = TowerDefense.getInstance().getConfig();
        String schematicPath = TowerDefense.getInstance().getDataFolder() + "/turrets/" + turretID + ".schematic";

        Location corner1 = LocationUtil.stringToLocation(config.getString("turrets."+turretID+".corner1"));
        Location corner2 = LocationUtil.stringToLocation(config.getString("turrets."+turretID+".corner2"));
        World world = corner1.getWorld();

        try {
            File file = new File(schematicPath);

            Vector minCorner = new Vector(corner1.getX(), corner1.getY(), corner1.getZ());
            Vector maxCorner = new Vector(corner2.getX(), corner2.getY(), corner2.getZ());

            CuboidRegion region = new CuboidRegion(new BukkitWorld(world), minCorner, maxCorner);

            Schematic schematic = new Schematic(region);
            schematic.save(file, ClipboardFormat.SCHEMATIC);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


}
