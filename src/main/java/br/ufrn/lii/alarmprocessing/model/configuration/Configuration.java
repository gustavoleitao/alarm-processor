/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.configuration;

/**
 *
 * @author Gustavo
 */
public class Configuration {

    public enum PredictionType {
        LINEAR, QUADRITIC, NONE;
    }

    private String alarmTag;
    private String processTag;
    private Double filterSignalValue;
    private Double filterSpeedValue;
    private PredictionType predictionType;

    public Configuration(String alarmTag, String processTag, Double filterSignalValue, Double filterSpeedValue) {
        this.alarmTag = alarmTag;
        this.processTag = processTag;
        this.filterSignalValue = filterSignalValue;
        this.filterSpeedValue = filterSpeedValue;
        predictionType = PredictionType.LINEAR;
    }

    public void setPredictionType(PredictionType predictionType) {
        this.predictionType = predictionType;
    }

    public PredictionType getPredictionType() {
        return predictionType;
    }

    public String getAlarmTag() {
        return alarmTag;
    }

    public void setAlarmTag(String alarmTag) {
        this.alarmTag = alarmTag;
    }

    public String getProcessTag() {
        return processTag;
    }

    public void setProcessTag(String processTag) {
        this.processTag = processTag;
    }

    public Double getFilterSignalValue() {
        return filterSignalValue;
    }

    public void setFilterSignalValue(Double filterSignalValue) {
        this.filterSignalValue = filterSignalValue;
    }

    public Double getFilterSpeedValue() {
        return filterSpeedValue;
    }

    public void setFilterSpeedValue(Double filterSpeedValue) {
        this.filterSpeedValue = filterSpeedValue;
    }

}
