package br.ufrn.lii.alarmprocessing.busines;




import org.apache.commons.math3.util.Pair;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Gustavo on 18/09/2016.
 */
public class SimilarityBatch<T> {

    private Map<String, Set<T>> sets = new LinkedHashMap<>();

    public void addSet(String id, Set<T> set) {
        sets.put(id, set);
    }

    public Map<Pair<String, String>, Double> calculate() {
        Map<Pair<String, String>, Double> pairDoubleMap = new LinkedHashMap<>();
        JaccardIndex<T> jaccardIndex = new JaccardIndex<>();

        for (String idFrom : sets.keySet()) {

            for (String idTo : sets.keySet()) {
                double similarity = jaccardIndex.similarity(sets.get(idFrom), sets.get(idTo));
                pairDoubleMap.put(new Pair(idFrom, idTo), similarity);
            }
        }

        return pairDoubleMap;

    }


    public String toString(Map<Pair<String, String>, Double> result) {
        final StringBuffer sb = new StringBuffer("");
        for (Map.Entry<Pair<String, String>, Double> entry : result.entrySet()) {
            sb.append("(").append(entry.getKey().getFirst()).append(",").append(entry.getKey().getSecond())
                    .append(")=").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
