package doodieman.towerdefense;

import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.values.Difficulty;
import doodieman.towerdefense.game.values.MobType;
import doodieman.towerdefense.maps.MapUtil;
import doodieman.towerdefense.maps.objects.Map;
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
            Map map = MapUtil.getInstance().getMap("afrotest");
            GameUtil.getInstance().startGame(player, map, Difficulty.EASY);
        }

        else if (args[0].equalsIgnoreCase("stop")) {
            Game game = GameUtil.getInstance().getActiveGame(player);
            GameUtil.getInstance().stopGame(game);
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
