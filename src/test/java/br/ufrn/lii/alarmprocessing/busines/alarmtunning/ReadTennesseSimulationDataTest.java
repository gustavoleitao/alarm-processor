package br.ufrn.lii.alarmprocessing.busines.alarmtunning;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by Gustavo on 27/06/2016.
 */
public class ReadTennesseSimulationDataTest extends TestCase {

    public void testRead() throws Exception {
        ReadTennesseSimulationData readTennesseSimulationData = new ReadTennesseSimulationData();
        List<Double[]> dados = readTennesseSimulationData.read("src\\test\\data\\csvreadertest.csv");
        Assert.assertNotNull(dados);
        Assert.assertEquals(2, dados.size());
        Assert.assertEquals(0.11, dados.get(0)[0]);
        Assert.assertEquals(0.13, dados.get(0)[2]);
        Assert.assertEquals(0.21, dados.get(1)[0]);
        Assert.assertEquals(0.23, dados.get(1)[2]);
    }

}