/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.alarmdefinition;

/**
 *
 * @author Gustavo
 */
public class AlarmDefinition {

    private final double threshold;
    private final AlarmTypes alarmType;
    private final String tag;
    private final int priority;
    private final double deadband;

    public AlarmDefinition(double threshold, AlarmTypes alarmType, String tag, int priority, double deadband) {
        this.threshold = threshold;
        this.alarmType = alarmType;
        this.tag = tag;
        this.priority = priority;
        this.deadband = deadband;
    }

    public double getDeadband() {
        return deadband;
    }

    public int getPriority() {
        return priority;
    }

    public String getTag() {
        return tag;
    }

    public double getThreshold() {
        return threshold;
    }

    public AlarmTypes getAlarmTypes() {
        return alarmType;
    }

}
