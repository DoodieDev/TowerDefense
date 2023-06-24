package doodieman.towerdefense.game.values;

import lombok.Getter;

public class MobTypeStack {

    @Getter
    private final int amount;
    @Getter
    private final MobType mobType;

    public MobTypeStack(int amount, MobType mobType) {
        this.amount = amount;
        this.mobType = mobType;
    }

}
