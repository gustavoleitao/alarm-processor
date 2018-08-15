/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.knowledge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gustavo
 */
public class KnowledgeBase {

    private final Map<AlarmElement, List<Failure>> failures;
    private final Map<Integer, Failure> failuresById;

    public KnowledgeBase() {
        failures = Collections.synchronizedMap(new HashMap<AlarmElement, List<Failure>>());
        failuresById = Collections.synchronizedMap(new HashMap<Integer, Failure>());
    }

    public void addFailure(Failure failure) {
        Collection<AlarmElement> alarms = failure.getTrigger().getAlarms();
        for (AlarmElement alarmElement : alarms) {
            List<Failure> failuresList = failures.get(alarmElement);
            if (failuresList == null) {
                failuresList = new ArrayList<>();
            }
            failuresList.add(failure);
            failures.put(alarmElement, failuresList);
        }
        failuresById.put(failure.getId(), failure);
    }

    public List<Failure> getFailureTriggeedByAlarm(AlarmElement alarm) {
        return failures.get(alarm);
    }

    public Failure getFailureById(int id) {
        return failuresById.get(id);
    }

}
