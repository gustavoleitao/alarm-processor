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
public interface Prediction {
    
    boolean isPredictionEnabled();

    Date predict(SignalData data, double acceleration, double speed, double value) throws PredictException;
    
}
