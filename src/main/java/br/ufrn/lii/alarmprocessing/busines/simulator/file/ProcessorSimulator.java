/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.simulator.file;

import br.ufrn.lii.alarmprocessing.busines.comunication.AlarmListener;
import br.ufrn.lii.alarmprocessing.busines.comunication.ProcessListener;
import br.ufrn.lii.alarmprocessing.busines.simulator.AlarmProcessor;
import br.ufrn.lii.alarmprocessing.busines.simulator.ProcessProcessor;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmDefinitionSet;
import br.ufrn.lii.alarmprocessing.model.configuration.ConfigurationSet;
import br.ufrn.lii.alarmprocessing.model.domain.SignalData;
import java.io.IOException;
import java.util.List;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gustavo
 */
public class ProcessorSimulator implements Runnable {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ProcessorSimulator.class);

    private final String baseFilePath;
    private LoadSimFile simulator;
    private double speed = 1.0;

    private final AlarmProcessor alarmProcessor;
    private final ProcessProcessor processProcessor;
    private final DateTime baseDateTime;

    public ProcessorSimulator(String baseFilePath, AlarmDefinitionSet alarmDefinitionSet, ConfigurationSet configurationSet, DateTime baseDateTime) {
        this.baseFilePath = baseFilePath;
        alarmProcessor = new AlarmProcessor(alarmDefinitionSet, configurationSet);
        processProcessor = new ProcessProcessor();
        this.baseDateTime = baseDateTime;
    }

    public ProcessorSimulator(String baseFilePath, AlarmDefinitionSet alarmDefinitionSet, ConfigurationSet configurationSet) {
        this(baseFilePath, alarmDefinitionSet, configurationSet, DateTime.now());
    }

    public int getProccessBufferSize() {
        return processProcessor.getProccessBufferSize();
    }

    public void setProccessBufferSize(int proccessBufferSize) {
        processProcessor.setProccessBufferSize(proccessBufferSize);
    }

    public int getProccessBufferTime() {
        return processProcessor.getProccessBufferTime();
    }

    public void setProccessBufferTime(int proccessBufferTime) {
        processProcessor.setProccessBufferTime(proccessBufferTime);
    }

    public void setAlarmBufferTime(int alarmBufferTime) {
        alarmProcessor.setAlarmBufferTime(alarmBufferTime);
    }

    public void setAlarmBufferSize(int alarmBufferSize) {
        alarmProcessor.setAlarmBufferSize(alarmBufferSize);
    }

    public int getAlarmBufferTime(int alarmBufferTime) {
        return alarmProcessor.getAlarmBufferTime();
    }

    public int getAlarmBufferSize(int alarmBufferSize) {
        return alarmProcessor.getAlarmBufferSize();
    }

    public void addProcessListener(ProcessListener listener) {
        processProcessor.addListner(listener);
    }

    public void addAlarmListener(AlarmListener listener) {
        alarmProcessor.addAlarmListener(listener);
    }

    public LoadSimFile getSimulator() {
        return simulator;
    }

    public void setSimulator(LoadSimFile simulator) {
        this.simulator = simulator;
    }

    private void start() throws IOException, InterruptedException {
        simulator = new LoadSimFile(baseDateTime);
        List<SignalData> data = simulator.loadFile(baseFilePath);
        processData(data);
    }

    private void processData(List<SignalData> data) throws InterruptedException {
        for (int i = 0; i < data.size(); i++) {
            SignalData current = data.get(i);
            processProcessor.processData(current);
            alarmProcessor.processAlarm(current);
            if ((i + 1) < data.size()) {
                SignalData next = data.get(i + 1);
                long nextTime = next.getDate().getTime();
                long currentTime = current.getDate().getTime();
                long sleepTime = Math.round((nextTime - currentTime) / speed);
                Thread.sleep(sleepTime);
            }
        }
    }

    @Override
    public void run() {
        try {
            logger.info("Iniciando simulação.");
            start();
            logger.info("Fim da simulação.");
        } catch (IOException ex) {
            logger.error("Falha ao carregar o arquivo de simulação.", ex);
        } catch (InterruptedException ex) {
            logger.error("Falha ao carregar o arquivo de simulação.", ex);
        }
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

}
