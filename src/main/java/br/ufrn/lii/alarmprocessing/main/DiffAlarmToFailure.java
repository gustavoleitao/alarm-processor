package br.ufrn.lii.alarmprocessing.main;

import br.ufrn.lii.alarmprocessing.busines.SimilarityBatch;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmTypes;
import br.ufrn.lii.alarmprocessing.model.domain.Alarm;
import br.ufrn.lii.alarmprocessing.model.domain.Failure;
import br.ufrn.lii.alarmprocessing.model.domain.FailuresRelation;
import br.ufrn.lii.alarmprocessing.view.GraphViewer;
import org.apache.commons.math3.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Gustavo on 13/09/2016.
 */
public class DiffAlarmToFailure {

    public List<Alarm> toAlarm(String input) throws IOException {

        List<Alarm> alarms = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] alarmData = sCurrentLine.split(":");
                alarms.add(new Alarm(alarmData[0], AlarmTypes.valueOf(alarmData[1])));
            }
        }

        return alarms;
    }

    public FailuresRelation failuresFromFile() throws IOException {
        FailuresRelation failuresRelation = new FailuresRelation();
        SimilarityBatch<Alarm> alarmSimilarityBatch = new SimilarityBatch<>();

        for (int i = 0; i < 28; i++) {
            int idv = i + 1;
            String id = "idv-" + idv;
            List<Alarm> alarms = toAlarm("result/alarm-diff-idv" + idv + ".txt");
            failuresRelation.addFailure(new Failure(id), alarms);
            alarmSimilarityBatch.addSet(id, new HashSet<>(alarms));
        }

        Map<Pair<String, String>, Double> resultSimilarity = alarmSimilarityBatch.calculate();
        System.out.println(alarmSimilarityBatch.toString(resultSimilarity));

        return failuresRelation;
    }


    public static void main(String[] args) throws IOException {
        DiffAlarmToFailure diffAlarmToFailure = new DiffAlarmToFailure();
        FailuresRelation failure = diffAlarmToFailure.failuresFromFile();
        GraphViewer graphViewer = new GraphViewer();
        graphViewer.toGraph(failure);
    }


}
