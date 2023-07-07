package doodieman.towerdefense.lobby.spawn;

import doodieman.towerdefense.game.GameUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        if (GameUtil.getInstance().isInGame(player)) {
            GameUtil.getInstance().exitGame(player, true);
            return true;
        }

        player.teleport(SpawnUtil.getSpawn());

        return true;
    }

}
