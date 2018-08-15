/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.simulator.file;

import br.ufrn.lii.alarmprocessing.model.domain.SignalData;
import br.ufrn.lii.alarmprocessing.model.exception.InvalidFormatException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gustavo
 */
public class LoadSimFile {

    private final Logger logger = LoggerFactory.getLogger(LoadSimFile.class);
    private static final String SEMICOLON = ";";
    private DateTime baseTime;

    public LoadSimFile() {
        this(DateTime.now());
    }

    public LoadSimFile(DateTime baseTime) {
        this.baseTime = baseTime;
    }

    public List<SignalData> loadFile(String filePath) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String sCurrentLine;
            List<SignalData> signalDatas = new ArrayList<>();
            while ((sCurrentLine = br.readLine()) != null) {
                try {
                    signalDatas.add(processLine(sCurrentLine));
                } catch (InvalidFormatException ex) {
                    logger.warn("A seguinte linha foi ignorada: [{}]", sCurrentLine);
                }
            }
            return signalDatas;
        }
    }

    private SignalData processLine(String line) throws InvalidFormatException {
        String[] columns = line.split(SEMICOLON);
        if (columns.length >= 3) {
            try {
                Date timestamp = toDate(columns[0]);
                String tag = toTag(columns[1]);
                Double value = toValue(columns[2]);
                SignalData data = new SignalData(timestamp, tag, value);
                return data;
            } catch (ParseException ex) {
                throw new InvalidFormatException("Linha inválida. Formato de data inválido.");
            } catch (NumberFormatException ex) {
                throw new InvalidFormatException("Linha inválida. Formato do valor inválido.");
            }
        } else {
            throw new InvalidFormatException("Linha inválida. O número de colunas deverá ser no mínimo 3 [data;tag;valor].");
        }

    }

    private Date toDate(String column) throws ParseException {
        int relTime = Integer.parseInt(column);
        return baseTime.plusSeconds(relTime).toDate();
    }

    private Double toValue(String column) {
        return Double.valueOf(column.replaceAll(",", "."));
    }

    private String toTag(String column) {
        return column.trim();
    }
}
