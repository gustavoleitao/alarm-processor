/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.core;

import br.ufrn.lii.alarmprocessing.model.domain.SignalData;
import java.util.Date;

/**
 *
 * @author Gustavo
 */
public class NonePrediction implements Prediction {

    @Override
    public boolean isPredictionEnabled() {
        return Boolean.FALSE;
    }

    @Override
    public Date predict(SignalData data, double acceleration, double speed, double value) throws PredictException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
