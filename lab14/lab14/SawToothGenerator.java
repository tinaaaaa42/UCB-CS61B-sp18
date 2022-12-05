package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int period;
    private int state;

    public SawToothGenerator(int period) {
        this.period = period;
        state = 0;
    }
    @Override
    public double next() {
        state += 1;
        return normalize(state % period);
    }

    /** a helper function that converts values between 0 and period - 1
     *  to values between -1.0 and 1.0 */
    private double normalize(int state) {
        return state * 2.0 / (period - 1) - 1.0;
    }
}
