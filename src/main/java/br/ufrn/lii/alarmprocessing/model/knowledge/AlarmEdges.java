/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufrn.lii.alarmprocessing.model.knowledge;

/**
 *
 * @author Gustavo
 */
public class AlarmEdges {
    
    private int delay;
    private AlarmElement from;
    private AlarmElement to;

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public AlarmElement getFrom() {
        return from;
    }

    public void setFrom(AlarmElement from) {
        this.from = from;
    }

    public AlarmElement getTo() {
        return to;
    }

    public void setTo(AlarmElement to) {
        this.to = to;
    }
    
}
