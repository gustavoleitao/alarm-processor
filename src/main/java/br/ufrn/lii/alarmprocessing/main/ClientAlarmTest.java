/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.main;

import br.ufrn.lii.alarmprocessing.busines.comunication.AlarmListener;
import br.ufrn.lii.alarmprocessing.model.domain.Alarm;
import br.ufrn.lii.alarmprocessing.busines.simulator.Engine;
import java.util.Collection;

/**
 *
 * @author Gustavo
 */
public class ClientAlarmTest implements AlarmListener {

    private final Engine engine;

    public ClientAlarmTest(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void onEvent(Collection<Alarm> alarmList) {
        for (Alarm alarm : alarmList) {
            System.out.println("Alarm arrived: " + alarm);
            engine.addAlarm(alarm);
        }
    }

}
