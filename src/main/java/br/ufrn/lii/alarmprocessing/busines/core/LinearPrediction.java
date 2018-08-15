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
public class LinearPrediction implements Prediction {

    public Date predict(SignalData data, double acceleration, double speed, double value) throws PredictException {

        long x0 = data.getDate().getTime();
        double y0 = data.getValue();
        double b = y0 - (speed * x0);
        long x1 = (long) ((value - b) / speed);
        Date predictTime = new Date(x1);

//        if (predictTime.compareTo(data.getDate()) < 0) {
//            throw new PredictException("Não foi possível realizar a predição. Valor inalcançável");
//        }

        return predictTime;

    }

    @Override
    public boolean isPredictionEnabled() {
        return Boolean.TRUE;
    }
}
