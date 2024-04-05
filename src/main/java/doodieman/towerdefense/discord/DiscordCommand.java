package doodieman.towerdefense.discord;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DiscordCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;
        player.sendMessage("");
        player.sendMessage("§fJoin vores Discord! §9§ndiscord.gg/TurWm7txxs");
        player.sendMessage("");
        return true;
    }

}
