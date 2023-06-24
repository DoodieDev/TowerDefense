package doodieman.towerdefense.game.values;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum Round {

    ROUND_1(1, 40L, Arrays.asList(
        new MobTypeStack(5,MobType.ZOMBIE1),
        new MobTypeStack(5,MobType.SKELETON1)
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
