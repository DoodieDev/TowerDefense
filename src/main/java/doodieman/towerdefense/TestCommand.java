package doodieman.towerdefense;

import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.values.Difficulty;
import doodieman.towerdefense.game.values.MobType;
import doodieman.towerdefense.maps.MapUtil;
import doodieman.towerdefense.maps.objects.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length < 1) return true;

        if (args[0].equalsIgnoreCase("start")) {

        }

        else if (args[0].equalsIgnoreCase("stop")) {

        }



        return true;
    }

}
