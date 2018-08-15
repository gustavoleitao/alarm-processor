package br.ufrn.lii.alarmprocessing.busines.alarmtunning;

import br.ufrn.lii.alarmprocessing.busines.util.CsvWriter;
import br.ufrn.lii.alarmprocessing.model.domain.MinMax;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Gustavo on 27/06/2016.
 */
public class StatisticsTenesseSimulation {

    public void processarDesvios(TuningMethod method, List<String> input) throws IOException {
        StatisticsTenesseSimulation statisticsTenesseSimulation = new StatisticsTenesseSimulation();

        List<List<MinMax>> lines = statisticsTenesseSimulation.processarSimulacoesConjunto(
                input, method, 0);

        List<MinMax> listMinMax = lines.get(0);

        DecimalFormat df = new DecimalFormat("#0.000");

        for (MinMax minMax : listMinMax) {
            System.out.print(df.format(minMax.getMin()) + ";");
        }

        System.out.println();

        for (MinMax minMax : listMinMax) {
            System.out.print(df.format(minMax.getMax()) + ";");
        }



    }

    public void processarCasoBase(String input, String outputMin, String outputMax, TuningMethod method) throws IOException {
        StatisticsTenesseSimulation statisticsTenesseSimulation = new StatisticsTenesseSimulation();

        List<List<MinMax>> lines = statisticsTenesseSimulation.processarSimulacoes(
                Arrays.asList(input), method, 0);

        CsvWriter csvMinWriter = new CsvWriter(new MinToString());
        CsvWriter csvMaxWriter = new CsvWriter(new MaxToString());

        csvMinWriter.writeFile(outputMin, lines);
        csvMaxWriter.writeFile(outputMax, lines);

    }

    public List<List<MinMax>> processarSimulacoes(List<String> files, TuningMethod method, int startPoint) throws IOException {
        ReadTennesseSimulationData readTennesseSimulationData = new ReadTennesseSimulationData();
        List<List<MinMax>> simulacoes = new ArrayList<>();
        for (String file : files) {
            simulacoes.add(method.alarmSet(readTennesseSimulationData.read(file), startPoint));
        }
        return simulacoes;
    }

    public List<List<MinMax>> processarSimulacoesConjunto(List<String> files, TuningMethod method, int startPoint) throws IOException {
        ReadTennesseSimulationData readTennesseSimulationData = new ReadTennesseSimulationData();
        List<List<MinMax>> simulacoes = new ArrayList<>();

        List<Double[]> dados = new ArrayList<>();
        for (String file : files) {
            dados.addAll(readTennesseSimulationData.read(file));
        }

        simulacoes.add(method.alarmSet(dados, startPoint));
        return simulacoes;
    }

}
