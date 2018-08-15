package br.ufrn.lii.alarmprocessing.busines;

import br.ufrn.lii.alarmprocessing.busines.comunication.AlarmListener;
import br.ufrn.lii.alarmprocessing.busines.util.FileUtil;
import br.ufrn.lii.alarmprocessing.model.domain.Alarm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Gustavo on 31/08/2016.
 */
public class FileDiffAlarmListner implements AlarmListener {

    private String filePath;
    private Set<String> alarms = new HashSet<>();

    public FileDiffAlarmListner(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void onEvent(Collection<Alarm> alarmList) {
        if (!alarmList.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Alarm alarm : alarmList) {
                if (!alarms.contains(toAlarmString(alarm))) {
                    stringBuilder.append(toAlarmString(alarm)).append("\n");
                    alarms.add(toAlarmString(alarm));
                }
            }
            if (!stringBuilder.toString().isEmpty()) {
                FileUtil.tentaEscrever(filePath, stringBuilder);
            }
        }
    }

    private String toAlarmString(Alarm alarm) {
        return alarm.getTag() + ":" + alarm.getType();
    }



}
