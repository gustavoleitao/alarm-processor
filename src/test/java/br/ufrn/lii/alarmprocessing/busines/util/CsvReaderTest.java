package br.ufrn.lii.alarmprocessing.busines.util;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by Gustavo on 27/06/2016.
 */
public class CsvReaderTest extends TestCase {

    public void testLoadFile() throws Exception {

        FromStringFunction<String[]> function = (t -> t.split(","));

        CsvReader csvReader = new CsvReader(function);
        List<String[]> data = csvReader.loadFile("src\\test\\data\\csvreadertest.csv");

        Assert.assertNotNull(data);
        Assert.assertEquals("Deve ter dois elementos, ", 2, data.size());
        Assert.assertEquals("Deve ter três elementos na primeira linha", 3, data.get(0).length);
        Assert.assertEquals("Deve ter três elementos na segunda linha", 3, data.get(1).length);
    }
}