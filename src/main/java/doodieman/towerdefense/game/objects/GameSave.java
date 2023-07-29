package doodieman.towerdefense.game.objects;

import doodieman.towerdefense.game.values.Difficulty;
import doodieman.towerdefense.maps.objects.Map;
import doodieman.towerdefense.playerdata.PlayerDataUtil;
import doodieman.towerdefense.playerdata.objects.PlayerData;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameSave {

    @Getter
    private final OfflinePlayer player;
    @Getter
    private final Map map;


    @Getter
    private double health;
    @Getter
    private double gold;
    @Getter
    private int round;
    @Getter
    private Difficulty difficulty;
    @Getter
    private Date date;

    public GameSave(OfflinePlayer player, Map map) {
        this.player = player;
        this.map = map;

        this.load();
    }

    private void load() {
        PlayerData playerData = PlayerDataUtil.getData(player);
        FileConfiguration config = playerData.getFile();

        ConfigurationSection section = config.getConfigurationSection("saves."+map.getMapID());

        this.health = section.getDouble("health");
        this.round = section.getInt("round");
        this.gold = section.getDouble("gold");
        this.difficulty = Difficulty.valueOf(section.getString("difficulty"));
        this.date = new Date(section.getLong("date"));
    }

    public String getFormattedDate() {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(this.date);
    }

    public String getFormattedTime() {
        String pattern = "HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(this.date);
    }

}
