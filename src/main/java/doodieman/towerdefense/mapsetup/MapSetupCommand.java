package doodieman.towerdefense.mapsetup;

import doodieman.towerdefense.TowerDefense;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MapSetupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage("§cIkke nok argumenter");
            return true;
        }

        String subcommand = args[0].toUpperCase();

        switch (subcommand) {

            case "createmap":

                if (args.length < 2) {
                    player.sendMessage("§cSkriv /mapsetup createmap <mapnavn>");
                    break;
                }
                String mapName = args[1].toLowerCase();

                FileConfiguration config = TowerDefense.getInstance().getConfig();
                config.set("maps."+mapName+".",);

                break;
        }

        return true;
    }

}
