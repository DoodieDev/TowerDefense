package doodieman.towerdefense.turretsetup;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.IOException;

public class TurretSetupHandler {

    public TurretSetupHandler() {
    }

    //Save the schematic of a turret
    public void saveSchematic(String turretID) {
        ConfigurationSection section = this.getSection(turretID);
        String schematicPath = TowerDefense.getInstance().getDataFolder() + "/turrets/" + turretID + ".schematic";

        Location corner1 = LocationUtil.stringToLocation(section.getString("corner1"));
        Location corner2 = LocationUtil.stringToLocation(section.getString("corner2"));
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

    //Save all the armorstands of a turret
    public void saveArmorstands(String turretID) {
        ConfigurationSection section = this.getSection(turretID);
        Location corner1 = LocationUtil.stringToLocation(section.getString("corner1"));

        corner1.add(0.5,0,0.5);
        World world = corner1.getWorld();

        //Clear current armorstands in config
        section.set("armorstands", null);
        ConfigurationSection asSection = section.createSection("armorstands");

        int i = 0;
        for (Entity entity : world.getNearbyEntities(corner1, 1.5,255, 1.5)) {
            if (!(entity instanceof ArmorStand)) continue;
            ArmorStand armorStand = (ArmorStand) entity;

            asSection.set(i+".small", armorStand.isSmall());

            //Save location
            asSection.set(i+".location", LocationUtil.locationToString(armorStand.getLocation()));

            //Save armorstand poses
            asSection.set(i+".head", LocationUtil.eulerAngleToString(armorStand.getHeadPose()));
            asSection.set(i+".body", LocationUtil.eulerAngleToString(armorStand.getBodyPose()));
            asSection.set(i+".rightArm", LocationUtil.eulerAngleToString(armorStand.getRightArmPose()));
            asSection.set(i+".leftArm", LocationUtil.eulerAngleToString(armorStand.getLeftArmPose()));
            asSection.set(i+".rightLeg", LocationUtil.eulerAngleToString(armorStand.getRightLegPose()));
            asSection.set(i+".leftLeg", LocationUtil.eulerAngleToString(armorStand.getLeftLegPose()));
            asSection.set(i+".visible", armorStand.isVisible());

            //Save equipment
            asSection.set(i+".equipment.helmet",armorStand.getHelmet());
            asSection.set(i+".equipment.chestplate",armorStand.getChestplate());
            asSection.set(i+".equipment.leggings",armorStand.getLeggings());
            asSection.set(i+".equipment.boots",armorStand.getBoots());
            asSection.set(i+".equipment.tool",armorStand.getItemInHand());

            i++;
        }

        TowerDefense.getInstance().saveConfig();
    }

    private ConfigurationSection getSection(String turretID) {
        FileConfiguration config = TowerDefense.getInstance().getConfig();
        return config.getConfigurationSection("turrets."+turretID);
    }

}
