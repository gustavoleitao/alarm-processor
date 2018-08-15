/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Gustavo
 */
public class Diagnostic {

    private Date timestamp;
    private final String mensagem;
    private final int priority;
    private double ocurrence;
    private double coverage;
    private final List<Alarm> alarms = new ArrayList<>();
    private final int failureID;

    public Diagnostic(int failureID, String mensagem, int priority, double ocurrence, Date timestamp) {
        this.mensagem = mensagem;
        this.priority = priority;
        this.ocurrence = ocurrence;
        this.failureID = failureID;
        this.timestamp = timestamp;
    }

    public int getFailureID() {
        return failureID;
    }

    public String getMensagem() {
        return mensagem;
    }

    public int getPriority() {
        return priority;
    }

    public double getOcurrence() {
        return ocurrence;
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public void setOcurrence(Date timestamp, double ocurrence) {
        this.ocurrence = ocurrence;
        this.timestamp = timestamp;
    }

    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getCoverage() {
        return coverage;
    }

    public void setCoverage(Date timestamp, double coverage) {
        this.coverage = coverage;
        this.timestamp = timestamp;
    }

}
