/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.filter;

import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;

/**
 * A Kalman filter implemented using SimpleMatrix. The code tends to be easier
 * to read and write, but the performance is degraded due to excessive
 * creation/destruction of memory and the use of more generic algorithms. This
 * also demonstrates how code can be seamlessly implemented using both
 * SimpleMatrix and DenseMatrix64F. This allows code to be quickly prototyped or
 * to be written either by novices or experts.
 *
 * @author Peter Abeles
 */
public class KalmanFilterSimple implements KalmanFilter {

    // kinematics description
    //Xk+1 = AXk
    private SimpleMatrix A;
    private SimpleMatrix Q;
    private SimpleMatrix H;
    // sytem state estimate
    private SimpleMatrix x;
    private SimpleMatrix P;

    /**
     * COnfigure o filtro de kalman
     *
     * @param A Matriz A que representa o modelo de estado do sistema
     * @param Q Pertubação do modelo
     * @param H Matriz que relaciona a combinação linear da estimativa da
     * predição e a estimativa do do valor medido
     */
    @Override
    public void configure(DenseMatrix64F A, DenseMatrix64F Q, DenseMatrix64F H) {
        this.A = new SimpleMatrix(A);
        this.Q = new SimpleMatrix(Q);
        this.H = new SimpleMatrix(H);
    }

    @Override
    public void setState(DenseMatrix64F x, DenseMatrix64F P) {
        this.x = new SimpleMatrix(x);
        this.P = new SimpleMatrix(P);
    }

    @Override
    public void predict() {
        // x = F x
        x = A.mult(x);

        // P = F P F' + Q
        P = A.mult(P).mult(A.transpose()).plus(Q);
    }

    @Override
    public void update(DenseMatrix64F _z, DenseMatrix64F _R) {
        // a fast way to make the matrices usable by SimpleMatrix
        SimpleMatrix z = SimpleMatrix.wrap(_z);
        SimpleMatrix R = SimpleMatrix.wrap(_R);

        // y = z - H x
        SimpleMatrix y = z.minus(H.mult(x));

        // S = H P H' + R
        SimpleMatrix S = H.mult(P).mult(H.transpose()).plus(R);

        // K = PH'S^(-1)
        SimpleMatrix K = P.mult(H.transpose().mult(S.invert()));

        // x = x + Ky
        x = x.plus(K.mult(y));

        // P = (I-kH)P = P - KHP
        P = P.minus(K.mult(H).mult(P));
    }

    @Override
    public DenseMatrix64F getState() {
        return x.getMatrix();
    }

    @Override
    public DenseMatrix64F getCovariance() {
        return P.getMatrix();
    }
}