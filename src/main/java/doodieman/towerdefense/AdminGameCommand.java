package doodieman.towerdefense;

import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.objects.GameTurret;
import doodieman.towerdefense.maps.MapUtil;
import doodieman.towerdefense.maps.objects.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminGameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length < 1) return true;

        switch (args[0].toUpperCase()) {

            case "GOLD":
                Game gameToGold = GameUtil.getInstance().getActiveGame(player);
                gameToGold.addGold(5000);
                break;

            case "SAVEGAME":
                Game gameToSave = GameUtil.getInstance().getActiveGame(player);
                gameToSave.saveGame();

                player.sendMessage("Saved game.. tror jeg nok");
                break;

            case "LOADGAME":

                Map map = MapUtil.getInstance().getMap(args[1].toLowerCase());
                if (map == null) {
                    player.sendMessage("§cUkendt map");
                    break;
                }

                GameUtil.getInstance().loadGame(player,map);

                break;

        }



        return true;
    }

}
