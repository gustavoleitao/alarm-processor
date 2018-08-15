package br.ufrn.lii.alarmprocessing.busines.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by gustavoleitao on 04/08/2018.
 */
public class FileUtil {

    public static void tentaEscrever(String filePath, StringBuilder stringBuilder) {
        try {
            if (Files.exists(Paths.get(filePath))) {
                Files.write(Paths.get(filePath), stringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
            } else {
                Files.createDirectories(Paths.get(filePath).getParent());
                Files.write(Paths.get(filePath), stringBuilder.toString().getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
