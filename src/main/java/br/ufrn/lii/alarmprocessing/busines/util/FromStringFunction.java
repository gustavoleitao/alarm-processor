package br.ufrn.lii.alarmprocessing.busines.util;

import java.util.function.Function;

/**
 * Created by Gustavo on 27/06/2016.
 */
@FunctionalInterface
public interface FromStringFunction<T> extends Function<String,T> {

}
