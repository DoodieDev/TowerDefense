package doodieman.towerdefense;

import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.objects.GameTurret;
import doodieman.towerdefense.game.values.Difficulty;
import doodieman.towerdefense.game.values.MobType;
import doodieman.towerdefense.maps.MapUtil;
import doodieman.towerdefense.maps.objects.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length < 1) return true;

        if (args[0].equalsIgnoreCase("as")) {

            Game game = GameUtil.getInstance().getActiveGame(player);

            for (GameTurret turret : game.getTurrets()) {
                turret.setRotation(turret.getRotation() + 10);

                turret.updateArmorStands();
            }


        }




        return true;
    }

}
