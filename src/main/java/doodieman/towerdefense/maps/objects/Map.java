package doodieman.towerdefense.maps.objects;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.utils.LocationUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map {

    @Getter
    private final String mapName;

    @Getter
    private Location corner1;
    @Getter
    private Location corner2;
    @Getter
    private final List<Location> path;

    public Map(String mapName) {
        this.mapName = mapName;
        this.path = new ArrayList<>();
    }

    //Loads the map from the config
    public void load() {

        ConfigurationSection section = getSection();

        this.corner1 = LocationUtil.stringToLocation(section.getString("corner1"));
        this.corner2 = LocationUtil.stringToLocation(section.getString("corner2"));

        //Load the path
        ConfigurationSection pathSection = section.getConfigurationSection("path");
        for (String key : pathSection.getKeys(false)) {
            Location location = LocationUtil.stringToLocation(pathSection.getString(key));
            this.path.add(location);
        }

        System.out.println("[TowerDefense] Succesfully loaded map: '"+mapName+"'");
    }

    //Paste the map at specific location
    public void pasteSchematic(Location location) {
        try {
            File file = new File(this.getSchematicPath());
            Vector pasteLocation = new Vector(location.getX(), location.getY(), location.getZ());
            com.sk89q.worldedit.world.World pasteWorld = new BukkitWorld(location.getWorld());
            WorldData pasteWorldData = pasteWorld.getWorldData();

            Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(file)).read(pasteWorldData);
            ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard, pasteWorldData);
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(pasteWorld, -1);

            Operation operation = clipboardHolder
                .createPaste(editSession, pasteWorldData)
                .to(pasteLocation)
                .ignoreAirBlocks(false)
                .build();

            Operations.completeLegacy(operation);
            editSession.flushQueue();

        } catch (IOException | MaxChangedBlocksException exception) {
            exception.printStackTrace();
        }
    }

    //Gets the path length
    public double getPathLength() {
        double length = 0d;
        for (int i = 0; i < path.size()-1; i++) {
            Location location = path.get(i);
            Location next = path.get(i+1);
            length += location.distance(next);
        }
        return length;
    }

    public Location getPathLocationAt(double targetLength) {
        double length = 0d;
        Location selected = path.get(0);

        for (int i = 0; i < path.size()-1; i++) {
            Location location = path.get(i);
            Location next = path.get(i+1);

            //It will exceed the target length
            if (location.distance(next) + length > targetLength) {
                double lengthLeft = (targetLength - length);
                selected = location.clone();
                selected.add(moveCloser(location,next,length));
            }

            length += location.distance(next);
        }
        return selected;
    }

    public Location moveCloser(Location loc1, Location loc2, double distance) {
        double dx = loc2.getX() - loc1.getX(); // Calculate the difference on the X-Axis
        double dy = loc2.getY() - loc1.getY(); // Calculate the difference on the Y-Axis
        double dz = loc2.getZ() - loc1.getZ(); // Calculate the difference on the Z-Axis

        double length = Math.sqrt(dx * dx + dy * dy + dz * dz); // Calculate the length of the vector between the two points

        if (length <= distance) { // If the distance is greater than or equal to the distance we want to move
            return loc2.clone(); // Return a copy of the second location
        }

        // Calculate the new coordinates
        double newX = loc1.getX() + dx * (distance / length);
        double newY = loc1.getY() + dy * (distance / length);
        double newZ = loc1.getZ() + dz * (distance / length);

        return new Location(loc1.getWorld(), newX, newY, newZ); // Return a new location with the new coordinates
    }

    public String getSchematicPath() {
        return TowerDefense.getInstance().getDataFolder() + "/maps/" + mapName + ".schematic";
    }

    public ConfigurationSection getSection() {
        return getConfig().getConfigurationSection("maps."+mapName);
    }
    public FileConfiguration getConfig() {
        return TowerDefense.getInstance().getConfig();
    }

}
