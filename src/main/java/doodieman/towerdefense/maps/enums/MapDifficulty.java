package doodieman.towerdefense.maps.enums;

import lombok.Getter;

public enum MapDifficulty {

    BEGINNER("BEGINNER", "§a", "Begynder"),
    INTERMEDIATE("INTERMEDIATE", "§e", "Øvede"),
    ADVANCED("ADVANCED", "§6", "Avanceret"),
    EXPERT("EXPERT", "§c", "Expert");

    @Getter
    private final String id;
    @Getter
    private final String color;
    @Getter
    private final String name;

    MapDifficulty(String id, String color, String name) {
        this.id = id;
        this.color = color;
        this.name = name;
    }

}
