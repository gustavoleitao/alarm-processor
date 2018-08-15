/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.busines.simulator;

import br.ufrn.lii.alarmprocessing.busines.comunication.DiagnosticListner;
import br.ufrn.lii.alarmprocessing.busines.core.PredictException;
import br.ufrn.lii.alarmprocessing.model.domain.Alarm;
import br.ufrn.lii.alarmprocessing.model.domain.SignalData;
import br.ufrn.lii.alarmprocessing.model.configuration.ConfigurationSet;
import br.ufrn.lii.alarmprocessing.model.knowledge.KnowledgeBase;
import br.ufrn.lii.alarmprocessing.busines.core.Processor;
import br.ufrn.lii.alarmprocessing.busines.core.ProcessorFactory;
import br.ufrn.lii.alarmprocessing.model.domain.Diagnostic;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmDefinition;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmDefinitionSet;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmTypes;
import br.ufrn.lii.alarmprocessing.model.configuration.Configuration;
import br.ufrn.lii.alarmprocessing.model.knowledge.AlarmElement;
import br.ufrn.lii.alarmprocessing.model.knowledge.Failure;
import com.logique.util.properties.PropertyUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gustavo
 */
public class Engine {

    private static final Logger logger = LoggerFactory.getLogger(Engine.class);

    private final int MAX_TIME_TO_ZERO = PropertyUtil.getNumericProperty("engine.max_time_to_zero_seconds", 30 * 10);
    private final Set<Alarm> allAlarmData = Collections.synchronizedSet(new HashSet<Alarm>());
    private final Map<String, Processor> dataProcessors = Collections.synchronizedMap(new HashMap<String, Processor>());
    private final Map<Integer, Diagnostic> activeDiags = Collections.synchronizedMap(new HashMap<Integer, Diagnostic>());
    private final Collection<DiagnosticListner> diagsListner = Collections.synchronizedList(new ArrayList<DiagnosticListner>());

    private final KnowledgeBase knowledgeBase;
    private final ConfigurationSet configurationSet;
    private final AlarmDefinitionSet alarmDefinitionSet;

    public Engine(KnowledgeBase knowledgeBase, ConfigurationSet configurationSet, AlarmDefinitionSet alarmDefinitionSet) {
        this.knowledgeBase = knowledgeBase;
        this.configurationSet = configurationSet;
        this.alarmDefinitionSet = alarmDefinitionSet;
    }

    public void addDiagnosticListner(DiagnosticListner diagnosticListner) {
        diagsListner.add(diagnosticListner);
    }

    public void addData(SignalData data) {
        Processor localBuffer = dataProcessors.get(data.getTag());
        Configuration config = configurationSet.getConfigurationByProcessTag(data.getTag());
        if (config == null) {
            config = configurationSet.getDefaultConfiguration();
        }
        if (localBuffer == null) {
            localBuffer = ProcessorFactory.createProcessor(config);
        }
        localBuffer.addData(data.getDate(), data.getTag(), data.getValue());
        dataProcessors.put(data.getTag(), localBuffer);
        processNewData(data);
    }

    public void addAlarm(Alarm alarm) {
        if (alarm.getState().equals(Alarm.AlarmState.ACTIVE)) {
            allAlarmData.add(alarm);
        } else {
            allAlarmData.remove(alarm);
        }
        processNewAlarm(alarm);
    }

    private void processNewData(SignalData data) {
        updateOcurrenceFromDiagnostics(data.getDate());
        fireDiagListner();
    }

    private void processNewAlarm(Alarm alarm) {
        processTriggers(alarm);
        updateOcurrenceFromDiagnostics(alarm.getTimestamp());
        fireDiagListner();
    }

    private void processTriggers(Alarm alarm) {
        List<Failure> failures = knowledgeBase.getFailureTriggeedByAlarm(new AlarmElement(alarm.getTag(), alarm.getType().getTypeStr()));
        if (failures != null) {
            for (Failure failure : failures) {
                if (failure != null && alarm.getState().equals(Alarm.AlarmState.ACTIVE)) {
                    Diagnostic diag = new Diagnostic(failure.getId(), failure.getMessage(), failure.getPriority(), 0, alarm.getTimestamp());
                    activeDiags.put(failure.getId(), diag);
                }
            }
        }
    }

