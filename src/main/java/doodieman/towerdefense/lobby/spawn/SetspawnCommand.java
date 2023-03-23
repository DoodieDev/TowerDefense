package doodieman.towerdefense.lobby.spawn;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetspawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        player.sendMessage("§aSpawn er nu blevet sat!");
        SpawnUtil.setSpawn(player.getLocation());

        return true;
    }

}
