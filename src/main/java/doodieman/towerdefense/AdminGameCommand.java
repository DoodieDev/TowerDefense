package doodieman.towerdefense;

import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.maps.MapUtil;
import doodieman.towerdefense.maps.objects.Map;
import doodieman.towerdefense.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class AdminGameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length < 1) return true;

        switch (args[0].toUpperCase()) {

            case "RELOAD":

                List<Integer> announceTimes = Arrays.asList(60, 30, 10, 3, 2, 1);

                new BukkitRunnable() {

                    int secondsLeft = 60;

                    @Override
                    public void run() {

                        if (secondsLeft <= 0) {
                            this.cancel();
                            Bukkit.broadcastMessage("");
                            Bukkit.broadcastMessage("§6[§eSOL§6] §cServeren reloader nu!");
                            Bukkit.broadcastMessage("");
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"plugman reload TowerDefense");
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"hd reload");
                            return;
                        }

                        if (announceTimes.contains(secondsLeft)) {
                            String formatted = StringUtil.convertToDanishTime(secondsLeft);
                            Bukkit.broadcastMessage("");
                            Bukkit.broadcastMessage("§6[§eSOL§6] §7Serveren reloader om §f"+ formatted+"§7! Dit spil vil blive gemt!");
                            Bukkit.broadcastMessage("");
                        }

                        secondsLeft--;
                    }
                }.runTaskTimer(TowerDefense.getInstance(),0L,20L);

                break;

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
