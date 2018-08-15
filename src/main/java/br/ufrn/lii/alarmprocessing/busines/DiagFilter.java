package br.ufrn.lii.alarmprocessing.busines;

import br.ufrn.lii.alarmprocessing.model.domain.Diagnostic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gustavoleitao on 05/08/2018.
 */
public class DiagFilter {

    private Map<Integer,Double> lastIndex = new HashMap();

    public double tolerance = 0.1;

    public DiagFilter(double tolerance) {
        this.tolerance = tolerance;
    }

    public boolean almostDiferent(double a, double b, double eps){
        return Math.abs(a - b) > eps;
    }

    public boolean isRelevant(Collection<Diagnostic> diags) {

        boolean relevant = false;

        if (significativeChange(diags) && !diags.isEmpty()){
            relevant = true;
            updateMap(diags);
        }

        return relevant;

    }

    private void updateMap(Collection<Diagnostic> diags) {
        for (Diagnostic diagnostic : diags) {
            double atualS = diagnostic.getCoverage() * diagnostic.getOcurrence();
            lastIndex.put(diagnostic.getFailureID(), atualS);
        }
    }

    private boolean significativeChange(Collection<Diagnostic> diags) {

        boolean significativeChange = false;

        if (lastIndex.isEmpty()){
            significativeChange = true;
        }else{

            for (Diagnostic diag : diags) {
                Double atualS = diag.getCoverage() * diag.getOcurrence();
                Double lastS = lastIndex.get(diag.getFailureID());
                if (lastS != null){
                    if (significativeChange(atualS, lastS)){
                        significativeChange = true;
                        break;
                    }
                }else{
                    significativeChange = true;
                }
            }
        }

        return significativeChange;
    }

    private boolean significativeChange(double s1, double s2) {
        return almostDiferent(s1, s2, 0.1);
    }

}
