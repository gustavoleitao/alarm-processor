package br.ufrn.lii.alarmprocessing.busines.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gustavo on 04/07/2016.
 */
public class CsvWriter<T> {

    private final Logger logger = LoggerFactory.getLogger(CsvReader.class);

    private ToStringFunction<T> fromStringFunction;

    public CsvWriter(ToStringFunction<T> toStringFunction) {
        this.fromStringFunction = toStringFunction;
    }

    public void writeFile(String filePath, List<T> lines) throws IOException {
        Path file = Paths.get(filePath);
        List<String> linesStr = new ArrayList<>();
        for (T line : lines) {
            linesStr.add(fromStringFunction.apply(line));
        }
        Files.write(file, linesStr, Charset.forName("UTF-8"));
    }

}
