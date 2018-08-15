/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.filter;

/**
 *
 * @author gustavoleitao
 */
public interface Filter {
    
     public double nextValue(double actual);
    
}
