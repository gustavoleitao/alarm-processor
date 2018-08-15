/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.filter;

import org.ejml.data.DenseMatrix64F;

/**
 *
 * @author Gustavo
 */
interface KalmanFilter {
    
    void configure(DenseMatrix64F F, DenseMatrix64F Q, DenseMatrix64F H);
    
    void setState(DenseMatrix64F x, DenseMatrix64F P);
    
    void predict();    
    
    void update(DenseMatrix64F _z, DenseMatrix64F _R);
    
    DenseMatrix64F getState();    
    
    DenseMatrix64F getCovariance();
    
}
