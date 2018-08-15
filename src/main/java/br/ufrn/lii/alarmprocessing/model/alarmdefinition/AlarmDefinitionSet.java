/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.alarmdefinition;

import java.util.*;

/**
 * @author Gustavo
 */
public class AlarmDefinitionSet {

    Map<String, List<AlarmDefinition>> definitionsTag = new HashMap<>();
    Map<String, AlarmDefinition> definitionsTagType = new HashMap<>();

    public void add(AlarmDefinition alarmDefinition) {
        List<AlarmDefinition> definitionsList = definitionsTag.get(alarmDefinition.getTag());
        if (definitionsList == null) {
            definitionsList = new ArrayList<>();
        }
        definitionsList.add(alarmDefinition);
        definitionsTag.put(alarmDefinition.getTag(), definitionsList);
        definitionsTagType.put(getKey(alarmDefinition.getTag(), alarmDefinition.getAlarmTypes().getTypeStr()), alarmDefinition);
    }

    public List<AlarmDefinition> getDefinitionsByTag(String tag) {
        return definitionsTag.get(tag);
    }

    public AlarmDefinition getDefinitionsByTagAndType(String tag, String type) {
        return definitionsTagType.get(getKey(tag, type));
    }

    public Set<String> getTags() {
        return new TreeSet<>(definitionsTag.keySet());
    }

    private String getKey(String tag, String type) {
        StringBuilder builder = new StringBuilder();
        builder.append(tag).append(".").append(type);
        return builder.toString();
    }

}
