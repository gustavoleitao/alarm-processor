/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.simulator;

import br.ufrn.lii.alarmprocessing.model.exception.InvalidFormatException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gustavo
 */
public class ConvertMatlabToAProcessing {

    private static final String SEMICOLON = ";";

    private DateTime baseDate;
    private String datePattern;

    private final static Logger LOGGER = LoggerFactory.getLogger(ConvertMatlabToAProcessing.class);

    public ConvertMatlabToAProcessing(DateTime baseDate) {
        this(baseDate, "dd/MM/yyyy HH:mm:ss");
    }

    public ConvertMatlabToAProcessing(DateTime baseDate, String datePattern) {
        this.baseDate = baseDate;
        this.datePattern = datePattern;
    }

    public void convert(String inputFile, String outputFile) throws IOException {
        loadFile(inputFile, outputFile);
    }

    private void loadFile(String filePath, String fileOut) throws FileNotFoundException, IOException {

        FileWriter fileWriter = new FileWriter(fileOut);

        try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    try {
                        String[] line = processLine(sCurrentLine);
                        bufferedWriter.write(gelLine(line));
                    } catch (InvalidFormatException ex) {
                        LOGGER.warn("A seguinte linha foi ignorada: [{}]", sCurrentLine);
                    }
                }
            }

        }
    }

    private String[] processLine(String line) throws InvalidFormatException {
        String[] columns = line.split(SEMICOLON);
        if (columns.length >= 3) {
            return columns;
        } else {
            throw new InvalidFormatException("Linha inválida. O número de colunas deverá ser no mínimo 3 [data;tag;valor].");
        }
    }

    public static void main(String[] args) throws IOException {
        ConvertMatlabToAProcessing aProcessing = new ConvertMatlabToAProcessing(DateTime.now());
        aProcessing.convert("cenario1.csv", "cenario-processado.csv");
    }

    private String gelLine(String[] line) {
        int diffTime = Integer.parseInt(line[0]);
        StringBuilder newLine = new StringBuilder();
        newLine.append(baseDate.plusSeconds(diffTime).toString(datePattern)).append(SEMICOLON);
        newLine.append(line[2]).append(SEMICOLON);
        newLine.append(line[1]).append(SEMICOLON).append("\n");
        return newLine.toString();

    }

}
