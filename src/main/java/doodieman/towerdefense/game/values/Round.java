package doodieman.towerdefense.game.values;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum Round {

    ROUND_1(1, 20L, Arrays.asList(
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1
    )),
    ROUND_2(2, 20L, Arrays.asList(
        MobType.ZOMBIE2,
        MobType.ZOMBIE2,
        MobType.ZOMBIE2,
        MobType.ZOMBIE2,
        MobType.ZOMBIE2,
        MobType.ZOMBIE2
    )),
    ROUND_3(3, 20L, Arrays.asList(
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE2,
        MobType.ZOMBIE2
    )),
    ROUND_4(4, 20L, Arrays.asList(
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE2,
        MobType.ZOMBIE2,
        MobType.ZOMBIE2
    ));

    @Getter
    private final int id;
    @Getter
    private final long spawnDelay;
    @Getter
    private final List<MobType> mobs;

    private static final HashMap<Integer, Round> GET_BY_ID = new HashMap<>();

    Round(int id, long spawnDelay, List<MobType> mobs) {
        this.id = id;
        this.mobs = mobs;
        this.spawnDelay = spawnDelay;
    }

    public static Round getRound(int id) {
        return GET_BY_ID.get(id);
    }

    static {
        for (Round round : values())
            GET_BY_ID.put(round.getId(),round);
    }
}
