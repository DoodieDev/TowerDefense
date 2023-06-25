package doodieman.towerdefense.game.values;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum Round {

    ROUND_1(1, 25L, Arrays.asList(
        new MobTypeStack(20,MobType.ZOMBIE1),
    )),

    ROUND_2(2, 15L, Arrays.asList(
        new MobTypeStack(35,MobType.ZOMBIE1),
    )),

    ROUND_3(3, 20L, Arrays.asList(
        new MobTypeStack(25,MobType.ZOMBIE1),
        new MobTypeStack(5,MobType.SKELETON1)
    )),

    ROUND_4(4, 15L, Arrays.asList(
        new MobTypeStack(35,MobType.ZOMBIE1),
        new MobTypeStack(20,MobType.SKELETON1)
    )),

    ROUND_5(5, 10L, Arrays.asList(
        new MobTypeStack(10,MobType.ZOMBIE1),
        new MobTypeStack(30,MobType.SKELETON1)
    )),

    ROUND_6(6, 25L, Arrays.asList(
        new MobTypeStack(15,MobType.ZOMBIE2),
        new MobTypeStack(15,MobType.SKELETON2)
    )),

    ROUND_7(7, 12.5L, Arrays.asList(
        new MobTypeStack(30,MobType.ZOMBIE1),
        new MobTypeStack(20,MobType.SKELETON1),
        new MobTypeStack(10,MobType.SKELETON2)
    )),

    ROUND_8(8, 15L, Arrays.asList(
        new MobTypeStack(15,MobType.ZOMBIE1),
        new MobTypeStack(15,MobType.ZOMBIE2),
        new MobTypeStack(15,MobType.SKELETON1),
        new MobTypeStack(15,MobType.SKELETON2)
    )),

    ROUND_9(9, 10L, Arrays.asList(
        new MobTypeStack(30,MobType.SKELETON2)
    )),

    ROUND_10(10, 25L, Arrays.asList(
        new MobTypeStack(1,MobType.ZOMBIE4)
    )),

    ROUND_11(11, 20L, Arrays.asList(
        new MobTypeStack(25,MobType.ZOMBIE3),
        new MobTypeStack(10,MobType.SKELETON3)
    )),

    ROUND_12(12, 5L, Arrays.asList(
        new MobTypeStack(30,MobType.ZOMBIE1),
        new MobTypeStack(15,MobType.ZOMBIE2),
        new MobTypeStack(10,MobType.SKELETON3)
    ));



    @Getter
    private final int id;
    @Getter
    private final long spawnDelay;
    @Getter
    private final List<MobTypeStack> mobStack;


    Round(int id, long spawnDelay, List<MobTypeStack> mobStack) {
        this.id = id;
        this.mobStack = mobStack;
        this.spawnDelay = spawnDelay;
    }


    public List<MobType> getMobsToSpawn() {
        List<MobType> mobs = new ArrayList<>();

        for (MobTypeStack stack : this.mobStack) {
            for (int i = 0; i < stack.getAmount(); i++) {
                mobs.add(stack.getMobType());
            }
        }

        return mobs;
    }


    private static final HashMap<Integer, Round> GET_BY_ID = new HashMap<>();

    public static Round getRound(int id) {
        return GET_BY_ID.get(id);
    }

    static {
        for (Round round : values())
            GET_BY_ID.put(round.getId(),round);
    }

}
