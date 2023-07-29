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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GameTurret {

    @Getter
    private final Game game;
    @Getter
    private final TurretType turretType;
    @Getter
    private final Location location;
    @Getter
    private double rotation;
    @Getter
    private final List<GameTurretArmorstand> armorStandList = new ArrayList<>();

    public GameTurret(Game game, TurretType turretType, Location location) {
        this.game = game;
        this.turretType = turretType;
        this.location = location;
        this.rotation = 0;
    }

    //Detect the mobs possible to shoot. Can be different from turret to turret.
    public abstract List<GameMob> detect();
    //Trigger the tower to shoot this mob.
    public abstract void shoot(GameMob mob);
    //Call the entire tower cycle of shooting mobs
    public abstract void update(long roundTick);

    public abstract void roundFinished();

    //Get the mob closest to the end goal
    public GameMob getClosestMob() {

        List<GameMob> detected = this.detect();
        if (detected.size() == 0) return null;

        GameMob closest = detected.get(0);
        double currentDistance = closest.currentLength;

        for (GameMob mob : detected) {

            double distance = mob.currentLength;
            if (distance <= currentDistance) continue;

            currentDistance = distance;
            closest = mob;

        }

        return closest;
    }

    //Make turrt execute shoot on the mob closest to end
    public void shootClosestMob() {
        GameMob closestMob = this.getClosestMob();
        if (closestMob == null) return;
        this.shoot(closestMob);
    }

    public void rotateTowardsMob(GameMob gameMob) {
        this.setRotation(LocationUtil.getAngleToLocation(this.getCenterLocation(),gameMob.getLocation()));
        this.updateArmorStands();
    }

    public void setRotation(double value) {
        this.rotation = value + 180d;
    }

    //Render the turret. (Schematic, hologram, etc)
    public void render(boolean animation) {
        this.game.setPastingTurret(true);

        //Render the turret. First paste the schematic async.
        TowerDefense.runAsync(() -> {
            this.pasteSchematic();

            //When schematic is done. Render the armorstands.
            TowerDefense.runSync(() -> {
                this.pasteRedstoneBlocks();
                this.pasteArmorStands();
                this.updateArmorStands();

                //Call animation
                if (animation)
                    this.getGame().getGameInteractive().getGameAnimations().onTurretPlacement(this);

                this.game.setPastingTurret(false);
                //TODO create hologram
            });
        });
    }

    //Get the zero location for pasting schematic
    public Location getZeroLocation() {
        Location loc = this.location.clone();
        loc.setX(Math.floor(loc.getX()));
        loc.setZ(Math.floor(loc.getZ()));
        loc.setY(0);
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

    //Paste the 3x3 redstoneblocks at the bottom
    public void pasteRedstoneBlocks() {

        Location zero = this.getZeroLocation();
        int xCorner1 = zero.getBlockX() - 1;
        int zCorner1 = zero.getBlockZ() - 1;
        int xCorner2 = zero.getBlockX() + 1;
        int zCorner2 = zero.getBlockZ() + 1;

        for (int x = xCorner1; x <= xCorner2; x++) {
            for (int z = zCorner1; z <= zCorner2; z++) {
                this.game.getWorld().getBlockAt(x,zero.getBlockY(),z).setType(Material.REDSTONE_BLOCK);
            }
        }
    }

    //Paste all the armorstands
    public void pasteArmorStands() {

        Location centerLocation = this.getZeroLocation().clone();
        ConfigurationSection section = getConfigSection().getConfigurationSection("armorstands");

        for (String id : section.getKeys(false)) {

            ConfigurationSection asSection = section.getConfigurationSection(id);

            Location realLocation = this.getRealLocation(LocationUtil.stringToLocation(asSection.getString("location")));
            ArmorStand armorStand = realLocation.getWorld().spawn(realLocation,ArmorStand.class);

            armorStand.setGravity(false);
            armorStand.setBasePlate(false);

            armorStand.setSmall(asSection.getBoolean("small",false));
            armorStand.setVisible(asSection.getBoolean("visible",false));

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
        Location center = this.getZeroLocation().add(0.5, 0, 0.5);

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

    public Location getCenterLocation() {
        return this.location.clone().add(0.5, 0, 0.5);
    }
}
