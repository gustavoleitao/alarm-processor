/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.alarmdefinition;

/**
 * @author Gustavo
 */
public class AlarmNode {

    private final int priority;
    private final double threshold;
    private final double deadband;

    public AlarmNode(int priority, double threshold, double deadband) {
        this.priority = priority;
        this.threshold = threshold;
        this.deadband = deadband;
    }

    public double getDeadband() {
        return deadband;
    }

    public int getPriority() {
        return priority;
    }

    public double getThreshold() {
        return threshold;
    }

}
