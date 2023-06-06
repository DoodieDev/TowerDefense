package doodieman.towerdefense.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtil {
    public static String locationToString(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
    }

    public static Location stringToLocation(String string) {
        String[] parts = string.split(";");
        if (parts.length != 6) return new Location(Bukkit.getWorld("world"), 0, 0, 0);

        World world = Bukkit.getWorld(parts[0]);
        float x = Float.parseFloat(parts[1]);
        float y = Float.parseFloat(parts[2]);
        float z = Float.parseFloat(parts[3]);
        float yaw = Float.parseFloat(parts[4]);
        float pitch = Float.parseFloat(parts[5]);

        return new Location(world, x, y, z, yaw, pitch);
    }

    public static boolean isInBoundaries(Location loc, Location corner1, Location corner2) {
        double
            x1 = Math.min(corner1.getX(), corner2.getX()),
            y1 = Math.min(corner1.getY(), corner2.getY()),
            z1 = Math.min(corner1.getZ(), corner2.getZ()),
            x2 = Math.max(corner1.getX(), corner2.getX()),
            y2 = Math.max(corner1.getY(), corner2.getY()),
            z2 = Math.max(corner1.getZ(), corner2.getZ());

        return
            isBetween(Math.floor(loc.getX()), x1, x2)
                && isBetween(Math.floor(loc.getY()), y1, y2)
                && isBetween(Math.floor(loc.getZ()), z1, z2);
    }

    public static double oppositeAngle(double angle) {
        double oppositeAngle = angle + 180;
        oppositeAngle = oppositeAngle % 360;
        if (oppositeAngle < 0) {
            oppositeAngle += 360;
        }
        return oppositeAngle;
    }

    public static double addAngle(double current, double add) {
        return (current + add) % 360;
    }

    private static boolean isBetween(double number, double min, double max) {
        return number >= min && number <= max;
    }

}
