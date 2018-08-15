package br.ufrn.lii.alarmprocessing.busines;

import br.ufrn.lii.alarmprocessing.busines.comunication.DiagnosticListner;
import br.ufrn.lii.alarmprocessing.busines.util.FileUtil;
import br.ufrn.lii.alarmprocessing.model.domain.Diagnostic;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gustavoleitao on 05/08/2018.
 */
public class FileDiagListner implements DiagnosticListner {

    private String basePath;

    private Map<Integer,Double> lastIndex = new HashMap();

    private Date startTime;

    private DecimalFormat formatter = new DecimalFormat("#.###");

    private DiagFilter filter = new DiagFilter(0.001);

    public FileDiagListner(Date startTime, String basePath) {
        this.basePath = basePath;
        this.startTime = startTime;
    }

    @Override
    public void onDiagnostics(Collection<Diagnostic> diags) {

        if (filter.isRelevant(diags)){
            addToFile(diags);
        }
    }

    private void addToFile(Collection<Diagnostic> diags) {
        for (Diagnostic diag: diags){
            FileUtil.tentaEscrever(getDiagFile(diag), getMsg(diag));
        }
    }

    private StringBuilder getMsg(Diagnostic diag) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(getTime(diag))
                .append(";").append(formatter.format(diag.getOcurrence()))
                .append(";").append(formatter.format(diag.getCoverage()))
                .append(";").append(formatter.format(diag.getOcurrence() * diag.getCoverage())).append("\n");
        return strBuilder;
    }

    private long getTime(Diagnostic diag) {
        return (diag.getTimestamp().getTime()-startTime.getTime()) / 1000;
    }

    private String getDiagFile(Diagnostic diag) {
        return basePath + System.getProperty("file.separator") + "f-" + diag.getFailureID() + ".csv";
    }

}
