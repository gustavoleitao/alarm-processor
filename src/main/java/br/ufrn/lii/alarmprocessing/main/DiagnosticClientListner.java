/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lii.alarmprocessing.main;

import br.ufrn.lii.alarmprocessing.busines.DiagFilter;
import br.ufrn.lii.alarmprocessing.busines.comunication.DiagnosticListner;
import br.ufrn.lii.alarmprocessing.model.domain.Diagnostic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gustavo
 */
public class DiagnosticClientListner implements DiagnosticListner {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    private DecimalFormat format = new DecimalFormat("#.###");
    private DiagFilter filter = new DiagFilter(0.1);


    @Override
    public void onDiagnostics(Collection<Diagnostic> diags) {

        if (filter.isRelevant(diags)){
            imprimirDiag(diags);
        }

    }

    private void imprimirDiag(Collection<Diagnostic> diags) {

        for (Diagnostic diagnostic : diags) {

            StringBuilder builder = new StringBuilder();
            String msg = diagnostic.getMensagem();
            double s = diagnostic.getOcurrence() * diagnostic.getCoverage();
            builder.append(dateFormat.format(diagnostic.getTimestamp())).append(";")
                    .append(msg).append(";")
                    .append(format.format(diagnostic.getOcurrence())).append(";")
                    .append(format.format(diagnostic.getCoverage())).append(";")
                    .append(format.format(s));

            System.out.println(builder.toString());

        }
    }



}