    private boolean isAlarmActiveOrMoreRelevant(AlarmElement alarmElement) {
        AlarmTypes aType = AlarmTypes.valueOf(alarmElement.getType());

        boolean result;
        switch (aType) {
            case HIGH:
                result = allAlarmData.contains(new Alarm(alarmElement.getTag(), AlarmTypes.HIGH));
                if (result) {
                    break;
                }
            case HIHI:
                result = allAlarmData.contains(new Alarm(alarmElement.getTag(), AlarmTypes.HIHI));
                break;

            case LOW:
                result = allAlarmData.contains(new Alarm(alarmElement.getTag(), AlarmTypes.LOW));
                if (result) {
                    break;
                }
            case LOLO:
                result = allAlarmData.contains(new Alarm(alarmElement.getTag(), AlarmTypes.LOLO));
                break;
            default:
                result = Boolean.FALSE;
        }

        return result;
    }

    private void updateOcurrenceFromDiagnostics(Date timestamp) {
        for (Map.Entry<Integer, Diagnostic> entry : activeDiags.entrySet()) {
            Integer failureId = entry.getKey();
            Diagnostic diagnostic = entry.getValue();
            Failure failure = knowledgeBase.getFailureById(failureId);
            Collection<AlarmElement> alarmsOnFailure = failure.getAlarmsElements();
            double totalSize = alarmsOnFailure.size();
            int sumReal = 0;
            double sumEstimativa = 0d;
            for (AlarmElement alarmElement : alarmsOnFailure) {
                if (isAlarmActiveOrMoreRelevant(alarmElement)) {
                    sumReal++;
                } else {
                    sumEstimativa += calculateRelativeOcurrence(alarmElement);
                }
            }
            double coverage = sumReal / (double)allAlarmData.size();
            double ocurrence = (sumReal+sumEstimativa) / totalSize;
            diagnostic.setOcurrence(timestamp, ocurrence);
            diagnostic.setCoverage(timestamp, coverage);
        }
    }

    private double calculateRelativeOcurrence(AlarmElement alarmElement) {
        Configuration configuration = configurationSet.getConfigurationByAlarmTag(alarmElement.getTag());
        Processor processor;
        double sum = 0d;
        if (configuration == null) {
            processor = dataProcessors.get(alarmElement.getTag());
        } else {
            String processTag = configuration.getProcessTag();
            processor = dataProcessors.get(processTag);
        }
        if (processor != null && processor.isPredictionEnabled()) {
            AlarmDefinition definitions = alarmDefinitionSet.getDefinitionsByTagAndType(alarmElement.getTag(), alarmElement.getType());
            sum = processOcurrenceByProcessor(processor, definitions);
        }
        return sum;
    }

    private double processOcurrenceByProcessor(Processor processor, AlarmDefinition definitions) {
        double timeToValue = 0;
        if (processor != null) {
            try {
                long predictTime = processor.predictMillisecondsToValue(definitions.getThreshold());
                if (predictTime < 0) {
                    timeToValue = interpreteNegativeTime(definitions, processor, timeToValue);
                } else {
                    if (predictTime <= MAX_TIME_TO_ZERO * 1000) {
                        timeToValue = 1 - ((predictTime) / (double) (MAX_TIME_TO_ZERO * 1000));
                    }
                }
            } catch (PredictException ex) {
                logger.warn("Valor inalcançável. Será dado nota Zero.");
            }

        }
        return timeToValue;

    }

    private double interpreteNegativeTime(AlarmDefinition definitions, Processor processor, double timeToValue) {
        switch (definitions.getAlarmTypes()) {
            case HIHI:
            case HIGH:
                if (processor.getLastData().getValue() > definitions.getThreshold()) {
                    timeToValue = 1;
                } else {
                    timeToValue = 0;
                }
                break;
            case LOW:
            case LOLO:
                if (processor.getLastData().getValue() < definitions.getThreshold()) {
                    timeToValue = 1;
                } else {
                    timeToValue = 0;
                }
        }
        return timeToValue;
    }

    public Diagnostic ackDiagnostic(Diagnostic diagnostic) {
        return activeDiags.remove(diagnostic.getFailureID());
    }

    private void fireDiagListner() {
        for (DiagnosticListner diagnosticListner : diagsListner) {
            diagnosticListner.onDiagnostics(activeDiags.values());
        }
    }

}
