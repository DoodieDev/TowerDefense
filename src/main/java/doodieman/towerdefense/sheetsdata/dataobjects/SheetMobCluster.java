package doodieman.towerdefense.sheetsdata.dataobjects;

import lombok.Getter;
import lombok.Setter;

public class SheetMobCluster {

    @Getter
    private final SheetMobType sheetMobType;
    @Getter @Setter
    private int amount;

    public SheetMobCluster(SheetMobType sheetMobType, int amount) {
        this.sheetMobType = sheetMobType;
        this.amount = amount;
    }

    public SheetMobCluster clone() {
        return new SheetMobCluster(sheetMobType,amount);
    }

}
