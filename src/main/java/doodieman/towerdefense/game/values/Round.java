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
        new MobTypeStack(3,MobType.ZOMBIE4)
    )),


    
    // 10 ROUNDS COMPLETED

    

    ROUND_11(11, 15L, Arrays.asList(
        new MobTypeStack(25,MobType.ZOMBIE3),
        new MobTypeStack(10,MobType.SKELETON3),
        new MobTypeStack(5,MobType.PIGMAN1)
    )),

    ROUND_12(12, 5L, Arrays.asList(
        new MobTypeStack(30,MobType.ZOMBIE1),
        new MobTypeStack(15,MobType.ZOMBIE2),
        new MobTypeStack(10,MobType.SKELETON3)
    )),

    ROUND_13(13, 10L, Arrays.asList(
        new MobTypeStack(50,MobType.SKELETON1),
        new MobTypeStack(15,MobType.SKELETON2)
    )),

    ROUND_14(14, 3L, Arrays.asList(
        new MobTypeStack(25,MobType.ZOMBIE1),
        new MobTypeStack(10,MobType.SKELETON3),
        new MobTypeStack(25,MobType.ZOMBIE1),
        new MobTypeStack(10,MobType.SKELETON3)
    )),

    ROUND_15(15, 7.5L, Arrays.asList(
        new MobTypeStack(20,MobType.ZOMBIE1),
        new MobTypeStack(15,MobType.ZOMBIE2),
        new MobTypeStack(10,MobType.ZOMBIE3),
        new MobTypeStack(20,MobType.SKELETON1),
        new MobTypeStack(15,MobType.SKELETON3),
        new MobTypeStack(10,MobType.SKELETON3),
        new MobTypeStack(5,MobType.PIGMAN1)
    )),

    ROUND_16(16, 10L, Arrays.asList(
        new MobTypeStack(40,MobType.ZOMBIE3),
        new MobTypeStack(10,MobType.SKELETON3),
        new MobTypeStack(10,MobType.SKELETON2),
        new MobTypeStack(10,MobType.PIGMAN2)
    )),

    ROUND_17(17, 3L, Arrays.asList(
        new MobTypeStack(50,MobType.SKELETON2)
    )),

    ROUND_18(18, 5L, Arrays.asList(
        new MobTypeStack(80,MobType.ZOMBIE3),
        new MobTypeStack(20,MobType.SKELETON4)
    )),

    ROUND_19(19, 2.5L, Arrays.asList(
        new MobTypeStack(50,MobType.SKELETON1),
        new MobTypeStack(20,MobType.SKELETON3),
        new MobTypeStack(10,MobType.SKELETON4)
    )),

    ROUND_20(20, 25L, Arrays.asList(
        new MobTypeStack(5,MobType.SKELETON5)
    )),


    
    // 20 ROUNDS COMPLETED (PROBABLY START GIVING EM IRON/DIA GEAR)

    

    ROUND_21(21, 7.5L, Arrays.asList(
        new MobTypeStack(40,MobType.PIGMAN1),
        new MobTypeStack(15,MobType.PIGMAN2),
    )),

    ROUND_22(22, 5L, Arrays.asList(
        new MobTypeStack(20,MobType.SKELETON4),
        new MobTypeStack(15,MobType.ZOMBIE3)
    )),

    ROUND_23(23, 10L, Arrays.asList(
        new MobTypeStack(10,MobType.PIGMAN3)
    )),

    ROUND_24(24, 1.5L, Arrays.asList(
        new MobTypeStack(50,MobType.SKELETON2),
        new MobTypeStack(1,MobType.PIGMAN4),
        new MobTypeStack(25,MobType.ZOMBIE3)
    )),

    ROUND_25(25, 3L, Arrays.asList(
        new MobTypeStack(25,MobType.ZOMBIE3),
        new MobTypeStack(15,MobType.SKELETON3),
        new MobTypeStack(30,MobType.SKELETON4)
    )),

    ROUND_26(26, 5L, Arrays.asList(
        new MobTypeStack(30,MobType.PIGMAN1),
        new MobTypeStack(15,MobType.PIGMAN2)
    )),

    ROUND_27(27, 2L, Arrays.asList(
        new MobTypeStack(100,MobType.ZOMBIE2),
        new MobTypeStack(60,MobType.SKELETON1),
        new MobTypeStack(45,MobType.ZOMBIE3),
        new MobTypeStack(45,MobType.SKELETON3)
    )),

    ROUND_28(28, 10L, Arrays.asList(
        new MobTypeStack(10,MobType.PIGMAN4),
        new MobTypeStack(10,MobType.SKELETON4),
        new MobTypeStack(10,MobType.PIGMAN4),
        new MobTypeStack(10,MobType.SKELETON4),
        new MobTypeStack(10,MobType.PIGMAN4),
        new MobTypeStack(10,MobType.SKELETON4)
    )),

    ROUND_29(29, 5L, Arrays.asList(
        new MobTypeStack(30,MobType.ZOMBIE3),
        new MobTypeStack(30,MobType.SKELETON4)
    )),

    ROUND_30(30, 25L, Arrays.asList(
        new MobTypeStack(10,MobType.PIGMAN5)
    )),



    // 30 ROUNDS COMPLETED


    
    ROUND_31(31, 1L, Arrays.asList(
        new MobTypeStack(100,MobType.PIGMAN3),
        new MobTypeStack(200,MobType.SKELETON2)
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
