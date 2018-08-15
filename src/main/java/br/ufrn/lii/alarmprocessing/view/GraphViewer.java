package br.ufrn.lii.alarmprocessing.view;

import br.ufrn.lii.alarmprocessing.model.alarmdefinition.AlarmTypes;
import br.ufrn.lii.alarmprocessing.model.domain.Alarm;
import br.ufrn.lii.alarmprocessing.model.domain.Failure;
import br.ufrn.lii.alarmprocessing.model.domain.FailuresRelation;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.List;
import java.util.Set;

/**
 * Created by Gustavo on 12/09/2016.
 */
public class GraphViewer {

    public void toGraph(FailuresRelation failuresRelation) {
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        Graph graph = new MultiGraph("Bazinga!");
        Set<Failure> failures = failuresRelation.failures();
        for (Failure failure : failures) {
            graph.addNode(failure.getName()).setAttribute("ui.label", failure.getName());
            List<Alarm> alarms = failuresRelation.getAlarms(failure);
            for (Alarm alarm : alarms) {
                String alarmStr = toString(alarm);
                Node nodeAlm = graph.getNode(alarmStr);

                if (nodeAlm == null) {
                    Node alamNode = graph.addNode(alarmStr);
                    alamNode.setAttribute("ui.label", alarmStr);
                    alamNode.setAttribute("ui.class", "alarm");
                }
                graph.addEdge(failure.getName() + alarmStr, alarmStr, failure.getName());
            }

        }

        configureUI(graph);
        graph.setStrict(false);
        graph.setAutoCreate(true);
        graph.display();


    }

    private void configureUI(Graph graph) {

        String styleSheet =
                "node {" +

                        "   size-mode: fit;" +
                        "   shape: rounded-box;" +
                        "   fill-color: white;" +
                        "   stroke-mode: plain;" +
                        "   padding: 3px, 2px;" +
                        "   fill-color: yellow;" +

                        "}" +
                        "node.alarm {" +
                        "	fill-color: red;" +
                        "}";

        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", styleSheet);
    }

    private String toString(Alarm alarm) {
        return alarm.getTag() + " - " + alarm.getType().toString();
    }



}