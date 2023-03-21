package doodieman.towerdefense;

import doodieman.towerdefense.mapgrid.MapGridHandler;
import doodieman.towerdefense.mapgrid.objects.GridLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length < 1) return true;

        if (args[0].equalsIgnoreCase("start")) {

            GridLocation gridLocation = MapGridHandler.getInstance().generateLocation();
            gridLocation.register();

            Location location = gridLocation.getLocation(player.getWorld());
            location.add(0, 10, 0);
            location.getBlock().setType(Material.SPONGE);

            Bukkit.broadcastMessage("Debug: X: "+gridLocation.getX()+", Z: "+gridLocation.getZ());
        }


        return true;
    }

}
