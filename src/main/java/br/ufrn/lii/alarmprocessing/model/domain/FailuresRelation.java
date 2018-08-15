package br.ufrn.lii.alarmprocessing.model.domain;

import java.util.*;

/**
 * Created by Gustavo on 13/09/2016.
 */
public class FailuresRelation {

    private Map<Failure, List<Alarm>> failureListMap = new HashMap<>();


    public void addFailure(Failure failure, List<Alarm> alarms) {
        failureListMap.put(failure, alarms);
    }


    public Set<Failure> failures() {
        return failureListMap.keySet();
    }

    public List<Alarm> getAlarms(Failure failure) {
        return failureListMap.get(failure);
    }

}
