package doodieman.towerdefense.utils;

import doodieman.towerdefense.TowerDefense;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LuckPermsUtil {


    public static String getRankColor(OfflinePlayer player) {
        User user = getLuckPermsUser(player.getUniqueId());
        String prefix = user.getCachedData().getMetaData().getPrefix();
        return ChatColor.translateAlternateColorCodes('&',prefix.substring(0, 2));
    }

    public static String getPrefix(String username) {
        return getPrefix(Bukkit.getOfflinePlayer(username));
    }

    public static String getPrefix(OfflinePlayer player) {
        User user = getLuckPermsUser(player.getUniqueId());
        String prefix = user.getCachedData().getMetaData().getPrefix();
        return ChatColor.translateAlternateColorCodes('&', prefix);
    }

    //Use uuid instead of username!
    public static User getLuckPermsUser(String username) {
        UUID uuid = Bukkit.getOfflinePlayer(username).getUniqueId();
        return getLuckPermsUser(uuid);
    }

    public static User getLuckPermsUser(UUID uuid) {
        LuckPerms luckPerms = TowerDefense.getInstance().getLuckPerms();
        if (!luckPerms.getUserManager().isLoaded(uuid))
            return luckPerms.getUserManager().loadUser(uuid).join();
        return luckPerms.getUserManager().getUser(uuid);
    }


}
