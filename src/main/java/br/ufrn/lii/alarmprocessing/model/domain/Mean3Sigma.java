package br.ufrn.lii.alarmprocessing.model.domain;

/**
 * Created by gustavoleitao on 17/07/2018.
 */
public class Mean3Sigma {

    private double mean;
    private double sigma;

    public Mean3Sigma(double mean, double sigma) {
        this.mean = mean;
        this.sigma = sigma;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }


}
