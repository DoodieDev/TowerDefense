package doodieman.towerdefense.mapsetup.command.arguments;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MapSetupSaveschematic {

    public MapSetupSaveschematic(Player player, String[] args, MapSetupHandler handler) {
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

        handler.saveSchematic(mapName);
        player.sendMessage("§aMap schematic er blevet gemt!");

    }

}
