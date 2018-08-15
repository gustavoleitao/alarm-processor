/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.simulator;

import br.ufrn.lii.alarmprocessing.busines.comunication.AlarmListener;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmDefinition;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmDefinitionSet;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmTypes;
import br.ufrn.lii.alarmprocessing.model.configuration.Configuration;
import br.ufrn.lii.alarmprocessing.model.configuration.ConfigurationSet;
import br.ufrn.lii.alarmprocessing.model.domain.Alarm;
import br.ufrn.lii.alarmprocessing.model.domain.SignalData;

import java.util.*;

/**
 * @author Gustavo
 */
public class AlarmProcessor {

    private final List<Alarm> alarmBuffer = new ArrayList<>();
    public AlarmDefinitionSet alarmDefinitionSet;
    private final List<AlarmListener> alarmListener = new ArrayList<>();
    private int alarmBufferTime = 10;
    private int alarmBufferSize = 100;
    public long lastTimeSentAlarmData = new Date().getTime();
    public Map<String, Alarm> activeAlarms = new HashMap<>();
    private final ConfigurationSet configurationSet;

    public AlarmProcessor(AlarmDefinitionSet alarmDefinitionSet, ConfigurationSet configuration) {
        this.alarmDefinitionSet = alarmDefinitionSet;
        this.configurationSet = configuration;
    }

    public int getAlarmBufferTime() {
        return alarmBufferTime;
    }

    public void setAlarmBufferTime(int alarmBufferTime) {
        this.alarmBufferTime = alarmBufferTime;
    }

    public int getAlarmBufferSize() {
        return alarmBufferSize;
    }

    public void setAlarmBufferSize(int alarmBufferSize) {
        this.alarmBufferSize = alarmBufferSize;
    }

    private void fireAlarm(List<Alarm> alarms) {
        for (AlarmListener listener : alarmListener) {
            listener.onEvent(alarms);
        }
    }

    public void processAlarm(SignalData current) {
        if (alarmDefinitionSet != null) {
            Configuration config = configurationSet.getConfigurationByProcessTag(current.getTag());
            if (config != null) {
                List<AlarmDefinition> definitions = alarmDefinitionSet.getDefinitionsByTag(config.getAlarmTag());
                if (definitions != null) {
                    List<Alarm> alarms = new ArrayList<>();
                    for (AlarmDefinition alarmDefinition : definitions) {
                        Alarm alarm;
                        if (evaluateStartAlarm(current, alarmDefinition)) {
                            AlarmTypes alarmType = AlarmTypes.valueOf(alarmDefinition.getAlarmTypes().getTypeStr());
                            alarm = new Alarm(alarmDefinition.getTag(), alarmType, alarmDefinition
                                    .getPriority(), current.getDate(), Alarm.AlarmState.ACTIVE);
                            alarms.add(alarm);
                        } else if (evaluateEndAlarm(current, alarmDefinition)) {
                            AlarmTypes alarmType = AlarmTypes.valueOf(alarmDefinition.getAlarmTypes().getTypeStr());
                            alarm = new Alarm(alarmDefinition.getTag(), alarmType, alarmDefinition
                                    .getPriority(), current.getDate(), Alarm.AlarmState.INACTIVE);
                            alarms.add(alarm);
                        }
                    }
                    alarms = filterIrrelevantAlarms(alarms);
                    processActiveAlarms(alarms);
                    sendToBufferAndClient();
                }
            }
        }
    }

    private boolean evaluateEndAlarm(SignalData current, AlarmDefinition alarmDefinition) {
        double threshhold = alarmDefinition.getThreshold();
        double deadband = alarmDefinition.getDeadband();
        double margin = Math.abs(threshhold * deadband);

        switch (alarmDefinition.getAlarmTypes()) {
            case HIGH:
            case HIHI:
                return current.getValue() < threshhold - margin;
            case LOW:
            case LOLO:
                return current.getValue() > threshhold + margin;
            default:
                return Boolean.FALSE;
        }
    }

    private boolean evaluateStartAlarm(SignalData current, AlarmDefinition alarmDefinition) {
        switch (alarmDefinition.getAlarmTypes()) {
            case HIGH:
            case HIHI:
                return current.getValue() > alarmDefinition.getThreshold();
            case LOW:
            case LOLO:
                return current.getValue() < alarmDefinition.getThreshold();
            default:
                return Boolean.FALSE;
        }

    }

    private void sendToBufferAndClient() {

        long now = new Date().getTime();
        if (alarmBuffer.size() >= alarmBufferSize || (now - lastTimeSentAlarmData > alarmBufferTime)) {
            lastTimeSentAlarmData = new Date().getTime();
            fireAlarm(alarmBuffer);
            alarmBuffer.clear();
        }
    }

    private List<Alarm> filterIrrelevantAlarms(List<Alarm> alarms) {
        Alarm relevantAlarm = null;
        List<Alarm> filteredAlarms = new ArrayList<>();
        for (Alarm alarm : alarms) {
            if (isActive(alarm)) {
                if (relevantAlarm == null) {
                    relevantAlarm = alarm;
                } else {
                    if (alarm.getType().getRelevance() > relevantAlarm.getType().getRelevance()) {
                        filteredAlarms.add(relevantAlarm.toInactive());
                        relevantAlarm = alarm;
                    }
                }
            } else {
                filteredAlarms.add(alarm);
            }
        }
        if (relevantAlarm != null) {
            filteredAlarms.add(relevantAlarm);
        }
        return filteredAlarms;
    }

    private static boolean isActive(Alarm alarm) {
        return alarm.getState().equals(Alarm.AlarmState.ACTIVE);
    }

    private void processActiveAlarms(List<Alarm> alarms) {
        for (Alarm alarm : alarms) {
            processActiveAlarms(alarm);
        }

    }

    private void processActiveAlarms(Alarm alarm) {
        Alarm alarmActive = activeAlarms.get(alarm.getTag());

        if (alarmActive == null) {
            if (isActive(alarm)) {
                alarmBuffer.add(alarm);
                activeAlarms.put(alarm.getTag(), alarm);
            }
        } else {

            AlarmTypes activeType = alarmActive.getType();
            if (isActive(alarm)) {
                if (!activeType.equals(alarm.getType())) {
                    alarmBuffer.add(new Alarm(alarm.getTag(), alarmActive.getType(), alarmActive.getPriority(),
                            alarmActive.getTimestamp(), Alarm.AlarmState.INACTIVE));
                    alarmBuffer.add(alarm);
                    activeAlarms.put(alarm.getTag(), alarm);
                }
            } else {
                if (activeType.equals(alarm.getType())) {
                    alarmBuffer.add(alarm);
                    activeAlarms.remove(alarm.getTag());
                }
            }

        }
    }

    public void addAlarmListener(AlarmListener listener) {
        alarmListener.add(listener);
    }

}
