package br.ufrn.lii.alarmprocessing.busines;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Gustavo on 18/09/2016.
 */
public class JaccardIndexTest extends TestCase {

    public void testSimilaritySameSet() throws Exception {
        JaccardIndex<String> stringJaccardIndex = new JaccardIndex<>();
        double similarity = stringJaccardIndex.similarity(getSet1(), getSet1());
        Assert.assertEquals("Deve ser igual", 1d, similarity);
    }

    public void testSimilaritySet1() throws Exception {
        JaccardIndex<String> stringJaccardIndex = new JaccardIndex<>();
        double similarity = stringJaccardIndex.similarity(getSet1(), getSet2());
        Assert.assertEquals("Deve ser igual", 0.75d, similarity);
    }

    public void testDistance() throws Exception {
        JaccardIndex<String> stringJaccardIndex = new JaccardIndex<>();
        double similarity = stringJaccardIndex.distance(getSet1(), getSet2());
        Assert.assertEquals("Deve ser igual", 0.25d, similarity);
    }

    private Set<String> getSet2() {
        Set<String> abc = new TreeSet<>();
        abc.add("A");
        abc.add("B");
        abc.add("C");
        abc.add("F");
        return abc;
    }

    private Set<String> getSet1() {
        Set<String> abc = new TreeSet<>();
        abc.add("A");
        abc.add("B");
        abc.add("C");
        return abc;
    }


}