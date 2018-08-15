package br.ufrn.lii.alarmprocessing.busines.alarmtunning;

import br.ufrn.lii.alarmprocessing.model.domain.MinMax;

import java.util.List;

/**
 * Created by gustavoleitao on 17/07/2018.
 */
public interface TuningMethod {

    List<MinMax> alarmSet(List<Double[]> dados, int startPoint);

    String name();

}
