package br.ufrn.lii.alarmprocessing.busines.alarmtunning;

import br.ufrn.lii.alarmprocessing.model.domain.Mean3Sigma;
import br.ufrn.lii.alarmprocessing.model.domain.MinMax;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gustavoleitao on 17/07/2018.
 */
public class SigmaMethod implements TuningMethod{

    @Override
    public List<MinMax> alarmSet(List<Double[]> dados, int startPoint) {
        List<Mean3Sigma> sigma = tresSigma(dados, startPoint);
        List<MinMax> minMaxList = new ArrayList<>();
        for (Mean3Sigma mean: sigma){
            double min = mean.getMean() - 3 * mean.getSigma();
            double max = mean.getMean() + 3 * mean.getSigma();
            minMaxList.add(MinMax.from(min, max));
        }
        return minMaxList;
    }

    @Override
    public String name() {
        return "3sigma";
    }

    private List<Mean3Sigma> tresSigma(List<Double[]> dados, int startPoint) {

        List<Mean3Sigma> sigma = new ArrayList<>();
        Map<Integer,DescriptiveStatistics> map = new HashMap<>();

        for (int i = startPoint; i < dados.size(); i++) {

            Double[] dado = dados.get(i);
            int index = 0;
            for (Double d: dado){
                DescriptiveStatistics desc = map.get(index);
                if (desc == null){
                    map.put(index, new DescriptiveStatistics() );
                }
                map.get(index).addValue(d);
                index++;
            }

        }

        for (Map.Entry<Integer, DescriptiveStatistics> entry : map.entrySet()) {
            double mean = entry.getValue().getMean();
            double desvpad = entry.getValue().getStandardDeviation();
            sigma.add(new Mean3Sigma(mean, desvpad));
        }

        return sigma;

    }
}
