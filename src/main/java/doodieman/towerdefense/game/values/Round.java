package doodieman.towerdefense.game.values;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum Round {

    ROUND_1(1, Arrays.asList(MobType.ZOMBIE, MobType.ZOMBIE));

    @Getter
    private final int id;
    @Getter
    private final List<MobType> mobs;

    private static final HashMap<Integer, Round> GET_BY_ID = new HashMap<>();

    Round(int id, List<MobType> mobs) {
        this.id = id;
        this.mobs = mobs;
    }

    static {
        for (Round round : values())
            GET_BY_ID.put(round.getId(),round);
    }
}
