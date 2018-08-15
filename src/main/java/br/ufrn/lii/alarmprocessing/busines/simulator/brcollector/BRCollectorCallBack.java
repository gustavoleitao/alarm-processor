/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.simulator.brcollector;

import br.ufrn.lii.alarmprocessing.model.domain.SignalData;
import br.ufrn.lii.commonsdomain.process.ProcessData;
import br.ufrn.lii.commonsdomain.process.TagItem;
import br.ufrn.lii.commonsdomain.process.realtime.RealtimeProcessCallback;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gustavo
 */
public class BRCollectorCallBack extends UnicastRemoteObject implements RealtimeProcessCallback {

    private SimulatorByBRCollector bRCollector;

    public BRCollectorCallBack(SimulatorByBRCollector bRCollector) throws RemoteException {
        this.bRCollector = bRCollector;
    }

    @Override
    public void onDataChange(HashMap<TagItem, ProcessData> hm) throws RemoteException {
        Collection<SignalData> signalData = toSignalData(hm);
        bRCollector.fireEvents(signalData);
    }

    private Collection<SignalData> toSignalData(HashMap<TagItem, ProcessData> hm) {
        Collection<SignalData> result = new ArrayList<SignalData>();
        for (Map.Entry<TagItem, ProcessData> entry : hm.entrySet()) {
            TagItem tagItem = entry.getKey();
            ProcessData processData = entry.getValue();
            SignalData sdata = new SignalData(processData.getTimeStamp(), tagItem.getIdStr(), processData.getValue().getDoubleValue());
            result.add(sdata);
        }

        return result;
    }

}
