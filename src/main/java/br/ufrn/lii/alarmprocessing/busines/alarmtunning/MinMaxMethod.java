package br.ufrn.lii.alarmprocessing.busines.alarmtunning;

import br.ufrn.lii.alarmprocessing.model.domain.MinMax;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gustavoleitao on 17/07/2018.
 */
public class MinMaxMethod implements TuningMethod{

    @Override
    public List<MinMax> alarmSet(List<Double[]> dados, int startPoint) {

        List<MinMax> minMaxes = new ArrayList<>();

        List<Double> mins = new ArrayList<>();
        List<Double> maxs = new ArrayList<>();

        for (int i = startPoint; i < dados.size(); i++) {

            Double[] dado = dados.get(i);

            if (mins.size() == 0) {
                inicializar(mins, dado.length);
            }

            if (maxs.size() == 0) {
                inicializar(maxs, dado.length);
            }

            for (int j = 0; j < dado.length; j++) {
                if (mins.get(j) == null || dado[j] < mins.get(j)) {
                    mins.set(j, dado[j]);
                }
                if (maxs.get(j) == null || dado[j] > maxs.get(j)) {
                    maxs.set(j, dado[j]);
                }
            }
        }

        int index = 0;
        for (Double min : mins) {
            minMaxes.add(MinMax.from(min, maxs.get(index)));
            index++;
        }

        return minMaxes;

    }

    @Override
    public String name() {
        return "minmax";
    }

    private void inicializar(List<Double> list, int length) {
        for (int i = 0; i < length; i++) {
            list.add(null);
        }
    }

}
