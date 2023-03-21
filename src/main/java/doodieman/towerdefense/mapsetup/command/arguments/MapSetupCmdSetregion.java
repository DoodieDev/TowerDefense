package doodieman.towerdefense.mapsetup.command.arguments;

import com.sk89q.worldedit.bukkit.selections.Selection;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import doodieman.towerdefense.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MapSetupCmdSetregion {

    public MapSetupCmdSetregion(Player player, String[] args, MapSetupHandler handler) {

        if (args.length < 2) {
            player.sendMessage("§cSkriv /mapsetup setregion <map navn>");
            return;
        }

        FileConfiguration config = TowerDefense.getInstance().getConfig();
        String mapName = args[1].toLowerCase();

        if (!handler.doesMapExist(mapName)) {
            player.sendMessage("§cMappet eksisterer ikke.");
            return;
        }

        //Get players selection
        Selection selection = TowerDefense.getInstance().getWorldedit().getSelection(player);
        if (selection == null) {
            player.sendMessage("§cDu skal markere et område med WorldEdit");
            return;
        }

        Location corner1 = selection.getMinimumPoint();
        Location corner2 = selection.getMaximumPoint();

        config.set("maps."+mapName+".corner1", LocationUtil.locationToString(corner1));
        config.set("maps."+mapName+".corner2", LocationUtil.locationToString(corner2));
        TowerDefense.getInstance().saveConfig();

        player.sendMessage("§aDu har sat mappets region!");
        player.sendMessage("§a- §fHjørne 1: §7X: "+corner1.getX()+" Y: "+corner1.getY()+" Z: "+corner1.getZ());
        player.sendMessage("§a- §fHjørne 2: §7X: "+corner2.getX()+" Y: "+corner2.getY()+" Z: "+corner2.getZ());
    }

}
