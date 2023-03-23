package doodieman.towerdefense.mapsetup.command.arguments;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MapSetupClearpath {

    public MapSetupClearpath(Player player, String[] args, MapSetupHandler handler) {

        if (args.length < 2) {
            player.sendMessage("§cSkriv /mapsetup setpath <map ID>");
            return;
        }

        FileConfiguration config = TowerDefense.getInstance().getConfig();
        String mapID = args[1].toLowerCase();

        if (!handler.doesMapExist(mapID)) {
            player.sendMessage("§cMappet eksisterer ikke.");
            return;
        }

        //Clear current path
        config.set("maps."+mapID+".path", null);
        TowerDefense.getInstance().saveConfig();

        player.sendMessage("§cPath er nu blevet slettet");

    }

}
