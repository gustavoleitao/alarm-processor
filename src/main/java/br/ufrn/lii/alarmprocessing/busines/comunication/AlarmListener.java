/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.comunication;

import br.ufrn.lii.alarmprocessing.model.domain.Alarm;
import java.util.Collection;

/**
 *
 * @author Gustavo
 */
public interface AlarmListener {

    void onEvent(Collection<Alarm> alarmList);

}
