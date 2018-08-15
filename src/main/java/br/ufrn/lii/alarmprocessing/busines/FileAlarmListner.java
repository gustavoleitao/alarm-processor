package br.ufrn.lii.alarmprocessing.busines;

import br.ufrn.lii.alarmprocessing.busines.comunication.AlarmListener;
import br.ufrn.lii.alarmprocessing.busines.util.FileUtil;
import br.ufrn.lii.alarmprocessing.model.domain.Alarm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

/**
 * Created by Gustavo on 31/08/2016.
 */
public class FileAlarmListner implements AlarmListener {

    private String filePath;

    public FileAlarmListner(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void onEvent(Collection<Alarm> alarmList) {
        if (!alarmList.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Alarm alarm : alarmList) {
                stringBuilder.append(alarm).append("\n");
            }
            FileUtil.tentaEscrever(filePath, stringBuilder);
        }
    }



}
