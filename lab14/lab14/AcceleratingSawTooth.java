package lab14;

import lab14lib.Generator;

public class AcceleratingSawTooth implements Generator {
    private int period;
    private double factor;
    private int state;

    public AcceleratingSawTooth(int period, double factor) {
        this.period = period;
        this.factor = factor;
        state = 0;

    }
    @Override
    public double next() {
        state += 1;
        if (state % period == 0) {
            state = 0;
            period = (int) (period * factor);
        }
        return normalize(state % period);
    }

    /** a helper function that converts values between 0 and period - 1
     *  to values between -1.0 and 1.0 */
    private double normalize(int state) {
        return state * 2.0 / (period - 1) - 1.0;
    }
}
