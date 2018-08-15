/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.domain;

import com.logique.util.collections.LimitedQueue;
import com.logique.util.properties.PropertyUtil;
import java.util.List;

/**
 *
 * @author gustavoleitao
 */
public class Signal {

    private final int MAX_CACHE_PROCESS_SIZE = PropertyUtil.getNumericProperty("engine.max_process_cache_size", 256);
    private List<SignalData> signal = new LimitedQueue<SignalData>(MAX_CACHE_PROCESS_SIZE);

    public void addData(SignalData data) {
        signal.add(data);
    }

    public int getSize() {
        return signal.size();
    }

    public SignalData get(int index) {
        return signal.get(index);
    }

    public SignalData getLastData() {
        if (getSize() <= 0) {
            return null;
        }
        return get(getSize() - 1);
    }

    public SignalData getPenultimateData() {
        if (getSize() <= 1) {
            return null;
        }
        return get(getSize() - 2);
    }

}
