package doodieman.towerdefense.sheetsdata.dataobjects;

import doodieman.towerdefense.sheetsdata.SheetsDataUtil;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class SheetRound {

    private final int round;

    private final List<SheetMobCluster> mobClusters;

    public SheetRound(int round, List<Object> roundData) throws Exception {
        this.round = round;
        this.mobClusters = new ArrayList<>();

        for (int i = 1; i < roundData.size(); i++) {
               String mobClusterData = (String) roundData.get(i);
               String[] mobClusterDataSplit = mobClusterData.split("x");

               String mobTypeName = mobClusterDataSplit[0];
               int mobAmount = Integer.parseInt(mobClusterDataSplit[1]);

               if (mobAmount <= 0) continue;

               SheetMobType mobType = SheetsDataUtil.getInstance().getSheetMob(mobTypeName);
               if (mobType == null) {
                   throw new Exception("The mobtype "+mobTypeName+" does not exist!");
               }


               SheetMobCluster cluster = new SheetMobCluster(mobType, mobAmount);

               this.mobClusters.add(cluster);
        }

    }

}
