/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.io;

import br.ufrn.lii.alarmprocessing.model.configuration.Configuration;
import br.ufrn.lii.alarmprocessing.model.configuration.ConfigurationSet;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Gustavo
 */
public class XMLConfigurationsReader {

    private final Logger logger = LoggerFactory.getLogger(XMLConfigurationsReader.class);

    public ConfigurationSet readFromFile(String path) throws ParserConfigurationException, SAXException, IOException {

        File fXmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();

        ConfigurationSet alarmDefinitionSet = new ConfigurationSet();
        alarmDefinitionSet.setDefaultConfiguration(getDefaultConfiguration(doc));

        NodeList nList = doc.getElementsByTagName("configuration");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            try {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    String alarmTag = eElement.getAttribute("alarmtag");
                    String processTag = eElement.getAttribute("processtag");
                    Double filterSignalValue = Double.valueOf(eElement.getAttribute("filterSignalValue"));
                    Double filterSpeedValue = Double.valueOf(eElement.getAttribute("filterSpeedValue"));
                    String typePrediction = eElement.getAttribute("typePrediction");

                    Configuration configuration = new Configuration(alarmTag, processTag, filterSignalValue, filterSpeedValue);
                    Configuration.PredictionType predictionType = Configuration.PredictionType.valueOf(typePrediction.toUpperCase());
                    if (predictionType != null) {
                        configuration.setPredictionType(predictionType);
                    }
                    configuration.setPredictionType(predictionType);
                    alarmDefinitionSet.addConfiguration(configuration);

                }
            } catch (Exception ex) {
                logger.error("Algum erro ocorreu ao tentar processao o XML de configurações.", ex);
            }

        }

        return alarmDefinitionSet;

    }

    private Configuration getDefaultConfiguration(Document doc) throws NumberFormatException {
        NodeList nListDefault = doc.getElementsByTagName("default");
        Node nNodeDefault = nListDefault.item(0);
        Element eElementDefault = (Element) nNodeDefault;
        Double filterSignalValueDefault = Double.valueOf(eElementDefault.getAttribute("filterSignalValue"));
        Double filterSpeedValueDefault = Double.valueOf(eElementDefault.getAttribute("filterSpeedValue"));
        String typePredictionDefault = eElementDefault.getAttribute("typePrediction");
        Configuration config = new Configuration("default", "default", filterSignalValueDefault, filterSpeedValueDefault);
        config.setPredictionType(Configuration.PredictionType.valueOf(typePredictionDefault.toUpperCase()));
        return config;
    }

}
