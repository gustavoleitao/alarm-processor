/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.domain;

import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmTypes;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Gustavo
 */
public class Alarm {

    private final String tag;
    private final AlarmTypes type;
    private int priority;
    private Date timestamp;
    private AlarmState state;

    public static enum AlarmState {

        ACTIVE("ACTIVE"), INACTIVE("INACTIVE");

        private String stateStr;

        private AlarmState(String stateStr) {
            this.stateStr = stateStr;
        }

        public String getStateStr() {
            return stateStr;
        }

    }

    public Alarm(String tag, AlarmTypes type, int priority, Date timestamp, AlarmState state) {
        this.tag = tag;
        this.type = type;
        this.priority = priority;
        this.timestamp = timestamp;
        this.state = state;
    }

    public Alarm(String tag, AlarmTypes type) {
        this.tag = tag;
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public AlarmTypes getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    public AlarmState getState() {
        return state;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Alarm toActive() {
        return new Alarm(tag, type, priority, timestamp, AlarmState.ACTIVE);
    }

    public Alarm toInactive() {
        return new Alarm(tag, type, priority, timestamp, AlarmState.INACTIVE);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(timestamp).append(" | ");
        builder.append(tag).append(" | ");
        builder.append(type).append(" | ");
        builder.append(state).append(" | ");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.tag);
        hash = 47 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Alarm other = (Alarm) obj;
        if (!Objects.equals(this.tag, other.tag)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }

}
