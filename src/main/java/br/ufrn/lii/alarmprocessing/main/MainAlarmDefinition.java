package br.ufrn.lii.alarmprocessing.main;

import br.ufrn.lii.alarmprocessing.busines.alarmtunning.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Gustavo on 22/08/2016.
 */
public class MainAlarmDefinition {

    public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException {
        //processarCasoBase(new MinMaxMethod());
        //processarCasoBase(new SigmaMethod());
        processarDesvio(new SigmaMethod());
    }

    private static void processarDesvio(TuningMethod method) throws IOException, TransformerException, ParserConfigurationException {
        StatisticsTenesseSimulation statisticsTenesseSimulation = new StatisticsTenesseSimulation();

        List<String> files = new ArrayList<>();

        for (int i=0;i<28;i++){
            files.add("simulacoes/simout-idv" + (i+1) +".csv");
        }

        statisticsTenesseSimulation.processarDesvios(method, files);

    }

    private static void processarCasoBase(TuningMethod method) throws IOException, TransformerException, ParserConfigurationException {
        StatisticsTenesseSimulation statisticsTenesseSimulation = new StatisticsTenesseSimulation();
        AlarmDefinitionWritter alarmDefinitionWritter = new AlarmDefinitionWritter();

        double deadband = 0.05;

        statisticsTenesseSimulation.processarCasoBase("simulacoes/xmv-idv0.csv",
                "output/min-xmv-"+method.name()+"-idv0.csv", "output/max-xmv-"+method.name()+"-idv0.csv", method);

        statisticsTenesseSimulation.processarCasoBase("simulacoes/simout-idv0.csv",
                "output/min-simout-"+method.name()+"-idv0.csv", "output/max-simout-"+method.name()+"-idv0.csv", method);

        alarmDefinitionWritter.toDefinitionFile("output/min-simout-"+method.name()+"-idv0.csv", "output/max-simout-"+method.name()+"-idv0.csv",
                "defs/alarm-definition-simout-"+method.name()+"-idv0.xml", "simout", 0.005, deadband);

        alarmDefinitionWritter.toDefinitionFile("output/min-xmv-"+method.name()+"-idv0.csv", "output/max-xmv-"+method.name()+"-idv0.csv",
                "defs/alarm-definition-xmv-"+method.name()+"-idv0.xml", "xmv", 0.005, deadband);


    }




}
