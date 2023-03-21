package doodieman.towerdefense.mapsetup.command;

import com.sk89q.worldedit.bukkit.selections.Selection;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupCmdCreate;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupCmdDelete;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupCmdList;
import doodieman.towerdefense.mapsetup.command.arguments.MapSetupCmdSetregion;
import doodieman.towerdefense.utils.LocationUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class MapSetupCommand implements CommandExecutor {

    private final MapSetupHandler handler;

    public void sendHelpMessage(Player player) {
        player.sendMessage("§c/mapsetup create <map navn>");
        player.sendMessage("§c/mapsetup delete <map navn>");
        player.sendMessage("§c/mapsetup list <map navn>");
        player.sendMessage("§c/mapsetup setregion <map navn>");
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
            case "CREATE":
                new MapSetupCmdCreate(player, args, handler);
                break;

            case "DELETE":
                new MapSetupCmdDelete(player, args, handler);
                break;

            case "LIST":
                new MapSetupCmdList(player, args, handler);
                break;

            case "SETREGION":
                new MapSetupCmdSetregion(player, args, handler);
                break;

            default:
                this.sendHelpMessage(player);
                break;
        }

        return true;
    }

}
