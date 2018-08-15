package br.ufrn.lii.alarmprocessing.busines.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Leitor de arquivos do tipo Csv.
 */
public class CsvReader<T> {

    private final Logger logger = LoggerFactory.getLogger(CsvReader.class);

    private FromStringFunction<T> fromStringFunction;

    public CsvReader(FromStringFunction<T> fromStringFunction) {
        this.fromStringFunction = fromStringFunction;
    }

    public List<T> loadFile(String filePath) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String sCurrentLine;
            List<T> signalDatas = new ArrayList<>();
            while ((sCurrentLine = br.readLine()) != null) {
                signalDatas.add(fromStringFunction.apply(sCurrentLine));
            }
            return signalDatas;
        }
    }

}
