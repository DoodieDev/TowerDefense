package doodieman.towerdefense;

import doodieman.towerdefense.game.GameUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length < 1) return true;

        if (args[0].equalsIgnoreCase("start")) {
            GameUtil.getInstance().startGame(player);
        }

        else if (args[0].equalsIgnoreCase("stop")) {
            GameUtil.getInstance().stopGame(player);
        }

        else if (args[0].equalsIgnoreCase("wave")) {
            GameUtil.getInstance().getActiveGame(player).startRound();
        }

        else if (args[0].equalsIgnoreCase("bigwave")) {

            new BukkitRunnable() {
                int i = 0;
                @Override
                public void run() {
                    i++;
                    if (i > 100) this.cancel();

                    GameUtil.getInstance().getActiveGame(player).startRound();
                }
            }.runTaskTimer(TowerDefense.getInstance(),0L,1L);
        }


        return true;
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

}
