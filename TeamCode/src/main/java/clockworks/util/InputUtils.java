package clockworks.util;

public class InputUtils {
    /**
     * Maps an input value to a new output value through a linear equation.
     * @param val the value to map
     * @param inMin the minimum input value
     * @param inMax the maximum input value
     * @param outMin the minimum output value
     * @param outMax the maximum output value
     * @return the new value
     */
    public static double linearMap(double val, double inMin, double inMax, double outMin, double outMax) {
        return (val - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;

    }

    /**
     * Limits the value of an input to the range of [-1, 1]
     * @param input the input to limit
     * @return the limited value. In the range of [-1, 1]
     */
    public static double limitValue(double input) {
        return limitValue(input, -1.0, 1.0);
    }

    /**
     * Limits the value of an input
     * @param input the input value to limit
     * @param min the minimum value to limit to
     * @param max the maximum value to limit to
     * @return the limited value
     */
    public static double limitValue(double input, double min, double max) {
        if(min > max)
            return 0;

        if(input < min)
            input = min;
        else if(input > max)
            input = max;

        return input;
    }

    /**
     * Finds coterminal angles in degrees
     * @param degrees the angle to reduce
     * @return the smallest coterminal angle to turn to
     */
    public static double findCoterminalAngle(double degrees) {
        while(degrees >= 360) {
            degrees -= 360;
        }

        while(degrees <= -360) {
            degrees += 360;
        }

        return degrees;
    }

    private InputUtils() {

    }
}

