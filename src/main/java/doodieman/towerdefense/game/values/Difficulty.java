package doodieman.towerdefense.game.values;

import lombok.Getter;

public enum  Difficulty {

    BEGINNER("Begynder"),
    EASY("Øvede"),
    HARD("Avanceret"),
    EXPERT("Expert");

    @Getter
    private String name;

    Difficulty(String name) {
        this.name = name;
    }


}
