package doodieman.towerdefense.game.objects.turrets;

import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.objects.GameMob;
import doodieman.towerdefense.game.objects.GameTurret;
import doodieman.towerdefense.game.values.TurretType;
import org.bukkit.Location;

import java.util.List;

public class StoneTower extends GameTurret {

    public StoneTower(Game game, TurretType turretType, Location location) {
        super(game, turretType, location);
    }

    @Override
    public List<GameMob> detect() {
        return null;
    }

    @Override
    public void shoot(GameMob mob) {

    }

    @Override
    public void roundFinished() {

    }

    @Override
    public void update(long roundTick) {

    }

}
