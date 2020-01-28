package clockworks.util;

public class InputUtils {
    public static double linearMap(double val, double inMin, double inMax, double outMin, double outMax) {
        return (val - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;

    }

    public static double limitValue(double input) {
        return limitValue(input, -1.0, 1.0);
    }

    public static double limitValue(double input, double min, double max) {
        if(min > max)
            return 0;

        if(input < min)
            input = min;
        else if(input > max)
            input = max;

        return input;
    }
}

