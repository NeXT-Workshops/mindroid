package mindroid.common.ev3.app;

import lejos.hardware.Battery;

public class Brick {

    /**
     * Experimental measured value of the minimum voltage the brick needs to run.
     * Brick will turn off below that value
     */
    private static float voltageLowerBound = 6.0f;

    /**
     * Voltage, when the brick is fully loaded
     */
    private static float voltageUpperBound = 8.0f;

    /**
     * Returns the load of the battery in percent
     * @return battery status in percent
     */
    public static int getBatteryStatus(){
        //upper bound or current voltage
        float currentVoltage = Math.min(voltageUpperBound,Battery.getVoltage());
        //Lower bound or current voltage
        float currentVoltageCalc = Math.max(voltageLowerBound,currentVoltage);
        //distance between lower and upper voltage bound
        float voltageBound = voltageUpperBound-voltageLowerBound;

        return (int) (((currentVoltageCalc-voltageLowerBound)/voltageBound)*100);
    }
}
