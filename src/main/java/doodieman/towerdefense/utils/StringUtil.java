package doodieman.towerdefense.utils;

import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class StringUtil {

    private final static NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);

    static {
        numberFormat.setMaximumFractionDigits(5);
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String formatNum(double input) {
        return numberFormat.format(input);
    }

    public static String formatNum(BigDecimal input) {
        return numberFormat.format(input);
    }

    public static String progressBar(double current, double target, int size) {

        double progress = current / target;
        int displayProgress = (int) Math.round(progress * size);

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= size; i++) {
            if (i <= displayProgress)
                sb.append("§c|");
            else
                sb.append("§0|");
        }

        return sb.toString();


        /*int size_round = (int) Math.ceil(size / 3);
        int progres_round = (int) Math.ceil(progres / 3);

        String block = "▌";

        String returnString = "§a";
        // TODO Auto-generated constructor stub
        for (int i = 0; i < size_round; i++) {
            if (i == progres_round)
                returnString += "§c";
            returnString += "" + block;
        }
        return returnString;*/
    }

}
