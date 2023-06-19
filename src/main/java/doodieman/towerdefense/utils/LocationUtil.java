package doodieman.towerdefense.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.EulerAngle;

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

    public static String eulerAngleToString(EulerAngle eulerAngle) {
        return eulerAngle.getX()+";"+eulerAngle.getY()+";"+eulerAngle.getZ();
    }

    public static EulerAngle stringToEulerAngle(String string) {
        String[] parts = string.split(";");
        if (parts.length != 3) return new EulerAngle(0,0,0);

        float x = Float.parseFloat(parts[0]);
        float y = Float.parseFloat(parts[1]);
        float z = Float.parseFloat(parts[2]);

        return new EulerAngle(x,y,z);
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

    //Moves location1 closer towards location2
    public static Location moveCloser(Location loc1, Location loc2, double distance) {
        double dx = loc2.getX() - loc1.getX();
        double dy = loc2.getY() - loc1.getY();
        double dz = loc2.getZ() - loc1.getZ();
        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (length <= distance) return loc2.clone();
        double newX = loc1.getX() + dx * (distance / length);
        double newY = loc1.getY() + dy * (distance / length);
        double newZ = loc1.getZ() + dz * (distance / length);
        return new Location(loc1.getWorld(), newX, newY, newZ);
    }

    public static Location getLocationInCircle(Location loc, double angle, double radius) {
        double x = (loc.getX() + radius * Math.cos(angle * Math.PI / 180));
        double z = (loc.getZ() + radius * Math.sin(angle * Math.PI / 180));
        return new Location(loc.getWorld(), x, loc.getY(), z);
    }

    public static double getAngleToLocation(Location center, Location target) {
        double deltaX = target.getX() - center.getX();
        double deltaZ = target.getZ() - center.getZ();
        double angle = Math.atan2(deltaZ, deltaX);
        angle = Math.toDegrees(angle);
        if (angle < 0)
            angle += 360;
        return angle;
    }

}
