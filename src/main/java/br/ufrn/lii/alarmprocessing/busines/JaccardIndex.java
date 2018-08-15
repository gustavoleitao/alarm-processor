package br.ufrn.lii.alarmprocessing.busines;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by gustavoleitao on 17/07/2018.
 */
public class JaccardIndex<T> {


    public double similarity(Set<T> ts1, Set<T> ts2) {
        final Set<T> intersectionSet = intecection(ts1, ts2);
        final Set<T> unionSet = union(ts1, ts2);
        return Double.valueOf(intersectionSet.size()) / Double.valueOf(unionSet.size());
    }

    public double distance(Set<T> set1, Set<T> set2) {
        return 1 - similarity(set1, set2);
    }

    private Set<T> union(Set<T> set1, Set<T> set2){
        Set<T> union = new HashSet<>();
        union.addAll(set1);
        union.addAll(set2);
        return union;
    }

    private Set<T> intecection(Set<T> set1, Set<T> set2){
        Set<T> intecection = new HashSet<>();
        for (T element: set1){
            if (set2.contains(element)){
                intecection.add(element);
            }
        }

        return intecection;

    }

}
