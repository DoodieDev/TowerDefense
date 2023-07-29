package doodieman.towerdefense.utils;

public class NumberUtil {

    public static double getNumberCloseToTarget(double start, double target, double percent) {
        double difference = target - start;
        double differenceOfPercent = difference * percent;
        double result;
        result = start + differenceOfPercent;
        return result;
    }

}
