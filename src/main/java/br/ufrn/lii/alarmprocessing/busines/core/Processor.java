/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.core;

import br.ufrn.lii.alarmprocessing.model.domain.Signal;
import br.ufrn.lii.alarmprocessing.model.domain.SignalData;
import br.ufrn.lii.alarmprocessing.busines.filter.Filter;
import java.util.Date;

/**
 *
 * @author Gustavo Leitão
 */
public class Processor {

    private final Signal signal = new Signal();
    private final Signal filteredSignal = new Signal();
    private Double speed;
    private Double previusSpeed;
    private Double aceleration;
    private final Filter filterSignal;
    private final Filter filterSpeed;
    private final Prediction prediction;

    public Processor(Filter filterSignal, Filter filterSpeed, Prediction prediction) {
        this.filterSignal = filterSignal;
        this.filterSpeed = filterSpeed;
        this.prediction = prediction;

    }

    public void addData(Date timestamp, String tag, double value) {
        signal.addData(new SignalData(timestamp, tag, value));
        double filteredValue = filterSignal.nextValue(value);
        filteredSignal.addData(new SignalData(timestamp, tag, filteredValue));
        updateSpeed(filteredSignal);
        updateAceleration(filteredSignal);
    }

    private void updateAceleration(Signal filteredSignal) {

        SignalData penultimateData = filteredSignal.getPenultimateData();
        SignalData lastData = filteredSignal.getLastData();
        if (lastData != null && penultimateData != null && previusSpeed != null) {
            double diffSpeed = speed - previusSpeed;
            double diffDate = lastData.getDate().getTime() - penultimateData.getDate().getTime();
            aceleration = diffSpeed / diffDate;
        }

    }

    private void updateSpeed(Signal filteredSignal) {

        SignalData lastData = filteredSignal.getLastData();
        SignalData penultimateData = filteredSignal.getPenultimateData();

        if (lastData != null && penultimateData != null) {
            double diffPosition = lastData.getValue() - penultimateData.getValue();
            double diffDate = lastData.getDate().getTime() - penultimateData.getDate().getTime();
            double actualSpeed = diffPosition / diffDate;
            previusSpeed = speed;
            speed = filterSpeed.nextValue(actualSpeed);
        }

    }

    public SignalData getLastData() {
        return signal.getLastData();
    }

    public double getSpeed() {
        return speed;
    }

    public Signal getFilteredSignal() {
        return filteredSignal;
    }

    public Signal getSignal() {
        return signal;
    }

    public Date predict(double value) throws PredictException {
        return prediction.predict(getLastData(), aceleration, speed, value);
    }
    
    public boolean isPredictionEnabled(){
        return prediction.isPredictionEnabled();
    }

    public long predictMillisecondsToValue(double value) throws PredictException {
        double speedLocal = 0;
        if (speed != null) {
            speedLocal = speed;
        }
        double acelerationLocal = 0;
        if (aceleration != null) {
            acelerationLocal = aceleration;
        }
        Date dateToPredict = prediction.predict(getLastData(), acelerationLocal, speedLocal, value);
        long milli = (dateToPredict.getTime() - getLastData().getDate().getTime());
        return milli;
    }
}
