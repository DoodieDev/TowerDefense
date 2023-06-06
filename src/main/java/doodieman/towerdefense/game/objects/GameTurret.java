package doodieman.towerdefense.game.objects;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.values.TurretType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GameTurret {

    @Getter
    private final Game game;
    @Getter
    private final TurretType turretType;
    @Getter
    private final Location location;
    @Getter @Setter
    private double rotation;

    public GameTurret(Game game, TurretType turretType, Location location) {
        this.game = game;
        this.turretType = turretType;
        this.location = location;
        this.rotation = 0;
    }

    //Render the turret. (Schematic, hologram, etc)
    public void render() {
        this.pasteSchematic();

        //TODO create hologram
    }

    //Get the zero location for pasting schematic
    public Location getSchematicLocation() {
        //All turret-schematics are 3x3 and start 2 blocks under the ground.
        //Dont mind this code it is very ugly and hardcoded
        Location loc = this.location.clone();
        loc.add(0, -4, 0);
        if (rotation == 0 || rotation == 360)
            loc.add(-1,0,-1);
        else if (rotation == 90)
            loc.add(1,0,-1);
        else if (rotation == 180)
            loc.add(1,0,1);
        else if (rotation == 270)
            loc.add(-1,0,1);
        return loc;
    }

    //Get the path to turret schematic
    public String getSchematicPath() {
        return TowerDefense.getInstance().getDataFolder() + "/turrets/" + turretType.getId() + ".schematic";
    }

    //Paste the schematic
    public void pasteSchematic() {
        TowerDefense.runAsync(() -> {
            Location loc = this.getSchematicLocation();
            try {
                File file = new File(this.getSchematicPath());
                Vector pasteLocation = new Vector(loc.getX(), loc.getY(), loc.getZ());

                com.sk89q.worldedit.world.World pasteWorld = new BukkitWorld(loc.getWorld());
                WorldData pasteWorldData = pasteWorld.getWorldData();

                Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(file)).read(pasteWorldData);
                ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard, pasteWorldData);
                EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(pasteWorld, -1);

                clipboardHolder.setTransform(clipboardHolder.getTransform().combine(new AffineTransform().rotateY(-rotation )));

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
        });
    }

}
