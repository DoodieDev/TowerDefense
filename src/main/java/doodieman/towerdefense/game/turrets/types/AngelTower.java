package doodieman.towerdefense.game.turrets.types;

import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.objects.GameMob;
import doodieman.towerdefense.game.turrets.GameTurret;
import doodieman.towerdefense.game.values.TurretType;
import org.bukkit.Location;
import org.bukkit.Sound;

import java.util.List;
import java.util.stream.Collectors;

public class AngelTower extends GameTurret {

    long lastShot = 0L;
    boolean doubleSpeed = false;

    public AngelTower(Game game, TurretType turretType, Location location) {
        super(game, turretType, location);
    }

    @Override
    public void update(long roundTick) {

        double attackSpeed = this.getTurretType().getAttackSpeed();
        attackSpeed = this.getGame().isDoubleRoundSpeed() ? attackSpeed * 2 : attackSpeed;
        double shotsPerTick = attackSpeed / 20;
        long fictiveTick = (long) Math.floor(shotsPerTick * roundTick);

        if (this.getGame().isDoubleRoundSpeed() != this.doubleSpeed) {
            this.doubleSpeed = this.getGame().isDoubleRoundSpeed();
            this.lastShot = fictiveTick;
            return;
        }
        this.doubleSpeed = this.getGame().isDoubleRoundSpeed();

        if (fictiveTick > lastShot) {
            long difference = fictiveTick - this.lastShot;

            for (int i = 0; i < difference; i++)
                this.shootClosestMob();

            this.lastShot = fictiveTick;
        }

    }

    @Override
    public void roundFinished() {
        this.lastShot = 0L;
    }

    @Override
    public List<GameMob> detect() {
        return this.getGame().getAliveMobs()
            .stream()
            .filter(mob -> mob.getLocation().distance(this.getCenterLocation()) <= this.getTurretType().getRange())
            .collect(Collectors.toList());
    }

    @Override
    public void shoot(GameMob mob) {
        getCenterLocation().getWorld().playSound(getLocation(), Sound.DIG_WOOL,1f,1f);

        this.rotateTowardsMob(mob);
        mob.damage(getTurretType().getDamage());
    }

}
