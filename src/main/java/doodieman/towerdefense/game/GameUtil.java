package doodieman.towerdefense.game;

import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.values.Difficulty;
import doodieman.towerdefense.lobby.spawn.SpawnUtil;
import doodieman.towerdefense.maps.objects.Map;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class GameUtil {

    final GameHandler handler;

    @Getter
    private static GameUtil instance;

    public GameUtil(GameHandler handler) {
        this.handler = handler;
        instance = this;
    }

    //Start a brand new game for a player
    public void startGame(OfflinePlayer player, Map map, Difficulty difficulty) {
        if (this.isInGame(player)) return;

        //Player stuff
        Player onlinePlayer = player.getPlayer();
        onlinePlayer.setHealth(20);
        onlinePlayer.setFoodLevel(20);
        onlinePlayer.setGameMode(GameMode.SURVIVAL);
        onlinePlayer.getInventory().clear();

        //Start game
        Game game = new Game(player, map, difficulty);
        this.handler.getActiveGames().put(player, game);
        game.prepare();
        game.start();
    }

    //Exit and save the game
    public void exitGame(OfflinePlayer player, boolean removeSchematic) {
        if (!this.isInGame(player)) return;

        Game game = this.getActiveGame(player);
        game.stop(removeSchematic);
        handler.getActiveGames().remove(game.getPlayer());

        player.getPlayer().sendMessage("§aSpillet er blevet gemt!");
        player.getPlayer().sendMessage("§aDu kan altid forsætte fra punktet du forlod.");
        player.getPlayer().getInventory().clear();
        player.getPlayer().teleport(SpawnUtil.getSpawn());
    }

    //Stop an active game
    public void stopGame(Game game) {
        game.stop(true);
        handler.getActiveGames().remove(game.getPlayer());
    }

    //Check if a player isIngame
    public boolean isInGame(OfflinePlayer player) {
        return this.handler.getActiveGames().containsKey(player);
    }

    //Get a player's active game
    public Game getActiveGame(OfflinePlayer player) {
        return this.handler.getActiveGames().get(player);
    }

}
