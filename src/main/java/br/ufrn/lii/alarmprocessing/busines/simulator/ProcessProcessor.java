/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.simulator;

import br.ufrn.lii.alarmprocessing.busines.comunication.ProcessListener;
import br.ufrn.lii.alarmprocessing.model.domain.SignalData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Gustavo
 */
public class ProcessProcessor {

    private final List<ProcessListener> alarmListener = new ArrayList<>();
    private final List<SignalData> processBuffer = new ArrayList<>();
    public long lastTimeSentProcessData = new Date().getTime();

    private int proccessBufferSize = 1;
    private int proccessBufferTime = 100;

    public void setProccessBufferSize(int proccessBufferSize) {
        this.proccessBufferSize = proccessBufferSize;
    }

    public void setProccessBufferTime(int proccessBufferTime) {
        this.proccessBufferTime = proccessBufferTime;
    }

    public int getProccessBufferSize() {
        return proccessBufferSize;
    }

    public int getProccessBufferTime() {
        return proccessBufferTime;
    }

    public void addListner(ProcessListener listner) {
        alarmListener.add(listner);
    }

    public void processData(SignalData atual) {
        processBuffer.add(atual);
        long now = new Date().getTime();
        if (processBuffer.size() >= proccessBufferSize || (now - lastTimeSentProcessData > proccessBufferTime)) {
            lastTimeSentProcessData = new Date().getTime();
            fireData(processBuffer);
            processBuffer.clear();
        }
    }

    private void fireData(Collection<SignalData> processBuffer) {
        for (ProcessListener processListener : alarmListener) {
            processListener.onData(processBuffer);
        }
    }

}
