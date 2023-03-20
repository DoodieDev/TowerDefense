package doodieman.towerdefense.mapsetup;

import doodieman.towerdefense.TowerDefense;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class MapSetupCommand implements CommandExecutor {

    private final MapSetupHandler handler;

    public void sendHelpMessage(Player player) {
        player.sendMessage("§c/mapsetup createmap <map navn>");
        player.sendMessage("§c/mapsetup deletemap <map navn>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;
        if (args.length < 1) {
            this.sendHelpMessage(player);
            return true;
        }

        FileConfiguration config = TowerDefense.getInstance().getConfig();
        String subcommand = args[0].toUpperCase();

        switch (subcommand) {
            case "CREATEMAP":

                if (args.length < 2) {
                    player.sendMessage("§cSkriv /mapsetup createmap <map navn>");
                    break;
                }

                String mapName = args[1].toLowerCase();

                //Create the map in the config with useless data ('creator')
                config.set("maps."+mapName+".creator", player.getName());
                TowerDefense.getInstance().saveConfig();

                player.sendMessage("§aMappet er nu blevet oprettet. Se alle maps med /mapsetup list");
                break;

            case "DELETEMAP":

                if (args.length < 2) {
                    player.sendMessage("§cSkriv /mapsetup deletemap <map navn>");
                    break;
                }

                mapName = args[1].toLowerCase();

                //Create the map in the config with useless data ('creator')
                config.set("maps."+mapName, null);
                TowerDefense.getInstance().saveConfig();

                player.sendMessage("§aMappet er nu blevet slettet.");
                break;

            default:
                this.sendHelpMessage(player);
                break;
        }

        return true;
    }

}
