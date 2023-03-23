package doodieman.towerdefense.mapsetup.command.arguments;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MapSetupCmdDelete {

    public MapSetupCmdDelete(Player player, String[] args, MapSetupHandler handler) {

        if (args.length < 2) {
            player.sendMessage("§cSkriv /mapsetup delete <map ID>");
            return;
        }

        FileConfiguration config = TowerDefense.getInstance().getConfig();
        String mapID = args[1].toLowerCase();

        config.set("maps."+mapID, null);
        TowerDefense.getInstance().saveConfig();

        player.sendMessage("§aMappet er nu blevet slettet.");
    }

}
