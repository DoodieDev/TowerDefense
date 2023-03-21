package doodieman.towerdefense.mapsetup.command.arguments;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import doodieman.towerdefense.utils.ItemBuilder;
import doodieman.towerdefense.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MapSetupEditpath {

    public MapSetupEditpath(Player player, String[] args, MapSetupHandler handler) {

        if (args.length < 3) {
            player.sendMessage("§cSkriv /mapsetup setpath <map navn> <Point>");
            return;
        }

        FileConfiguration config = TowerDefense.getInstance().getConfig();
        String mapName = args[1].toLowerCase();

        if (!handler.doesMapExist(mapName)) {
            player.sendMessage("§cMappet eksisterer ikke.");
            return;
        }

        String point = args[2];
        Location location = player.getLocation();

        config.set("maps."+mapName+".path."+point, LocationUtil.locationToString(location));
        TowerDefense.getInstance().saveConfig();

        player.sendMessage("§aDu har tilføjet punkt #"+point+" til map path.");
    }

}
