/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.main;

import br.ufrn.lii.alarmprocessing.busines.comunication.ProcessListener;
import br.ufrn.lii.alarmprocessing.model.domain.SignalData;
import br.ufrn.lii.alarmprocessing.busines.simulator.Engine;
import java.util.Collection;

/**
 *
 * @author Gustavo
 */
public class ClientProcessTest implements ProcessListener {

    private final Engine engine;

    public ClientProcessTest(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void onData(Collection<SignalData> data) {
        for (SignalData signalData : data) {
            //System.out.println("Data arrived: " + data);
            engine.addData(signalData);
        }
    }

}
