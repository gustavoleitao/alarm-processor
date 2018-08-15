/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gustavo
 */
public class ConfigurationSet {

    private final Map<String, Configuration> configurationsProcess = new HashMap<>();
    private final Map<String, Configuration> configurationsAlarm = new HashMap<>();
    private Configuration defaultConfiguration;

    public void setDefaultConfiguration(Configuration defaultConfiguration) {
        this.defaultConfiguration = defaultConfiguration;
    }

    public Configuration getDefaultConfiguration() {
        return defaultConfiguration;
    }

    public void addConfiguration(Configuration configuration) {
        configurationsProcess.put(configuration.getProcessTag(), configuration);
        configurationsAlarm.put(configuration.getAlarmTag(), configuration);
    }

    public Configuration getConfigurationByProcessTag(String processTag) {
        return configurationsProcess.get(processTag);
    }

    public Collection<Configuration> getProcessConfiguration() {
        return configurationsProcess.values();
    }

    public Collection<Configuration> getAlarmConfiguration() {
        return configurationsAlarm.values();
    }

    public Configuration getConfigurationByAlarmTag(String alarmTag) {
        return configurationsAlarm.get(alarmTag);
    }

}
