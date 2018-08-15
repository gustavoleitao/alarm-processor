/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.filter;

/**
 *
 * @author Gustavo
 */
public class ExponencialSmoothing implements Filter {

    private double alpha;
    private Double mean = null;

    public ExponencialSmoothing(double alpha) {
        this.alpha = alpha;
    }

    public double nextValue(double actual) {
        if (mean == null) {
            mean = actual;
        } else {
            mean = alpha * actual + (1 - alpha) * mean;
        }
        return mean;
    }

    public Double getMean() {
        return mean;
    }

    public double getAlpha() {
        return alpha;
    }
}
