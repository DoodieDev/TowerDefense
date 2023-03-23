package doodieman.towerdefense.playerdata;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.playerdata.objects.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class PlayerDataUtil {

    public static PlayerData getData(OfflinePlayer player) {
        return getHandler().playerData.getOrDefault(player, new PlayerData(player));
    }

    public static PlayerData getData(String uuid) {
        return getData(UUID.fromString(uuid));
    }

    public static PlayerData getData(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        return getHandler().playerData.getOrDefault(player, new PlayerData(player));
    }

    public static PlayerDataHandler getHandler() {
        return TowerDefense.getInstance().getPlayerDataHandler();
    }

}
