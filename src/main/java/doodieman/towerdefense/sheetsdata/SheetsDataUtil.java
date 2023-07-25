package doodieman.towerdefense.sheetsdata;

import doodieman.towerdefense.sheetsdata.dataobjects.SheetMobType;
import doodieman.towerdefense.sheetsdata.dataobjects.SheetRound;
import lombok.Getter;
import org.bukkit.Bukkit;

public class SheetsDataUtil {

    @Getter
    private static SheetsDataUtil instance;

    final SheetsDataManager manager;

    public SheetsDataUtil(SheetsDataManager manager) {
        this.manager = manager;
        instance = this;
    }

    public SheetMobType getSheetMob(String mobTypeName) {
        for (SheetMobType mobType : manager.getSheetMobsList()) {
            if (mobType.getName().equalsIgnoreCase(mobTypeName)) {
                return mobType;
            }
        }
        return null;
    }

    public SheetRound getRound(int roundNumber) {
        for (SheetRound round : manager.getSheetRoundList()) {
            if (round.getRound() == roundNumber) {
                return round;
            }
        }
        return null;
    }

}
