package br.ufrn.lii.alarmprocessing.main;

import br.ufrn.lii.alarmprocessing.busines.simulator.ConvertMatlabToSimulationFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Gustavo on 25/08/2016.
 */
public class MainTransformMatlabSim {


    public static void main(String[] args) throws IOException {
        ConvertMatlabToSimulationFile convertMatlabToSimulationFile = new ConvertMatlabToSimulationFile();
        System.out.println("Iniciando conversão");

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 6, 7, 8, 10, 11);

        for (Integer i : list) {
            System.out.println("Convertendo cenário " + i);
            convertMatlabToSimulationFile.transformFile(getHeader("simout-", 41),
                    "sim-failures/failures-alternativas/estacionarias/simout-failure" + i + ".csv", "cenarios/cenarios-alternativos/estacionarias/cenario-simout-failure" + i + ".csv");
        }

    }

    private static String[] getHeader(String simout, int qnt) {
        String[] headers = new String[qnt];
        for (int i = 0; i < qnt; i++) {
            StringBuilder builder = new StringBuilder(simout);
            if (i < 10) {
                builder.append("0");
            }
            builder.append(i + 1);
            headers[i] = builder.toString();
        }
        return headers;
    }

}
