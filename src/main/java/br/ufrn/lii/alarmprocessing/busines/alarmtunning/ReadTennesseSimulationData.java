package br.ufrn.lii.alarmprocessing.busines.alarmtunning;

import br.ufrn.lii.alarmprocessing.busines.util.CsvReader;
import br.ufrn.lii.alarmprocessing.busines.util.FromStringFunction;

import java.io.IOException;
import java.util.List;

/**
 * Faz a leitura dos dados de saida da simulação do Tennesse.
 */
public class ReadTennesseSimulationData {

    public List<Double[]> read(String filePath) throws IOException {
        return read(filePath, ",");
    }

    public List<Double[]> read(String filePath, String separator) throws IOException {
        FromStringFunction<Double[]> doubleFromStringFunction = getTransformFunction(separator);
        CsvReader<Double[]> csvReader = new CsvReader<>(doubleFromStringFunction);
        return csvReader.loadFile(filePath);
    }

    private FromStringFunction<Double[]> getTransformFunction(String separator) {
        return s -> {
            String[] stringData = s.split(separator);
            Double[] dData = new Double[stringData.length];

            for (int i = 0; i < stringData.length; i++) {
                dData[i] = Double.valueOf(stringData[i]);
            }

            return dData;
        };
    }

}
