package br.ufrn.lii.alarmprocessing.busines.alarmtunning;

import br.ufrn.lii.alarmprocessing.busines.util.ToStringFunction;
import br.ufrn.lii.alarmprocessing.model.domain.MinMax;

import java.util.List;

/**
 * Created by Gustavo on 04/07/2016.
 */
public class MinToString implements ToStringFunction<List<MinMax>> {

    public static final String SEPARATOR = ";";

    @Override
    public String apply(List<MinMax> listMinMax) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < listMinMax.size(); i++) {
            stringBuilder.append(listMinMax.get(i).getMin());
            if (i < listMinMax.size() - 1) {
                stringBuilder.append(SEPARATOR);
            }
        }

        return stringBuilder.toString();
    }
}
