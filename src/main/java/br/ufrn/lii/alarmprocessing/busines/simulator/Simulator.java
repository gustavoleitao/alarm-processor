/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.simulator;

import br.ufrn.lii.alarmprocessing.busines.comunication.AlarmListener;
import br.ufrn.lii.alarmprocessing.busines.comunication.DiagnosticListner;
import br.ufrn.lii.alarmprocessing.busines.simulator.brcollector.SimulatorByBRCollector;
import br.ufrn.lii.alarmprocessing.busines.simulator.file.SimulatorRDAByFile;
import br.ufrn.lii.alarmprocessing.main.ClientAlarmTest;
import br.ufrn.lii.alarmprocessing.main.ClientProcessTest;
import br.ufrn.lii.alarmprocessing.main.DiagnosticClientListner;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmDefinitionSet;
import br.ufrn.lii.alarmprocessing.model.configuration.ConfigurationSet;
import br.ufrn.lii.alarmprocessing.model.io.XMLAlarmDefinitions;
import br.ufrn.lii.alarmprocessing.model.io.XMLConfigurationsReader;
import br.ufrn.lii.alarmprocessing.model.io.XMLKnowledgebaseReader;
import br.ufrn.lii.alarmprocessing.model.knowledge.KnowledgeBase;
import org.joda.time.DateTime;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Date;

/**
 * @author Gustavo
 */
public class Simulator {

    private String knowlodgeBase = "knowledgebase.xml";
    private String configuration = "configuration.xml";
    private String alarmDefinition = "alarmDefinitions.xml";
    private Date baseDateTime = DateTime.parse("2015-05-19").toDate();
    private int speed = 100;

    private AlarmListener[] alarmListeners = null;

    private DiagnosticListner[] diagListners = null;

    public Simulator withKnowlodgeBase(String path) {
        this.knowlodgeBase = path;
        return this;
    }

    public Simulator withAlarmDefinition(String path) {
        this.alarmDefinition = path;
        return this;
    }

    public Simulator withConfiguration(String path) {
        this.configuration = path;
        return this;
    }

    public Simulator withSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public Simulator withAlarmListner(AlarmListener... alarmListner) {
        this.alarmListeners = alarmListner;
        return this;
    }


    public Simulator withDiagListnetr(DiagnosticListner... diagListner) {
        this.diagListners = diagListner;
        return this;
    }

    public Simulator withBaseDateTime(Date dateTime){
        this.baseDateTime = dateTime;
        return this;
    }

    public void startSimulationByFile(String file) throws ParserConfigurationException, SAXException, IOException {
        simulate(file);
    }

    public void startAndWaitSimulationByFile(String file) throws ParserConfigurationException, SAXException, IOException, InterruptedException {
        simulate(file).waitTerminate();
    }

    private SimulatorRDAByFile simulate(String file) throws ParserConfigurationException, SAXException, IOException {
        XMLConfigurationsReader configurationsReader = new XMLConfigurationsReader();
        ConfigurationSet configSet = configurationsReader.readFromFile(configuration);

        XMLKnowledgebaseReader knowledgebaseReader = new XMLKnowledgebaseReader();
        KnowledgeBase knowledge = knowledgebaseReader.readFromFile(knowlodgeBase);

        XMLAlarmDefinitions alarmDefinitionsReader = new XMLAlarmDefinitions();
        AlarmDefinitionSet alarmsDefinition = alarmDefinitionsReader.readFromFile(alarmDefinition);

        Engine engine = new Engine(knowledge, configSet, alarmsDefinition);

        SimulatorRDAByFile simulator = new SimulatorRDAByFile(file, alarmsDefinition, configSet, new DateTime(baseDateTime));

        simulator.start();
        simulator.addProcessListener(new ClientProcessTest(engine));
        simulator.addAlarmListener(new ClientAlarmTest(engine));
        simulator.setAlarmBufferTime(100);
        simulator.setAlarmBufferSize(1);
        simulator.setSpeed(speed);


        registerAlarmsListner(simulator);
        registerDiagsListner(engine);

        engine.addDiagnosticListner(new DiagnosticClientListner());


        return simulator;
    }

    private void registerDiagsListner(Engine engine) {
        if (diagListners != null){
            for (DiagnosticListner diagListner : diagListners) {
                engine.addDiagnosticListner(diagListner);
            }
        }
    }

    private void registerAlarmsListner(SimulatorRDAByFile simulator) {
        if (alarmListeners != null) {
            for (AlarmListener alarmListener : alarmListeners) {
                simulator.addAlarmListener(alarmListener);
            }
        }
    }

    public void startSimulationByBRCollector() throws ParserConfigurationException, SAXException, IOException {

        XMLConfigurationsReader configurationsReader = new XMLConfigurationsReader();
        ConfigurationSet configSet = configurationsReader.readFromFile("configuration.xml");

        XMLKnowledgebaseReader knowledgebaseReader = new XMLKnowledgebaseReader();
        KnowledgeBase knowledge = knowledgebaseReader.readFromFile("knowledgebase.xml");

        XMLAlarmDefinitions alarmDefinitionsReader = new XMLAlarmDefinitions();
        AlarmDefinitionSet alarmsDefinition = alarmDefinitionsReader.readFromFile("alarmDefinitions.xml");

        Engine engine = new Engine(knowledge, configSet, alarmsDefinition);

        SimulatorByBRCollector simulatorByBRCollector = new SimulatorByBRCollector("et04", 1234, "Dados", "DA", alarmsDefinition, configSet);
        simulatorByBRCollector.start();

        simulatorByBRCollector.addProcessListener(new ClientProcessTest(engine));
        simulatorByBRCollector.addAlarmListener(new ClientAlarmTest(engine));
        simulatorByBRCollector.setAlarmBufferTime(100);
        simulatorByBRCollector.setAlarmBufferSize(1);

        engine.addDiagnosticListner(new DiagnosticClientListner());
    }

}
