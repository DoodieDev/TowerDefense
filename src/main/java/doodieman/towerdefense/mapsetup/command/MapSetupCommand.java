package doodieman.towerdefense.mapsetup.command;

import doodieman.towerdefense.mapsetup.MapSetupHandler;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupClearpath;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupCmdCreate;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupCmdDelete;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupCmdList;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupCmdSetregion;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupSetname;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupSetpath;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupSaveschematic;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class MapSetupCommand implements CommandExecutor {

    private final MapSetupHandler handler;

    public void sendHelpMessage(Player player) {
        player.sendMessage("§6/mapsetup create <map ID> §fOpret et map");
        player.sendMessage("§6/mapsetup delete <map ID> §fSlet et map");
        player.sendMessage("§6/mapsetup list §fSe alle maps");
        player.sendMessage("§6/mapsetup setregion <map ID> §fSæt schematic region");
        player.sendMessage("§6/mapsetup setpath <map ID> <Point> §fSæt points til mob stien");
        player.sendMessage("§6/mapsetup clearpath <map ID> §fClear mob stien");
        player.sendMessage("§6/mapsetup saveschematic <map ID> §fGem schematic af region");
        player.sendMessage("§6/mapsetup setname <map ID> <navn> §fSæt mappets navn");
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
                new MapSetupSetpath(player, args, handler);
                break;

            case "SLETPATH":
            case "CLEARPATH":
                new MapSetupClearpath(player, args, handler);
                break;

            case "SAVESCHEMATIC":
                new MapSetupSaveschematic(player, args, handler);
                break;

            case "SETNAME":
                new MapSetupSetname(player, args, handler);
                break;

            default:
                this.sendHelpMessage(player);
                break;
        }

        return true;
    }

}
