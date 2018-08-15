package br.ufrn.lii.alarmprocessing.model.io;

import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmDefinition;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmDefinitionSet;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmTypes;
import junit.framework.TestCase;

/**
 * Created by Gustavo on 18/08/2016.
 */
public class XMLAlarmDefinitionsTest extends TestCase {

    public void testToXML() throws Exception {

        AlarmDefinitionSet alarmDefinitionSet = new AlarmDefinitionSet();
        alarmDefinitionSet.add(new AlarmDefinition(110, AlarmTypes.HIGH, "TAG-1234", 100, 0.1d));
        alarmDefinitionSet.add(new AlarmDefinition(0, AlarmTypes.LOW, "TAG-1234", 100, 0.1d));

        alarmDefinitionSet.add(new AlarmDefinition(1, AlarmTypes.HIGH, "TAG-1123", 100, 0.1d));
        alarmDefinitionSet.add(new AlarmDefinition(-1, AlarmTypes.LOW, "TAG-1123", 100, 0.1d));

        XMLAlarmDefinitions xmlAlarmDefinitions = new XMLAlarmDefinitions();
        xmlAlarmDefinitions.toXML(alarmDefinitionSet, "simulacoes/tuning.csv.xml");

    }
}