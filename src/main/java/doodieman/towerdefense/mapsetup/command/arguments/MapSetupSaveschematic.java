package doodieman.towerdefense.mapsetup.command.arguments;

import doodieman.towerdefense.mapsetup.MapSetupHandler;
import org.bukkit.entity.Player;

public class MapSetupSaveschematic {

    public MapSetupSaveschematic(Player player, String[] args, MapSetupHandler handler) {
        if (args.length < 2) {
            player.sendMessage("§cSkriv /mapsetup setregion <map ID>");
            return;
        }

        String mapID = args[1].toLowerCase();

        if (!handler.doesMapExist(mapID)) {
            player.sendMessage("§cMappet eksisterer ikke.");
            return;
        }

        handler.saveSchematic(mapID);
        player.sendMessage("§aMap schematic er blevet gemt!");

    }

}
