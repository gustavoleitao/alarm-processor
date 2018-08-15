/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.core;

import br.ufrn.lii.alarmprocessing.model.configuration.Configuration;
import br.ufrn.lii.alarmprocessing.busines.filter.ExponencialSmoothing;

/**
 *
 * @author Gustavo
 */
public class ProcessorFactory {

    public synchronized static Processor createProcessor(Configuration configuration) {
        return new Processor(new ExponencialSmoothing(configuration.getFilterSignalValue()),
                new ExponencialSmoothing(configuration.getFilterSpeedValue()),
                PredictionFactory.createPrediction(configuration.getPredictionType()));
    }

}
