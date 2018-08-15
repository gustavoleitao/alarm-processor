/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.alarmdefinition;

/**
 *
 * @author Gustavo
 */
public enum AlarmTypes {

    HIHI("HIHI",220), HIGH("HIGH",210), LOW("LOW",110), LOLO("LOLO",120); 

    private final String typeStr;
    private final int relevance;

    private AlarmTypes(String typeStr, int relevance) {
        this.typeStr = typeStr;
        this.relevance = relevance;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public int getRelevance() {
        return relevance;
    }
    
}
