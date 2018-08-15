/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.model.io;

import br.ufrn.lii.alarmprocessing.model.knowledge.AlarmEdges;
import br.ufrn.lii.alarmprocessing.model.knowledge.AlarmElement;
import br.ufrn.lii.alarmprocessing.model.knowledge.Failure;
import br.ufrn.lii.alarmprocessing.model.knowledge.KnowledgeBase;
import br.ufrn.lii.alarmprocessing.model.knowledge.Trigger;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class XMLKnowledgebaseReader {
    
    private final Logger logger = LoggerFactory.getLogger(XMLKnowledgebaseReader.class);
    
    private static final String FAILURES = "failure";
    
    public KnowledgeBase readFromFile(String path) throws ParserConfigurationException, SAXException, IOException {
        
        File fXmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        
        NodeList nList = doc.getElementsByTagName(FAILURES);
        
        for (int temp = 0; temp < nList.getLength(); temp++) {
            
            try {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    
                    Element eElement = (Element) nNode;
                    String idAttr = eElement.getAttribute("id");
                    String priorityAttr = eElement.getAttribute("priority");
                    
                    Failure failure = new Failure(Integer.parseInt(idAttr));
                    failure.setPriority(Integer.parseInt(priorityAttr));
                    failure.setMessage(getMsg(eElement));
                    failure.setTrigger(getTrigger(eElement));
                    
                    Map<Integer, AlarmElement> alarmsNodes = getAlarmsElements(eElement);
                    failure.setAlarmsElements(alarmsNodes.values());
                    failure.setAlarmEdges(getAlarmsEdges(eElement, alarmsNodes));
                    
                    knowledgeBase.addFailure(failure);
                }
            } catch (Exception ex) {
                logger.error("Algum erro ocorreu ao tentar processao o XML de knowledgeReader.", ex);
            }
            
        }
        
        return knowledgeBase;
    }
    
    private Trigger getTrigger(Element eElement) {
        Node node = eElement.getElementsByTagName("trigger").item(0);
        NodeList nList = node.getChildNodes();
        Trigger result = new Trigger();
        
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element alarmElement = (Element) nNode;
                //Element nodeAlarm = (Element) alarmElement.getElementsByTagName("alarm").item(0);
                String tag = alarmElement.getAttribute("tag");
                String type = alarmElement.getAttribute("type");
                result.addAlarm(new AlarmElement(tag, type));
            }
        }
        return result;
    }
    
    private Map<Integer, AlarmElement> getAlarmsElements(Element eElement) {
        Node node = eElement.getElementsByTagName("expected").item(0);
        NodeList nList = node.getChildNodes();
        Map<Integer, AlarmElement> alarmElements = new HashMap<>();
        
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element alarmElement = (Element) nNode;
                if (alarmElement.getNodeName().equals("alarm")) {
                    String idStr = alarmElement.getAttribute("id");
                    String tag = alarmElement.getAttribute("tag");
                    String type = alarmElement.getAttribute("type");
                    int id = Integer.parseInt(idStr);
                    alarmElements.put(id, new AlarmElement(id, tag, type));
                }
            }
        }
        return alarmElements;
        
    }
    
    private List<AlarmEdges> getAlarmsEdges(Element eElement, Map<Integer, AlarmElement> alarmsNodes) {
        Node node = eElement.getElementsByTagName("expected").item(0);
        NodeList nList = node.getChildNodes();
        List<AlarmEdges> alarmElements = new ArrayList<>();
        
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element alarmElement = (Element) nNode;
                if (alarmElement.getNodeName().equals("edge")) {
                    String sourceStr = alarmElement.getAttribute("source");
                    String targetStr = alarmElement.getAttribute("target");
                    String delay = alarmElement.getAttribute("delay");
                    int source = Integer.parseInt(sourceStr);
                    int target = Integer.parseInt(targetStr);
                    AlarmEdges edge = new AlarmEdges();
                    edge.setDelay(Integer.parseInt(delay));
                    edge.setFrom(alarmsNodes.get(source));
                    edge.setTo(alarmsNodes.get(target));
                }
            }
        }
        return alarmElements;
    }
    
    private String getMsg(Element eElement) {
        Node node = eElement.getElementsByTagName("msg").item(0);
        return node.getTextContent();
    }
    
}
