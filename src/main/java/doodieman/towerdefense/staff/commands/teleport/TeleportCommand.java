package doodieman.towerdefense.staff.commands.teleport;

import doodieman.towerdefense.staff.StaffHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("staff.teleport")) {
            player.sendMessage("§cDet har du ikke adgang til!");
            return true;
        }

        //Not enough args
        if (args.length <= 0) {
            player.sendMessage("§cDu skal skrive en spiller!");
            player.sendMessage("§cBenyt: /tp <Spiller>");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);

        //Not online
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            player.sendMessage("§c"+args[0]+" er ikke online!");
            return true;
        }

        //Not OP, and is not in staffmode
        if (!player.hasPermission("*") && !StaffHandler.getInstance().isInStaffMode(player)) {
            player.sendMessage("§cDu skal være i staffmode for at teleportere!");
            player.sendMessage("§cBenyt: /staff og prøv igen.");
            return true;
        }

        player.teleport(targetPlayer.getLocation());
        player.sendMessage("§6Teleporteret!");

        return true;
    }

}
