package br.ufrn.lii.alarmprocessing.main;

import br.ufrn.lii.alarmprocessing.busines.FileAlarmListner;
import br.ufrn.lii.alarmprocessing.busines.FileDiagListner;
import br.ufrn.lii.alarmprocessing.busines.FileDiffAlarmListner;
import br.ufrn.lii.alarmprocessing.busines.simulator.Simulator;
import org.joda.time.DateTime;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by gustavoleitao on 03/08/2018.
 */
public class MainTep {

    private static String basePath;

    public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, SAXException {

        String folder = "estacionarias";

        String base = getResultBasePath(folder);

        List<Integer> list = Arrays.asList( 1, 2, 3, 4, 6, 7, 8, 10, 11);

        //List<Integer> list = Arrays.asList(1);

        Date baseDateTime = DateTime.parse("2015-05-19").toDate();

        for (Integer idv : list) {
            System.out.println("Iniciando simulação..." + idv);
            Simulator simulator = new Simulator()
                    .withAlarmDefinition("defs/alarm-def-tep-min-max.xml")
                    .withConfiguration("configs/config-simout.xml")
                    .withKnowlodgeBase("kb/knowledgebase-tep.xml")
                    .withAlarmListner(new FileAlarmListner(base + "/alarm-failure" + idv + ".txt"),
                            new FileDiffAlarmListner(base + "/alarm-diff-failure" + idv + ".txt"))
                    .withDiagListnetr(new FileDiagListner(baseDateTime, base + "/failure-" + idv + "/"))
                    .withBaseDateTime(baseDateTime)
                    .withSpeed(600);
            simulator.startAndWaitSimulationByFile("cenarios/"+folder+"/cenario-simout-failure" + idv + ".csv");
            System.out.println("Fim da simulação..." + idv);
        }

    }

    public static String getResultBasePath(String folder) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
        return "result/" + folder + "/" + formatter.format(date);
    }
}
