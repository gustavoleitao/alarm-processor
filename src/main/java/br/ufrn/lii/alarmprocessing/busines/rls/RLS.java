/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.rls;

import br.ufrn.lii.alarmprocessing.busines.plot.InvalidMatrixException;
import br.ufrn.lii.alarmprocessing.busines.plot.Plot2D;
import br.ufrn.lii.alarmprocessing.busines.util.MatrixUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.simple.SimpleMatrix;

/**
 * Implementação do Algoritmo RLS
 *
 * @author Gustavo
 */
public class RLS {

    private DenseMatrix64F x;
    private DenseMatrix64F y;
    int numAmostras;

    public RLS(int numAmostras) {
        this.numAmostras = numAmostras;
    }

    public DenseMatrix64F getDSignal() {
        double w0 = 0.001;
        double phi = 0.1;
        DenseMatrix64F dSignal = MatrixUtil.fill(1, numAmostras);
        CommonOps.scale(2 * Math.PI * w0, dSignal);
        MatrixUtil.sin(dSignal);
        return dSignal;
    }

    public DenseMatrix64F getRSignal() {
        double ita = Math.pow(10, 4);
        DenseMatrix64F rSignal = MatrixUtil.ones(1, numAmostras);
        CommonOps.scale(ita, rSignal);
        return rSignal;
    }

    public DenseMatrix64F getXSignal(DenseMatrix64F d) {
        DenseMatrix64F rndnSignal = MatrixUtil.randn(1, numAmostras);
        CommonOps.scale(0.5, rndnSignal);
        DenseMatrix64F xSignal = new DenseMatrix64F(1, numAmostras);
        CommonOps.add(rndnSignal, d, xSignal);
        return xSignal;
    }

    public void process() {
        try {

            double ita = Math.pow(10, 4);
            DenseMatrix64F R = getRSignal();
            DenseMatrix64F D = getDSignal();
            DenseMatrix64F X = getXSignal(D);
            
                       
                       
            //DenseMatrix64F X_trasp = MatrixUtil.zeros(numAmostras, 1);
            DenseMatrix64F W = MatrixUtil.zeros(1, numAmostras);
            //DenseMatrix64F W_trasp = MatrixUtil.zeros(numAmostras, 1);

            DenseMatrix64F Y = MatrixUtil.zeros(1, numAmostras);
            DenseMatrix64F E = MatrixUtil.zeros(1, numAmostras);
            DenseMatrix64F Q = MatrixUtil.zeros(1, numAmostras);
            DenseMatrix64F Z = MatrixUtil.zeros(1, numAmostras);
            DenseMatrix64F ZZ = MatrixUtil.zeros(1, numAmostras);

            DenseMatrix64F YD = MatrixUtil.zeros(1, numAmostras);


            for (int i = 0; i < numAmostras; i++) {

                /* Calculando o Y*/
                //CommonOps.transpose(W, W_trasp);
                double ytemp = W.get(0, i) * X.get(0, i);
                Y.set(0, i, ytemp);

                /* Calculando o erro */
                double etemp = D.get(0, i) - Y.getIndex(0, i);
                E.set(0, i, etemp);

                /* Calculando o Z*/
                double ztemp = R.get(0, i) * X.get(0, i);
                Z.set(0, i, ztemp);

                /* Calculando o Q*/
                //CommonOps.transpose(X, X_trasp);
                double qtemp = X.get(0, i) * Z.get(0, i);
                Q.set(0, i, ztemp);

                /* Calculando ZZ */
                double v = 1 / 1 + qtemp;
                double zztemp = v * Z.getIndex(0, i);
                ZZ.set(0, i, zztemp);

                /* atualizando o peso */
                if (i < numAmostras - 1) {

                    double wtemp = W.get(0, i) + E.get(0, i) * ZZ.get(0, i);
                    W.set(0, i + 1, wtemp);

                    double rtemp = R.get(0, i) - ZZ.get(0, i) * Z.get(0, i);
                    R.set(0, i + 1, rtemp);
                }

            }

            for (int i = 0; i < numAmostras; i++) {
               // CommonOps.transpose(W, W_trasp);
                double ydTemp = W.get(0, i) * X.get(0, i);
                YD.set(0, i, ydTemp);
            }


            Plot2D plot = new Plot2D("RLS");
            //plot.addSerie("D Signal", SimpleMatrix.wrap(D));
            //plot.addSerie("R Signal", SimpleMatrix.wrap(R));
            plot.addLineSerie("X Signal", SimpleMatrix.wrap(X));
            //plot.addSerie("YD Signal", SimpleMatrix.wrap(YD));
            plot.plot();
        } catch (InvalidMatrixException ex) {
            Logger.getLogger(RLS.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
