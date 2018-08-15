package br.ufrn.lii.alarmprocessing.busines.comunication;

import br.ufrn.lii.alarmprocessing.model.domain.Alarm;

import java.util.Collection;

/**
 * Created by Gustavo on 31/08/2016.
 */
public class NullAlarmListner implements AlarmListener {

    @Override
    public void onEvent(Collection<Alarm> alarmList) {

    }
}
