package doodieman.towerdefense.game.values;

import lombok.Getter;

public enum GameSetting {

    AUTO_START(false),
    SPED_UP(false);

    @Getter
    private final boolean defaultValue;

    GameSetting(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }




}
