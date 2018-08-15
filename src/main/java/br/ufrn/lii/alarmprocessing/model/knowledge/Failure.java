/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.knowledge;

import java.util.Collection;

/**
 *
 * @author Gustavo
 */
public class Failure {

    private String message;
    private Trigger trigger;
    private int priority;
    private Collection<AlarmElement> alarmsElements;
    private Collection<AlarmEdges> alarmEdges;
    private int id;

    public Failure(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public void setAlarmEdges(Collection<AlarmEdges> alarmEdges) {
        this.alarmEdges = alarmEdges;
    }

    public void setAlarmsElements(Collection<AlarmElement> alarmsElements) {
        this.alarmsElements = alarmsElements;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public Collection<AlarmElement> getAlarmsElements() {
        return alarmsElements;
    }

    public Collection<AlarmEdges> getAlarmEdges() {
        return alarmEdges;
    }

}
