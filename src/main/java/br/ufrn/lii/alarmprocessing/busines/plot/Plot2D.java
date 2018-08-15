/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.plot;

import br.ufrn.lii.alarmprocessing.model.domain.Signal;
import br.ufrn.lii.alarmprocessing.model.domain.SignalData;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import org.ejml.data.MatrixIterator;
import org.ejml.simple.SimpleMatrix;
import org.math.plot.Plot2DPanel;

/**
 *
 * @author Gustavo
 */
public class Plot2D {

    private Plot2DPanel plot = new Plot2DPanel();
    private String chartName;

    public Plot2D(String chartName) {
        this.chartName = chartName;
    }

    public void addLineSerie(String name, Signal signal) {
        plot.addLinePlot(name, toDoubleArray(signal));
    }
    
    public void addScatterSerie(String name, Signal signal){
        plot.addScatterPlot(name, toDoubleArray(signal));
    }
    
    public void addLineSerie(String name, SimpleMatrix matrix) throws InvalidMatrixException {
        if (matrix.isVector()) {
            plot.addLinePlot(name, toDoubleArray(matrix));
        } else {
            throw new InvalidMatrixException("Para plotar a matriz deverá possuir apenas uma linha/coluna.");
        }
    }

    public void addLineSerie(String name, SimpleMatrix matrixX, Color color) throws InvalidMatrixException {
        if (matrixX.isVector()) {
            plot.addLinePlot(name, color, toDoubleArray(matrixX));
        } else {
            throw new InvalidMatrixException("Para plotar a matriz deverá possuir apenas uma linha/coluna.");
        }
    }

    public void addSerie(String name, SimpleMatrix matrixX, SimpleMatrix matrixY) throws InvalidMatrixException {
        if (matrixX.isVector() && matrixY.isVector()) {
            plot.addLinePlot(name, toDoubleArray(matrixX), toDoubleArray(matrixY));
        } else {
            throw new InvalidMatrixException("Para plotar a matriz deverá possuir apenas uma linha/coluna.");
        }
    }

    public void addSerie(String name, SimpleMatrix matrixX, SimpleMatrix matrixY, Color color) throws InvalidMatrixException {
        if (matrixX.isVector() && matrixY.isVector()) {
            if (matrixX.getNumElements() == matrixY.getNumElements()) {
                plot.addLinePlot(name, color, toDoubleArray(matrixX), toDoubleArray(matrixY));
            } else {
                throw new InvalidMatrixException("Tamanho inválido. Os vetores deverão possuir a mesma dimensão.");
            }
        } else {
            throw new InvalidMatrixException("Para plotar a matriz deverá possuir apenas uma linha/coluna.");
        }
    }

    public void plot() {
        JFrame frame = new JFrame(chartName);
        frame.setContentPane(plot);
        frame.setPreferredSize(new Dimension(450, 450));
        frame.setMinimumSize(new Dimension(450, 450));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private static double[] toDoubleArray(SimpleMatrix matrix) {
        SimpleMatrix vector = matrix.extractVector(true, 0);
        MatrixIterator iterator = vector.iterator(true, 0, 0, 0, vector.getNumElements() - 1);
        double output[] = new double[vector.getNumElements()];
        int index = 0;
        while (iterator.hasNext()) {
            output[index++] = iterator.next().doubleValue();
        }
        return output;
    }

    private static double[][] toDoubleArray(Signal signal) {
        int size = signal.getSize();
        double output[][] = new double[size][2];
        for (int i = 0; i < size; i++) {
            SignalData data = signal.get(i);
            output[i][0] = (double) data.getDate().getTime();
            output[i][1] = data.getValue();
        }
        return output;
    }
}
