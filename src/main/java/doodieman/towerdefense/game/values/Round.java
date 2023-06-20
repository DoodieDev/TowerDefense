package doodieman.towerdefense.game.values;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum Round {

    ROUND_1(1, 40L, Arrays.asList(
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1
    )),
    
    ROUND_2(2, 30L, Arrays.asList(
        MobType.ZOMBIE1,
        MobType.ZOMBIE2,
        MobType.ZOMBIE1,
        MobType.ZOMBIE2,
        MobType.ZOMBIE1,
        MobType.ZOMBIE2,
        MobType.ZOMBIE1,
        MobType.ZOMBIE2,
        MobType.ZOMBIE1,
        MobType.ZOMBIE2,
        MobType.ZOMBIE1,
        MobType.ZOMBIE2,
        MobType.ZOMBIE1,
        MobType.ZOMBIE2,
        MobType.ZOMBIE1
    )),

    ROUND_3(3, 25L, Arrays.asList(
        MobType.SKELETON1,
        MobType.SKELETON1,
        MobType.SKELETON1,
        MobType.SKELETON1,
        MobType.SKELETON1,
        MobType.SKELETON1,
        MobType.SKELETON1,
        MobType.SKELETON1,
        MobType.SKELETON1,
        MobType.SKELETON1,
        MobType.SKELETON1,
        MobType.SKELETON1,
        MobType.SKELETON1,
        MobType.SKELETON1,
        MobType.SKELETON1
    )),

    ROUND_4(4, 20L, Arrays.asList(
        MobType.SKELETON1,
        MobType.ZOMBIE1,
        MobType.SKELETON1,
        MobType.ZOMBIE1,
        MobType.SKELETON1,
        MobType.ZOMBIE1,
        MobType.SKELETON1,
        MobType.ZOMBIE1,
        MobType.SKELETON1,
        MobType.ZOMBIE1,
        MobType.SKELETON1,
        MobType.ZOMBIE1,
        MobType.SKELETON1,
        MobType.ZOMBIE1,
        MobType.SKELETON1,
        MobType.ZOMBIE1,
        MobType.SKELETON1,
        MobType.ZOMBIE1,
        MobType.SKELETON1,
        MobType.ZOMBIE1
    )),

    ROUND_5(5, 50L, Arrays.asList(
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2
    )),

    ROUND_6(6, 20L, Arrays.asList(
        MobType.SKELETON1,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.SKELETON1,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.SKELETON1,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.SKELETON1,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.SKELETON1,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2
    )),

    ROUND_7(7, 5L, Arrays.asList(
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON3,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON3,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON3,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON2,
        MobType.SKELETON3
    )),

        ROUND_8(8, 10L, Arrays.asList(
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3
     )),

    ROUND_9(9, 40L, Arrays.asList(
        MobType.SKELETON3,
        MobType.SKELETON3,
        MobType.SKELETON3,
        MobType.SKELETON3,
        MobType.SKELETON3,
        MobType.SKELETON3,
        MobType.SKELETON3,
        MobType.SKELETON3,
        MobType.SKELETON3,
        MobType.SKELETON3
    )),

    ROUND_10(10, 15L, Arrays.asList(
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE1,
        MobType.ZOMBIE2,
        MobType.ZOMBIE2,
        MobType.ZOMBIE2,
        MobType.ZOMBIE2,
        MobType.ZOMBIE2,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE3,
        MobType.ZOMBIE4
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
