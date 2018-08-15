package br.ufrn.lii.alarmprocessing.model.domain;

/**
 * Created by Gustavo on 04/07/2016.
 */
public class MinMax {

    private double min;
    private double max;

    private MinMax(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public static MinMax from(double min, double max){
        return new MinMax(min, max);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    public String toString() {
        return
                "{" + min +
                "," + max +
                '}';
    }
}
