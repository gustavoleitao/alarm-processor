/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.core;

import br.ufrn.lii.alarmprocessing.model.configuration.Configuration;

/**
 *
 * @author Gustavo
 */
public class PredictionFactory {

    public static Prediction createPrediction(Configuration.PredictionType type) {
        switch (type) {
            case LINEAR:
                return new LinearPrediction();
            case QUADRITIC:
                return new QuadraticPrediction();
            case NONE:
                return new NonePrediction();
        }
        return null;
    }

}
