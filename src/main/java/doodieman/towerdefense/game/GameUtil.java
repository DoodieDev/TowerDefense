package doodieman.towerdefense.game;

import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.values.Difficulty;
import doodieman.towerdefense.maps.objects.Map;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

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

        Game game = new Game(player, map, difficulty);
        this.handler.getActiveGames().put(player, game);
        game.prepare();
        game.start();
    }

    //Stop an active game
    public void stopGame(Game game) {
        game.stop();
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
