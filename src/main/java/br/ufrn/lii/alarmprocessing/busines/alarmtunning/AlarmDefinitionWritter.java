package br.ufrn.lii.alarmprocessing.busines.alarmtunning;

import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmDefinition;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmDefinitionSet;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmTypes;
import br.ufrn.lii.alarmprocessing.model.io.XMLAlarmDefinitions;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

/**
 * Created by Gustavo on 18/08/2016.
 */
public class AlarmDefinitionWritter {


    public void toDefinitionFile(String minFile, String maxFile, String output, String prefix, double margin, double deadband) throws IOException, TransformerException, ParserConfigurationException {
        AlarmDefinitionSet alarmDefinitionSet = getAlarmDefinitionSet(minFile, maxFile, prefix, margin, deadband);
        XMLAlarmDefinitions xmlAlarmDefinitions = new XMLAlarmDefinitions();
        xmlAlarmDefinitions.toXML(alarmDefinitionSet, output);
    }

    private AlarmDefinitionSet getAlarmDefinitionSet(String minFile, String maxFile, String prefix, double margin, double deadband) throws IOException {
        ReadTennesseSimulationData readTennesseSimulationData = new ReadTennesseSimulationData();
        List<Double[]> mins = readTennesseSimulationData.read(minFile, ";");
        List<Double[]> maxs = readTennesseSimulationData.read(maxFile, ";");

        Double[] minArray = mins.get(0);
        Double[] maxsArray = maxs.get(0);

        AlarmDefinitionSet alarmDefinitionSet = new AlarmDefinitionSet();

        for (int i = 0; i < minArray.length; i++) {
            Double lowAlarm = minArray[i];
            Double highAlarm = maxsArray[i];
            AlarmDefinition lowDefinition = new AlarmDefinition(getLowThreshold(margin, lowAlarm), AlarmTypes.LOW, prefix + "-" + getIndex(i), 100, deadband);
            AlarmDefinition highDefinition = new AlarmDefinition(getHighThreshold(margin, highAlarm), AlarmTypes.HIGH, prefix + "-" + getIndex(i), 100, deadband);
            alarmDefinitionSet.add(lowDefinition);
            alarmDefinitionSet.add(highDefinition);
        }

        return alarmDefinitionSet;
    }

    private double getHighThreshold(double margem, Double highAlarm) {
        return highAlarm + margem(highAlarm, margem);
    }

    private double getLowThreshold(double margem, Double lowAlarm) {
        return lowAlarm - margem(lowAlarm, margem);
    }

    private double margem(Double limite, Double margem) {
        return Math.abs(margem * limite);
    }

    private String getIndex(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        if (i + 1 < 10) {
            stringBuilder.append("0");
        }
        return stringBuilder.append(i + 1).toString();
    }

}
