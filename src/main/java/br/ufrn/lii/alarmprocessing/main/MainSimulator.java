/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.main;

import br.ufrn.lii.alarmprocessing.busines.FileAlarmListner;
import br.ufrn.lii.alarmprocessing.busines.FileDiffAlarmListner;
import br.ufrn.lii.alarmprocessing.busines.simulator.Simulator;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author Gustavo
 */
public class MainSimulator {

    public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, SAXException {

        for (int i = 0; i < 28; i++) {
            int idv = i + 1;
            System.out.println("Iniciando simulação..." + idv);
            Simulator simulator = new Simulator()
                    .withAlarmDefinition("defs/alarm-definition-simout-idv0.xml")
                    .withConfiguration("configs/config-simout.xml")
                    .withKnowlodgeBase("kb/knowledgebase.xml")
                    .withAlarmListner(new FileAlarmListner("result/alarm-idv" + idv + ".txt"),
                            new FileDiffAlarmListner("result/alarm-diff-idv" + idv + ".txt"))
                    .withSpeed(300);
            simulator.startAndWaitSimulationByFile("cenarios/cenario-simout-idv" + idv + ".csv");
            System.out.println("Fim da simulação..." + idv);
        }


    }

}
