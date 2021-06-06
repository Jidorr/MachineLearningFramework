package uoc.dpoo.metrics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uoc.dpoo.trainTest.Pair;

import java.util.List;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;

public class MetricsTest {

    @Test
    public void testAccuracyWithValue1(){
        List<Pair> Pairs = List.of(new Pair("1", "1"),
                new Pair("1", "1"),
                new Pair("0", "0"),
                new Pair("0", "0"));
        Metrics metrics = new Metrics(Pairs);
        Assertions.assertEquals(metrics.accuracy(), 1);
    }

    @Test
    public void testAccuracyWithValue0(){
        List<Pair> Pairs = List.of(new Pair("1", "0"),
                new Pair("1", "0"),
                new Pair("0", "1"),
                new Pair("0", "1"));
        Metrics metrics = new Metrics(Pairs);
        Assertions.assertEquals(metrics.accuracy(), 0);
    }

    @Test
    public void testRecallWithValue1(){
        List<Pair> Pairs = List.of(new Pair("1", "1"),
                new Pair("1", "1"),
                new Pair("0", "0"),
                new Pair("0", "0"));
        Metrics metrics = new Metrics(Pairs);
        Assertions.assertEquals(metrics.recall(), 1);
    }

    @Test
    public void testRecallWithValue0(){
        List<Pair> Pairs = List.of(new Pair("1", "0"),
                new Pair("1", "0"),
                new Pair("0", "1"),
                new Pair("0", "1"));
        Metrics metrics = new Metrics(Pairs);
        Assertions.assertEquals(metrics.recall(), 0);
    }

    @Test
    public void testPrecisionWithValue1(){
        List<Pair> Pairs = List.of(new Pair("1", "1"),
                new Pair("1", "1"),
                new Pair("0", "0"),
                new Pair("0", "0"));
        Metrics metrics = new Metrics(Pairs);
        Assertions.assertEquals(metrics.precision(), 1);
    }

    @Test
    public void testPrecisionWithValue0(){
        List<Pair> Pairs = List.of(new Pair("1", "0"),
                new Pair("1", "0"),
                new Pair("0", "1"),
                new Pair("0", "1"));
        Metrics metrics = new Metrics(Pairs);
        Assertions.assertEquals(metrics.precision(), 0);
    }

    @Test
    public void testF1WithValue1(){
        List<Pair> Pairs = List.of(new Pair("1", "1"),
                new Pair("1", "1"),
                new Pair("0", "0"),
                new Pair("0", "0"));
        Metrics metrics = new Metrics(Pairs);
        Assertions.assertEquals(metrics.f1(), 1);
    }

    @Test
    public void testF1WithValue0(){
        List<Pair> Pairs = List.of(new Pair("1", "0"),
                new Pair("1", "0"),
                new Pair("0", "1"),
                new Pair("0", "1"));
        Metrics metrics = new Metrics(Pairs);
        Assertions.assertEquals(metrics.f1(), 0);
    }

    @Test
    public void testConfusionMatrix() throws Exception {
        List<Pair> Pairs = List.of(new Pair("1", "0"),
                new Pair("1", "1"),
                new Pair("0", "1"),
                new Pair("0", "1"));
        Metrics metrics = new Metrics(Pairs);
        String actual = tapSystemOut(metrics::confusionMatrix);
        String expected =  " |0\t\t1" + System.lineSeparator() +
                "-----------" + System.lineSeparator() +
                "0|0\t\t1" +  System.lineSeparator() +
                "1|2\t\t1" + System.lineSeparator();
        Assertions.assertEquals(expected, actual);
    }
}
