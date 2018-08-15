package br.ufrn.lii.alarmprocessing.busines.util;

import java.util.function.Function;

/**
 * Created by Gustavo on 04/07/2016.
 */
@FunctionalInterface
public interface ToStringFunction<T> extends Function<T,String> {
}
