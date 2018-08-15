/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.simulator.file;

import br.ufrn.lii.alarmprocessing.busines.comunication.AlarmListener;
import br.ufrn.lii.alarmprocessing.busines.comunication.ProcessListener;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmDefinitionSet;
import br.ufrn.lii.alarmprocessing.model.configuration.ConfigurationSet;
import java.io.IOException;
import org.joda.time.DateTime;

/**
 *
 * @author Gustavo
 */
public class SimulatorRDAByFile {

    private ProcessorSimulator processorSimulator;
    private final String basePath;
    private final AlarmDefinitionSet alarmDefinition;
    private Thread simThread;
    private ConfigurationSet configurationSet;
    private final DateTime baseDateTime;

    public SimulatorRDAByFile(String basePath, AlarmDefinitionSet alarmDefinition, ConfigurationSet configurationSet, DateTime baseDateTime) {
        this.basePath = basePath;
        this.alarmDefinition = alarmDefinition;
        this.configurationSet = configurationSet;
        this.baseDateTime = baseDateTime;
    }

    public void start() throws IOException {
        processorSimulator = new ProcessorSimulator(basePath, alarmDefinition, configurationSet, baseDateTime);
        simThread = new Thread(processorSimulator);
        simThread.start();
    }

    public void setSpeed(double speed) {
        if (processorSimulator != null) {
            processorSimulator.setSpeed(speed);
        }
    }

    public void setProccessBufferSize(int proccessBufferSize) {
        processorSimulator.setProccessBufferSize(proccessBufferSize);
    }

    public void setProccessBufferTime(int proccessBufferTime) {
        processorSimulator.setProccessBufferTime(proccessBufferTime);
    }

    public void waitTerminate() throws InterruptedException {
        simThread.join();
    }

    public void setAlarmBufferSize(int alarmBufferSize) {
        processorSimulator.setAlarmBufferSize(alarmBufferSize);
    }

    public void setAlarmBufferTime(int alarmBufferTime) {
        processorSimulator.setAlarmBufferTime(alarmBufferTime);
    }

    public void addProcessListener(ProcessListener listener) {
        processorSimulator.addProcessListener(listener);
    }

    public void addAlarmListener(AlarmListener listener) {
        processorSimulator.addAlarmListener(listener);
    }

    public void finaliza() {
        simThread.interrupt();
    }

}
