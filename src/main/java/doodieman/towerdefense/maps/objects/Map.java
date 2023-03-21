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
