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
    }

    public static String convertToDanishTime(int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Seconds must be a non-negative integer.");
        }

        int days = seconds / (60 * 60 * 24);
        seconds -= days * (60 * 60 * 24);

        int hours = seconds / (60 * 60);
        seconds -= hours * (60 * 60);

        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        StringBuilder danishTime = new StringBuilder();

        if (days > 0) {
            danishTime.append(days);
            danishTime.append(days == 1 ? " dag" : " dage");
        }

        if (hours > 0) {
            if (danishTime.length() > 0) {
                danishTime.append(" og ");
            }
            danishTime.append(hours);
            danishTime.append(hours == 1 ? " time" : " timer");
        }

        if (minutes > 0) {
            if (danishTime.length() > 0) {
                danishTime.append(" og ");
            }
            danishTime.append(minutes);
            danishTime.append(minutes == 1 ? " minut" : " minutter");
        }

        if (remainingSeconds > 0 || danishTime.length() == 0) {
            if (danishTime.length() > 0) {
                danishTime.append(" og ");
            }
            danishTime.append(remainingSeconds);
            danishTime.append(remainingSeconds == 1 ? " sekund" : " sekunder");
        }

        return danishTime.toString();
    }

}
