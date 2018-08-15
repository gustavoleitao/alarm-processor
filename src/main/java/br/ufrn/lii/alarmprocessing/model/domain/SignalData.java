/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.domain;

import java.util.Date;

/**
 *
 * @author gustavoleitao
 */
public class SignalData {

    private final Date date;
    private final String tag;
    private final double value;

    public SignalData(Date date, String tag, double value) {
        this.date = date;
        this.tag = tag;
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public Date getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(date).append(" | ").append(tag).append(" | ").append(value);
        return builder.toString();

    }

}
