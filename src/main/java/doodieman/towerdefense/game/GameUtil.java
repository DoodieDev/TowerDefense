package doodieman.towerdefense.game;

import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.maps.MapUtil;
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

    public void startGame(OfflinePlayer player) {
        Map map = MapUtil.getInstance().getMap("afrotest");
        Game game = new Game(player, map);
        handler.getActiveGames().put(player, game);
        game.prepare();
        game.start();
    }

    public void stopGame(OfflinePlayer player) {
        Game game = getActiveGame(player);
        game.stop();
        handler.getActiveGames().remove(player);
    }

    public boolean isInGame(OfflinePlayer player) {
        return handler.getActiveGames().containsKey(player);
    }

    public Game getActiveGame(OfflinePlayer player) {
        return handler.getActiveGames().get(player);
    }

}
