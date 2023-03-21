package doodieman.towerdefense.mapsetup.command;

import doodieman.towerdefense.mapsetup.MapSetupHandler;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupClearpath;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupCmdCreate;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupCmdDelete;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupCmdList;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupCmdSetregion;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupEditpath;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class MapSetupCommand implements CommandExecutor {

    private final MapSetupHandler handler;

    public void sendHelpMessage(Player player) {
        player.sendMessage("§6/mapsetup create <map navn> §fOpret et map");
        player.sendMessage("§6/mapsetup delete <map navn> §fSlet et map");
        player.sendMessage("§6/mapsetup list §fSe alle maps");
        player.sendMessage("§6/mapsetup setregion <map navn> §fSæt schematic region");
        player.sendMessage("§6/mapsetup setpath <map navn> <Point> §fSæt points til mob stien");
        player.sendMessage("§6/mapsetup clearpath <map navn> §fClear mob stien");
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

        String subcommand = args[0].toUpperCase();

        switch (subcommand) {

            case "OPRET":
            case "CREATEMAP":
            case "CREATE":
                new MapSetupCmdCreate(player, args, handler);
                break;

            case "SLET":
            case "DELETEMAP":
            case "DELETE":
                new MapSetupCmdDelete(player, args, handler);
                break;

            case "LIST":
                new MapSetupCmdList(player, args, handler);
                break;

            case "SETSCHEMATIC":
            case "SETREGION":
                new MapSetupCmdSetregion(player, args, handler);
                break;

            case "SETPATH":
                new MapSetupEditpath(player, args, handler);
                break;

            case "SLETPATH":
            case "CLEARPATH":
                new MapSetupClearpath(player, args, handler);
                break;

            default:
                this.sendHelpMessage(player);
                break;
        }

        return true;
    }

}
