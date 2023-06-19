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
import doodieman.towerdefense.utils.LocationUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameTurret {

    @Getter
    private final Game game;
    @Getter
    private final TurretType turretType;
    @Getter
    private final Location location;
    @Getter @Setter
    private double rotation;
    @Getter
    private final List<GameTurretArmorstand> armorStandList = new ArrayList<>();

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
    public Location getZeroLocation() {
        Location loc = this.location.clone();
        loc.setX(Math.floor(loc.getX()));
        loc.setZ(Math.floor(loc.getZ()));
        loc.setY(1);
        return loc;
    }

    //Get the path to turret schematic
    public String getSchematicPath() {
        return TowerDefense.getInstance().getDataFolder() + "/turrets/" + turretType.getId() + ".schematic";
    }

    //Paste the schematic
    public void pasteSchematic() {
        Location loc = this.getZeroLocation();
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
                .ignoreEntities(true)
                .to(pasteLocation)
                .ignoreAirBlocks(false)
                .build();

            Operations.completeLegacy(operation);
            editSession.flushQueue();

            //TowerDefense.runSync(this::pasteArmorStands);

        } catch (IOException | MaxChangedBlocksException exception) {
            exception.printStackTrace();
        }
    }

    //Paste all the armorstands
    public void pasteArmorStands() {

        Location centerLocation = this.getZeroLocation().add(0.5,0,0.5);
        ConfigurationSection section = getConfigSection().getConfigurationSection("armorstands");

        for (String id : section.getKeys(false)) {

            ConfigurationSection asSection = section.getConfigurationSection(id);

            Location realLocation = this.getRealLocation(LocationUtil.stringToLocation(asSection.getString("location")));
            ArmorStand armorStand = realLocation.getWorld().spawn(realLocation,ArmorStand.class);

            armorStand.setGravity(false);
            armorStand.setBasePlate(false);
            armorStand.setVisible(false);

            armorStand.setSmall(asSection.getBoolean("small",false));

            armorStand.setHeadPose(LocationUtil.stringToEulerAngle(asSection.getString("head")));
            armorStand.setBodyPose(LocationUtil.stringToEulerAngle(asSection.getString("body")));
            armorStand.setRightArmPose(LocationUtil.stringToEulerAngle(asSection.getString("rightArm")));
            armorStand.setLeftArmPose(LocationUtil.stringToEulerAngle(asSection.getString("leftArm")));
            armorStand.setRightLegPose(LocationUtil.stringToEulerAngle(asSection.getString("rightLeg")));
            armorStand.setLeftLegPose(LocationUtil.stringToEulerAngle(asSection.getString("leftLeg")));

            armorStand.setHelmet(asSection.getItemStack("equipment.helmet"));
            armorStand.setChestplate(asSection.getItemStack("equipment.chestplate"));
            armorStand.setLeggings(asSection.getItemStack("equipment.leggings"));
            armorStand.setBoots(asSection.getItemStack("equipment.boots"));
            armorStand.setItemInHand(asSection.getItemStack("equipment.tool"));

            //Save the GameTurretArmorstand
            String tags = asSection.getString("tags", "");
            GameTurretArmorstand customArmorStand = new GameTurretArmorstand(armorStand,turretType,id,tags);
            customArmorStand.setStaticRotation(LocationUtil.getAngleToLocation(centerLocation, realLocation));
            customArmorStand.setStaticYaw(realLocation.getYaw());

            this.armorStandList.add(customArmorStand);
        }

    }

    //Update the armorstand positions.
    public void updateArmorStands() {
        Location center = this.getZeroLocation().add(0.5,0,0.5);

        for (GameTurretArmorstand turretArmorstand : this.getArmorStandList()) {
            if (turretArmorstand.getTags().contains("norotation")) continue;

            ArmorStand armorStand = turretArmorstand.getArmorStand();
            Location asLocation = armorStand.getLocation();

            //Get the radius from center to the armorstand
            Location radiusLocation = asLocation.clone();
            radiusLocation.setY(center.getY());
            double radius = center.distance(radiusLocation);

            //Rotate location accordingly to the center
            double finalRotation = turretArmorstand.getStaticRotation() + rotation;
            finalRotation = finalRotation % 360;
            Location rotatedLocation = LocationUtil.getLocationInCircle(center,finalRotation,radius);
            rotatedLocation.setY(asLocation.getY());

            //Rotate the location YAW
            rotatedLocation.setYaw(turretArmorstand.getStaticYaw() + (float) rotation);

            //Render
            armorStand.teleport(rotatedLocation);
        }

    }

    //Get real location from location in config.
    //Example: The turret is built and saved with different coordinates in a different world.
    //When its pasted in the real game world. The location varies.
    public Location getRealLocation(Location location) {
        ConfigurationSection section = this.getConfigSection();
        String corner1String = section.getString("corner1");
        Location corner1 = LocationUtil.stringToLocation(corner1String);
        double xDiffer = -(corner1.getX() - location.getX());
        double yDiffer = -(corner1.getY() - location.getY());
        double zDiffer = -(corner1.getZ() - location.getZ());
        Location newLocation = getZeroLocation().clone().add(xDiffer,yDiffer,zDiffer);
        newLocation.setYaw(location.getYaw());
        newLocation.setPitch(location.getPitch());
        return newLocation;
    }

    public ConfigurationSection getConfigSection() {
        FileConfiguration config = TowerDefense.getInstance().getConfig();
        return config.getConfigurationSection("turrets."+turretType.getId());
    }

}
