/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.util;

import java.util.Random;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

/**
 *
 * @author Gustavo
 */
public class MatrixUtil {

    public static DenseMatrix64F ones(int numRows, int numColumns) {
        DenseMatrix64F denseMatrix64F = new DenseMatrix64F(numRows, numColumns);
        CommonOps.fill(denseMatrix64F, 1);
        return denseMatrix64F;
    }

    public static DenseMatrix64F zeros(int numRows, int numColumns) {
        DenseMatrix64F denseMatrix64F = new DenseMatrix64F(numRows, numColumns);
        CommonOps.fill(denseMatrix64F, 0);
        return denseMatrix64F;
    }

    public static DenseMatrix64F randn(int numRows, int numColumns) {
        DenseMatrix64F denseMatrix64F = new DenseMatrix64F(numRows, numColumns);
        Random random = new Random();
        for (int i = 0; i < denseMatrix64F.numRows; i++) {
            for (int j = 0; j < denseMatrix64F.numCols; j++) {
                denseMatrix64F.set(i, j, random.nextGaussian());
            }
        }
        return denseMatrix64F;
    }

    public static DenseMatrix64F randn(int numRows, int numColumns, double desvpad) {
        DenseMatrix64F denseMatrix64F = new DenseMatrix64F(numRows, numColumns);
        Random random = new Random();
        for (int i = 0; i < denseMatrix64F.numRows; i++) {
            for (int j = 0; j < denseMatrix64F.numCols; j++) {
                denseMatrix64F.set(i, j, random.nextGaussian()*desvpad);
            }
        }
        return denseMatrix64F;
    }

    public static DenseMatrix64F randn(int numRows, int numColumns, long seed) {
        DenseMatrix64F denseMatrix64F = new DenseMatrix64F(numRows, numColumns);
        Random random = new Random(seed);
        for (int i = 0; i < denseMatrix64F.numRows; i++) {
            for (int j = 0; j < denseMatrix64F.numCols; j++) {
                denseMatrix64F.set(i, j, random.nextGaussian());
            }
        }
        return denseMatrix64F;
    }

    public static DenseMatrix64F rand(int numRows, int numColumns, long seed) {
        DenseMatrix64F denseMatrix64F = new DenseMatrix64F(numRows, numColumns);
        Random random = new Random(seed);
        for (int i = 0; i < denseMatrix64F.numRows; i++) {
            for (int j = 0; j < denseMatrix64F.numCols; j++) {
                denseMatrix64F.set(i, j, random.nextDouble());
            }
        }
        return denseMatrix64F;
    }

    public static DenseMatrix64F rand(int numRows, int numColumns) {
        DenseMatrix64F denseMatrix64F = new DenseMatrix64F(numRows, numColumns);
        Random random = new Random();
        for (int i = 0; i < denseMatrix64F.numRows; i++) {
            for (int j = 0; j < denseMatrix64F.numCols; j++) {
                denseMatrix64F.set(i, j, random.nextDouble());
            }
        }
        return denseMatrix64F;
    }

    public static DenseMatrix64F fill(int startNumber, int endNumber) {
        int size = endNumber - startNumber + 1;
        double[][] values = new double[1][size];
        for (int i = 0; i < size; i++) {
            values[0][i] = startNumber++;
        }
        return new DenseMatrix64F(values);
    }

    /**
     * Compute sin.
     *
     * @param inputSignal Data to be applied the sin [modified]
     */
    public static void sin(DenseMatrix64F inputSignal) {
        for (int i = 0; i < inputSignal.numRows; i++) {
            for (int j = 0; j < inputSignal.numCols; j++) {
                double data = inputSignal.get(i, j);
                inputSignal.set(i, j, Math.sin(data));
            }
        }
    }

    /**
     * Compute cos.
     *
     * @param inputSignal Data to be applied the cos [modified]
     */
    public static void cos(DenseMatrix64F inputSignal) {
        for (int i = 0; i < inputSignal.numRows; i++) {
            for (int j = 0; j < inputSignal.numCols; j++) {
                double data = inputSignal.get(i, j);
                inputSignal.set(i, j, Math.cos(data));
            }
        }
    }

    /**
     * Compute tan.
     *
     * @param inputSignal Data to be applied the tan [modified]
     */
    public static void tan(DenseMatrix64F inputSignal) {
        for (int i = 0; i < inputSignal.numRows; i++) {
            for (int j = 0; j < inputSignal.numCols; j++) {
                double data = inputSignal.get(i, j);
                inputSignal.set(i, j, Math.tan(data));
            }
        }
    }
}
