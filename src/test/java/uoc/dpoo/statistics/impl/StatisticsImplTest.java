package uoc.dpoo.statistics.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uoc.dpoo.exceptions.StatisticsException;
import uoc.dpoo.io.CSV;

import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class StatisticsImplTest {

    private CSV csv;


    @BeforeEach
    public void createCSV() throws Exception {
        Locale.setDefault(Locale.US);
        csv = new CSV(Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("test.csv")).toURI()).toString());
    }

    @Test
    @DisplayName("When count the number of elements then it returns the number of not missing values elements")
    public void countValuesWithNoNullValues(){
        //process counts OTHER and NUMBER values
        Assertions.assertEquals(0, new Count(csv).process(List.of()));
        Assertions.assertEquals(3, new Count(csv).process(List.of("1", "2", "3")));
        Assertions.assertEquals(2, new Count(csv).process(List.of("1", "2", "")));
        Assertions.assertEquals(3, new Count(csv).process(List.of("1", "2", "", "bad")));

        //countNumbers only counts NUMBER values, OTHER values are ommited.
        Assertions.assertEquals(3, new Count(csv).countNumbers(List.of("1","2","3")));
        Assertions.assertEquals(2, new Count(csv).countNumbers(List.of("1","2","")));
        Assertions.assertEquals(2, new Count(csv).countNumbers(List.of("1","2","", "bad")));
    }


    @Test
    @DisplayName("When Sum the number of elements then it returns the sum of all NUMBER values")
    public void whenSumThenTheResultIsTheSumOfAllValues(){
        Assertions.assertEquals(3, new Max(csv).process(List.of("1", "2", "3")));
        Assertions.assertEquals(3, new Max(csv).process(List.of("1", "-2", "3")));
        Assertions.assertEquals(-1, new Max(csv).process(List.of("-1", "-2", "-3")));
        //The OTHER values are omitted
        Assertions.assertEquals(2, new Max(csv).process(List.of("1", "2", "")));
        Assertions.assertEquals(10, new Max(csv).process(List.of("10", "Henrik", "Jim")));
        Assertions.assertEquals(Double.NaN, new Max(csv).process(List.of("", "Henrik", "Jim")));
    }

    @Test
    @DisplayName("When Min the number of elements then it returns the minimum of all NUMBER values")
    public void whenMinThenTheResultIsTheSumOfAllValues(){
        Assertions.assertEquals(1, new Min(csv).process(List.of("1", "2", "3")));
        Assertions.assertEquals(-2, new Min(csv).process(List.of("1", "-2", "3")));
        Assertions.assertEquals(-3, new Min(csv).process(List.of("-1", "-2", "-3")));
        //The OTHER values are omitted
        Assertions.assertEquals(1, new Min(csv).process(List.of("1", "2", "")));
        Assertions.assertEquals(10, new Min(csv).process(List.of("10", "Henrik", "Jim")));
        Assertions.assertEquals(Double.NaN, new Min(csv).process(List.of("", "Henrik", "Jim")));
    }

    @Test
    @DisplayName("When calculate the mean then it returns the average of all NUMBER values")
    public void whenMeanThenTheResultIsTheAverageOfAllValues(){
        Assertions.assertEquals(2, new Mean(csv).process(List.of("1", "2", "3")));
        Assertions.assertEquals(-1, new Mean(csv).process(List.of("1", "-7", "3")));
        Assertions.assertEquals(-2, new Mean(csv).process(List.of("-1", "-2", "-3")));
        //The OTHER values are omitted, the mean is done with the size of the list without no number elements
        Assertions.assertEquals(1.5, new Mean(csv).process(List.of("1", "2", "")));
        Assertions.assertEquals(10, new Mean(csv).process(List.of("10", "Henrik", "Jim")));
        //The average of no number values, is NaN
        Assertions.assertEquals(Double.NaN, new Mean(csv).process(List.of("", "Henrik", "Jim")));
    }

    @Test
    @DisplayName("When calculate the median then it returns the median of all NUMBER values")
    public void whenMedianThenTheResultIsTheMedianOfValues(){
        Assertions.assertEquals(2, new Median(csv).process(List.of("1", "2", "3")));
        Assertions.assertEquals(2, new Median(csv).process(List.of("3", "1", "2")));
        Assertions.assertEquals(2.5, new Median(csv).process(List.of("1", "2", "3", "4")));
        Assertions.assertEquals(2.5, new Median(csv).process(List.of("2", "4", "1", "3")));
        Assertions.assertEquals(-2, new Median(csv).process(List.of("-1", "-2", "-3")));
        //The OTHER values are omitted, the mean is done with the size of the list without no number elements
        Assertions.assertEquals(1.5, new Median(csv).process(List.of("1", "2", "")));
        Assertions.assertEquals(10, new Median(csv).process(List.of("10", "Henrik", "Jim")));
        //The median of no number values, is NaN
        Assertions.assertEquals(Double.NaN, new Median(csv).process(List.of("", "Henrik", "Jim")));
    }

    @Test
    @DisplayName("When calculate the quantil then it returns the expected quantil of all NUMBER values")
    public void whenQuantilThenTheResultIsTheQuantil() throws StatisticsException {
        Assertions.assertEquals(11, new Quantil(csv).process(List.of("10", "34", "12", "57"), 1));
        Assertions.assertEquals(11, new Quantil(csv).process(List.of("10", "34", "12", "57", "120"), 1));
        Assertions.assertEquals(23, new Quantil(csv).process(List.of("10", "34", "12", "57"), 2));
        Assertions.assertEquals(34, new Quantil(csv).process(List.of("10", "34", "12", "57", "120"), 2));
        Assertions.assertEquals(45.5, new Quantil(csv).process(List.of("10", "34", "12", "57"), 3));
        Assertions.assertEquals(88.5, new Quantil(csv).process(List.of("10", "34", "12", "57", "120"), 3));
        Assertions.assertEquals(34, new Quantil(csv).process(List.of("10", "34", "12"), 3));
        Assertions.assertEquals(34, new Quantil(csv).process(List.of("10", "34", "12", ""), 3));
        Assertions.assertEquals(Double.NaN, new Quantil(csv).process(List.of(""), 3));
    }

    @Test
    @DisplayName("When call std then it returns the expected standard deviation of all NUMBER values")
    public void whenStdThenTheResultIsTheStandardDeviation(){
        DecimalFormat format = new DecimalFormat("#.##");
        Assertions.assertEquals("19.08", format.format(new Std(csv).process(List.of("10", "34", "12", "57"))));
        Assertions.assertEquals("10.87", format.format(new Std(csv).process(List.of("10", "34", "12"))));
        Assertions.assertEquals(Double.NaN, new Std(csv).process(List.of("Andrew", "Jimmy")));
        Assertions.assertEquals(Double.NaN, new Std(csv).process(List.of("")));
        Assertions.assertEquals(Double.NaN, new Std(csv).process(List.of("")));
    }

    @Test
    @DisplayName("When call var then it returns the expected variance of all NUMBER values")
    public void whenVarThenTheResultIsTheVariance(){
        DecimalFormat format = new DecimalFormat("#.##");
        Assertions.assertEquals("364.19", format.format(new Variance(csv).process(List.of("10", "34", "12", "57"))));
        Assertions.assertEquals("118.22", format.format(new Variance(csv).process(List.of("10", "34", "12"))));
        Assertions.assertEquals("25", format.format(new Variance(csv).process(List.of("10","20", "Andrew"))));
        Assertions.assertEquals(Double.NaN, new Variance(csv).process(List.of("Andrew", "Jimmy")));
        Assertions.assertEquals(Double.NaN, new Variance(csv).process(List.of("")));
    }

    @Test
    @DisplayName("When call missingValues then it returns the number of missing value in the array")
    public void whenMissingValuesThenReturnTheNumberOfMissings(){
        Assertions.assertEquals(0,new MissingCount(csv).missingValuesOther(List.of("4", "23", "14")));
        Assertions.assertEquals(1, new MissingCount(csv).missingValuesOther(List.of("4", "23", "")));
        Assertions.assertEquals(0, new MissingCount(csv).missingValuesOther(List.of("4", "23", "bad"))); //Bad no tiene el formato esperado, pero no se encuentra en missingValues.
        Assertions.assertEquals(1, new MissingCount(csv).missingValuesOther(List.of("4", "23", "", "bad")));
        Assertions.assertEquals(1, new MissingCount(csv).missingValuesOther(List.of("")));

        Assertions.assertEquals(0, new MissingCount(csv).missingValuesNumber(List.of("4", "23", "14")));
        Assertions.assertEquals(1, new MissingCount(csv).missingValuesNumber(List.of("4", "23", "")));
        Assertions.assertEquals(1, new MissingCount(csv).missingValuesNumber(List.of("4", "23", "bad"))); //Bad no tiene el formato esperado, pero no se encuentra en missingValues.
        Assertions.assertEquals(2, new MissingCount(csv).missingValuesNumber(List.of("4", "23", "", "bad")));
        Assertions.assertEquals(1, new MissingCount(csv).missingValuesNumber(List.of("")));
    }
}
