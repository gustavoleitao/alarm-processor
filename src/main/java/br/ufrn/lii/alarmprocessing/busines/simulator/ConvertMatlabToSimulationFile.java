package br.ufrn.lii.alarmprocessing.busines.simulator;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Gustavo on 25/08/2016.
 */
public class ConvertMatlabToSimulationFile {

    public void transformFile(String[] varsName, String inputFile, String outputFile) throws FileNotFoundException, IOException {
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String sCurrentLine;
            int line = 0;
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile), charset)) {
                while ((sCurrentLine = br.readLine()) != null) {
                    String data = transformLine(varsName, ++line, sCurrentLine);
                    writer.append(data, 0, data.length());
                }
            }

        }
    }


    private String transformLine(String[] varsName, int line, String sCurrentLine) {
        String[] columns = sCurrentLine.split(",");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            stringBuilder.append(line).append(";").append(varsName[i]).append(";").append(columns[i]).append("\n");
        }
        return stringBuilder.toString();
    }

}
