package doodieman.towerdefense.sheetsdata.dataobjects;

import lombok.Getter;

public class SheetMobCluster {

    @Getter
    private final SheetMobType sheetMobType;
    @Getter
    private final int amount;

    public SheetMobCluster(SheetMobType sheetMobType, int amount) {
        this.sheetMobType = sheetMobType;
        this.amount = amount;
    }

}
