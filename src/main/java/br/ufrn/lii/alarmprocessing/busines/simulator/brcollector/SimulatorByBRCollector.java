package br.ufrn.lii.alarmprocessing.busines.simulator.brcollector;

import br.ufrn.lii.alarmprocessing.busines.comunication.AlarmListener;
import br.ufrn.lii.alarmprocessing.busines.comunication.ProcessListener;
import br.ufrn.lii.alarmprocessing.busines.simulator.AlarmProcessor;
import br.ufrn.lii.alarmprocessing.busines.simulator.file.ProcessorSimulator;
import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmDefinitionSet;
import br.ufrn.lii.alarmprocessing.model.configuration.Configuration;
import br.ufrn.lii.alarmprocessing.model.configuration.ConfigurationSet;
import br.ufrn.lii.alarmprocessing.model.domain.SignalData;
import br.ufrn.lii.alarmprocessing.model.exception.BRCollectorConnectionException;
import br.ufrn.lii.brcollector.connection.ConnectionProvider;
import br.ufrn.lii.brcollector.connection.rmi.Request;
import br.ufrn.lii.brcollector.connection.rmi.RequestRDA;
import br.ufrn.lii.commonsdomain.ProcessVariableSubscription;
import br.ufrn.lii.commonsdomain.process.TagItem;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gustavo Leitão
 */
public class SimulatorByBRCollector implements Runnable {

    private final String hostServer;
    private final int porta;
    private final String idServer;
    private final String nameServer;
    private final ConfigurationSet configuration;

    private final AlarmProcessor alarmProcessor;
    private final List<ProcessListener> processListners = new ArrayList<ProcessListener>();

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SimulatorByBRCollector.class);

    public SimulatorByBRCollector(String hostServer, int porta, String idServer, String nameServer,
            AlarmDefinitionSet alarmDefinition, ConfigurationSet configuration) {
        this.hostServer = hostServer;
        this.porta = porta;
        this.idServer = idServer;
        this.nameServer = nameServer;
        this.configuration = configuration;        
        alarmProcessor = new AlarmProcessor(alarmDefinition, configuration);
    }

    public void start() throws IOException {
        Thread simThread = new Thread(this);
        simThread.start();
    }

    @Override
    public void run() {
        RequestRDA request = getPersistentRequestRDA();
        Collection<Configuration> processConfig = configuration.getAlarmConfiguration();
        ProcessVariableSubscription sub;
        try {
            request.connect();
            sub = request.createSubscription("subAlarmProcessor", true, 1000, 0, new BRCollectorCallBack(this));
            for (Configuration configuration1 : processConfig) {
                request.addItemsToSubscription(sub, getTagItemByName(configuration1.getProcessTag()));
            }
        } catch (RemoteException ex) {
            logger.error("Falha ao se comunicar com o BR-Collector");
        }

    }

    private RequestRDA getPersistentRequestRDA() {
        while (true) {
            try {
                RequestRDA request = getRequestRDA();
                return request;
            } catch (BRCollectorConnectionException ex) {
                logger.info("Alguma falha ocorreu ao tentar se comunicar com o BR-Collector. O sistema tentará novamente em 5 segundos.");
                dormir(5000);
            }
        }
    }

    private void dormir(int miliis) {
        try {
            Thread.sleep(miliis);
        } catch (InterruptedException ex1) {
            Logger.getLogger(SimulatorByBRCollector.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    private RequestRDA getRequestRDA() throws BRCollectorConnectionException {
        try {
            Request provider = ConnectionProvider.connectToCollector(hostServer, porta, idServer);
            RequestRDA requestRDA = provider.getRequestRDA(nameServer);
            return requestRDA;
        } catch (NotBoundException ex) {
            logger.error("Falha ao iniciar a comunicaçção com o BR-Collector");
            throw new BRCollectorConnectionException("Falha ao se comunicar com o BR-Collector. Servidor não registrado.", ex);
        } catch (MalformedURLException ex) {
            throw new BRCollectorConnectionException("Falha ao se comunicar com o BR-Collector. URL mnal formada.", ex);
        } catch (RemoteException ex) {
            throw new BRCollectorConnectionException("Falha ao se comunicar com o BR-Collector. Servidor indisponível.", ex);
        }
    }

    private TagItem getTagItemByName(String idTag) {
        TagItem tagItem = new TagItem();
        tagItem.setIdStr(idTag);
        tagItem.setName(idTag);
        return tagItem;
    }

    public void addProcessListener(ProcessListener listener) {
        processListners.add(listener);
    }

    public void addAlarmListener(AlarmListener listener) {
        alarmProcessor.addAlarmListener(listener);
    }

    void fireEvents(Collection<SignalData> signalData) {
        fireAlarmsListners(signalData);
        fileProcessListners(signalData);
    }

    private void fireAlarmsListners(Collection<SignalData> signalData) {
        for (SignalData signalData1 : signalData) {
            alarmProcessor.processAlarm(signalData1);
        }
    }

    private void fileProcessListners(Collection<SignalData> signalData) {
        for (ProcessListener processListener : processListners) {
            processListener.onData(signalData);
        }
    }

    public void setAlarmBufferTime(int alarmBufferTime) {
        alarmProcessor.setAlarmBufferTime(alarmBufferTime);
    }

    public void setAlarmBufferSize(int alarmBufferSize) {
        alarmProcessor.setAlarmBufferSize(alarmBufferSize);
    }

    public int getAlarmBufferTime(int alarmBufferTime) {
        return alarmProcessor.getAlarmBufferTime();
    }

    public int getAlarmBufferSize(int alarmBufferSize) {
        return alarmProcessor.getAlarmBufferSize();
    }

}
