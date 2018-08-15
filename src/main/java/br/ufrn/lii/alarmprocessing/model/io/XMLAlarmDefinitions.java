/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.io;

import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmDefinition;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmDefinitionSet;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmNode;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmTypes;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Gustavo Leitão
 */
public class XMLAlarmDefinitions {

    public static final String ALARMS = "alarm";

    public void toXML(AlarmDefinitionSet alarmDefinitionSet, String path) throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root element
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("alarms");
        doc.appendChild(rootElement);

        Set<String> tags = alarmDefinitionSet.getTags();
        for (String tag : tags) {
            Element alarm = doc.createElement("alarm");
            alarm.setAttribute("tag", tag);
            rootElement.appendChild(alarm);

            List<AlarmDefinition> definitions = alarmDefinitionSet.getDefinitionsByTag(tag);

            for (AlarmDefinition definition : definitions) {
                Element alarmType = doc.createElement(definition.getAlarmTypes().toString().toLowerCase());
                alarmType.setAttribute("priority", String.valueOf(definition.getPriority()));
                alarmType.setAttribute("deadband", String.valueOf(definition.getDeadband()));
                alarmType.appendChild(doc.createTextNode(String.valueOf(definition.getThreshold())));
                alarm.appendChild(alarmType);
            }

        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(new File(path));
        transformer.transform(source, result);

    }

    public AlarmDefinitionSet readFromFile(String path) throws ParserConfigurationException, SAXException, IOException {

        File fXmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();

        AlarmDefinitionSet alarmDefinitionSet = new AlarmDefinitionSet();

        NodeList nList = doc.getElementsByTagName(ALARMS);

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                String tagName = eElement.getAttribute("tag");
                AlarmDefinition highValue = getHighValue(tagName, eElement);
                AlarmDefinition hihiValue = getHihiValue(tagName, eElement);
                AlarmDefinition lowValue = getLowValue(tagName, eElement);
                AlarmDefinition loloValue = getLoloValue(tagName, eElement);

                if (highValue != null) {
                    alarmDefinitionSet.add(highValue);
                }
                if (hihiValue != null) {
                    alarmDefinitionSet.add(hihiValue);
                }
                if (lowValue != null) {
                    alarmDefinitionSet.add(lowValue);
                }
                if (loloValue != null) {
                    alarmDefinitionSet.add(loloValue);
                }

            }
        }

        return alarmDefinitionSet;

    }

    private AlarmDefinition getHighValue(String tag, Element eElement) {
        try {
            AlarmNode alarmNode = getAlarmNodeValue(eElement, "high");
            return new AlarmDefinition(alarmNode.getThreshold(), AlarmTypes.HIGH, tag, alarmNode.getPriority(), alarmNode.getDeadband());
        } catch (Exception ex) {
            return null;
        }
    }

    private AlarmDefinition getHihiValue(String tag, Element eElement) {
        try {
            AlarmNode alarmNode = getAlarmNodeValue(eElement, "hihi");
            return new AlarmDefinition(alarmNode.getThreshold(), AlarmTypes.HIHI, tag, alarmNode.getPriority(), alarmNode.getDeadband());
        } catch (Exception ex) {
            return null;
        }
    }

    private AlarmDefinition getLowValue(String tag, Element eElement) {
        try {
            AlarmNode alarmNode = getAlarmNodeValue(eElement, "low");
            return new AlarmDefinition(alarmNode.getThreshold(), AlarmTypes.LOW, tag, alarmNode.getPriority(), alarmNode.getDeadband());
        } catch (Exception ex) {
            return null;
        }
    }

    private AlarmDefinition getLoloValue(String tag, Element eElement) {
        try {
            AlarmNode alarmNode = getAlarmNodeValue(eElement, "lolo");
            return new AlarmDefinition(alarmNode.getThreshold(), AlarmTypes.LOLO, tag, alarmNode.getPriority(), alarmNode.getDeadband());
        } catch (Exception ex) {
            return null;
        }
    }

    private int getPriorityValue(Node node) throws NumberFormatException, DOMException {
        String priorityStr = node.getAttributes().getNamedItem("priority").getNodeValue();
        int priority = Integer.parseInt(priorityStr);
        return priority;
    }

    private double getDeadbandValue(Node node) throws NumberFormatException, DOMException {
        double deadband = 0d;
        Node atribute = node.getAttributes().getNamedItem("deadband");
        if (atribute != null) {
            deadband = Double.parseDouble(atribute.getNodeValue());
        }
        return deadband;
    }

    private AlarmNode getAlarmNodeValue(Element eElement, String key) {
        Node node = eElement.getElementsByTagName(key).item(0);
        String hihi = node.getTextContent();
        int priority = getPriorityValue(node);
        double deadband = getDeadbandValue(node);
        double threshold = Double.valueOf(hihi);
        return new AlarmNode(priority, threshold, deadband);
    }

}
