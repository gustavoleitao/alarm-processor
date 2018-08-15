/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.knowledge;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Gustavo
 */
public class Trigger {

    private final Collection<AlarmElement> alarms = new ArrayList<>();

    public void addAlarm(AlarmElement alarm) {
        alarms.add(alarm);
    }

    public boolean removeAlarm(AlarmElement alarm) {
        return alarms.remove(alarm);
    }

    public Collection<AlarmElement> getAlarms() {
        return alarms;
    }

}
