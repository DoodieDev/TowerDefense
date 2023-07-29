package doodieman.towerdefense.chat;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.utils.LuckPermsUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    final ChatHandler handler;

    public ChatListener(ChatHandler handler) {
        this.handler = handler;
        Bukkit.getPluginManager().registerEvents(this, TowerDefense.getInstance());
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();

        String prefix = LuckPermsUtil.getPrefix(player);
        String username = player.getName();
        String message = event.getMessage();

        message = message.replace("%","%%");

        event.setFormat(prefix+username+":§f "+message);
    }

}
