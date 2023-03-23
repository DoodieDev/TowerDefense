package doodieman.towerdefense.mapsetup.command.arguments;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MapSetupCmdCreate {

    public MapSetupCmdCreate(Player player, String[] args, MapSetupHandler handler) {

        if (args.length < 2) {
            player.sendMessage("§cSkriv /mapsetup create <map ID>");
            return;
        }

        FileConfiguration config = TowerDefense.getInstance().getConfig();
        String mapID = args[1].toLowerCase();

        //Create the map in the config with useless data ('creator')
        config.set("maps."+mapID+".creator", player.getName());
        TowerDefense.getInstance().saveConfig();

        player.sendMessage("§aMappet er nu blevet oprettet. Se alle maps med /mapsetup list");

    }

}
