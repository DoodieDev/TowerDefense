package doodieman.towerdefense.mapsetup.command.arguments;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import doodieman.towerdefense.utils.ItemBuilder;
import doodieman.towerdefense.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MapSetupSetpath {

    public MapSetupSetpath(Player player, String[] args, MapSetupHandler handler) {

        if (args.length < 3) {
            player.sendMessage("§cSkriv /mapsetup setpath <map ID> <Point>");
            return;
        }

        FileConfiguration config = TowerDefense.getInstance().getConfig();
        String mapID = args[1].toLowerCase();

        if (!handler.doesMapExist(mapID)) {
            player.sendMessage("§cMappet eksisterer ikke.");
            return;
        }

        String point = args[2];
        Location location = player.getLocation();

        config.set("maps."+mapID+".path."+point, LocationUtil.locationToString(location));
        TowerDefense.getInstance().saveConfig();

        player.sendMessage("§aDu har tilføjet punkt #"+point+" til map path.");
    }

}
