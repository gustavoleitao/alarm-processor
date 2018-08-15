/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.main;

import br.ufrn.lii.alarmprocessing.busines.filter.ExponencialSmoothing;
import br.ufrn.lii.alarmprocessing.busines.filter.Filter;
import br.ufrn.lii.alarmprocessing.busines.plot.Plot2D;
import br.ufrn.lii.alarmprocessing.busines.core.LinearPrediction;
import br.ufrn.lii.alarmprocessing.busines.core.PredictException;
import br.ufrn.lii.alarmprocessing.busines.core.Processor;
import br.ufrn.lii.alarmprocessing.model.domain.Signal;
import br.ufrn.lii.alarmprocessing.model.domain.SignalData;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo
 */
public class MainProcessor {

    private Processor processor;

    public static void main(String[] args) {
        MainProcessor mainProcessor = new MainProcessor();
        mainProcessor.start();
    }

    private void start() {
        Filter filterSignal = new ExponencialSmoothing(0.1);
        Filter filterSpeed = new ExponencialSmoothing(0.8);
        processor = new Processor(filterSignal, filterSpeed, new LinearPrediction());
        
        initData();
        plotProcessor();
    }

    private void initData() {
        double alpha = 0.9;
        double beta = 0.2;
        for (int i = 0; i < 130; i++) {
            double data = alpha * Math.sin(i / 20d) + beta * Math.random();
            processor.addData(new Date(), "Tag", data);
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void plotProcessor() {
        Plot2D plot2D = new Plot2D("Teste");
        plot2D.addLineSerie("Sinal filtrado", processor.getFilteredSignal());
        plot2D.addLineSerie("Sinal original", processor.getSignal());
        double valueToPredict = 2;
        Date predict;
        try {
            predict = processor.predict(valueToPredict);
            Signal signal = new Signal();
            signal.addData(new SignalData(predict, "Tag-X", valueToPredict));
            plot2D.addScatterSerie("Predição", signal);
        } catch (PredictException ex) {
            Logger.getLogger(MainProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        plot2D.plot();
    }
}
