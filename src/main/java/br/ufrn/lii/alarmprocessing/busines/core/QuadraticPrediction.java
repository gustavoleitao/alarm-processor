/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.core;

import br.ufrn.lii.alarmprocessing.model.domain.SignalData;
import java.util.Date;

/**
 *
 * @author Gustavo
 */
public class QuadraticPrediction implements Prediction {

    public Date predict(SignalData data, double acceleration, double speed, double value) throws PredictException {

        long x0 = data.getDate().getTime();
        double y0 = data.getValue();
        double a = acceleration / 2;
        double b = speed;
        double c = value - y0;

        double delta = Math.pow(b, 2) - (4 * a * c);
        long xr1 = (long) ((-b + (Math.sqrt(delta))) / 2 * a) + x0;
        long xr2 = (long) ((-b - (Math.sqrt(delta))) / 2 * a) + x0;

        Date predictTime1 = new Date(xr1);
        Date predictTime2 = new Date(xr2);

        if (predictTime1.compareTo(data.getDate()) < 0 && predictTime2.compareTo(data.getDate()) < 0) {
            throw new PredictException("Não foi possível realizar a predição. Valor inalcançável");
        } else if (predictTime1.compareTo(data.getDate()) < 0) {
            return predictTime2;
        } else {
            return predictTime1;
        }

    }

    @Override
    public boolean isPredictionEnabled() {
        return Boolean.TRUE;
    }
}
