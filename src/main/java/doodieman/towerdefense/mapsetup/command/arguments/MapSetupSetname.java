package doodieman.towerdefense.mapsetup.command.arguments;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import doodieman.towerdefense.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class MapSetupSetname {

    public MapSetupSetname(Player player, String[] args, MapSetupHandler handler) {

        if (args.length < 3) {
            player.sendMessage("§cSkriv /mapsetup setpath <map ID> <Navn>");
            return;
        }

        FileConfiguration config = TowerDefense.getInstance().getConfig();
        String mapID = args[1].toLowerCase();

        if (!handler.doesMapExist(mapID)) {
            player.sendMessage("§cMappet eksisterer ikke.");
            return;
        }

        //Get the map name
        StringJoiner mapNameBuilder = new StringJoiner(" ");
        int i = 2;
        while (args.length > i) {
            mapNameBuilder.add(args[i]);
            i++;
        }
        String mapName = mapNameBuilder.toString();
        config.set("maps."+mapID+".name", mapName);
        TowerDefense.getInstance().saveConfig();

        player.sendMessage("§aDu har ændret navnet til §2"+mapName);
    }

}
