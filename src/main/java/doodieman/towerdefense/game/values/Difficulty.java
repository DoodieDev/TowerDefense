package doodieman.towerdefense.game.values;

import lombok.Getter;

public enum  Difficulty {

    BEGINNER("Begynder", 800),
    EASY("Øvede", 600),
    HARD("Avanceret", 400),
    EXPERT("Expert", 200);

    @Getter
    private final String name;
    @Getter
    private final double health;

    Difficulty(String name, double health) {
        this.name = name;
        this.health = health;
    }


}
