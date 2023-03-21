package doodieman.towerdefense.game;

import doodieman.towerdefense.game.objects.Game;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;

public class GameHandler {

    @Getter
    private final GameUtil gameUtil;
    @Getter
    private final Map<OfflinePlayer, Game> activeGames = new HashMap<>();

    public GameHandler() {
        this.gameUtil = new GameUtil(this);
    }

}
