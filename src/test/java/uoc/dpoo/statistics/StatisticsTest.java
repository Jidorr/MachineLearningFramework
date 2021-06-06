package uoc.dpoo.statistics;

import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uoc.dpoo.io.CSV;
import uoc.dpoo.preprocessing.DiscretizeTest;
import uoc.dpoo.statistics.impl.Count;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StatisticsTest {

    private CSV csv;

    @BeforeEach
    void createCSV() throws Exception {
        csv = new CSV(Paths.get(Objects.requireNonNull(DiscretizeTest.class.getClassLoader().getResource("test.csv")).toURI()).toString());
    }

    @Test
    @DisplayName("When call isMissingValue with default missing values then '' and ' ' are marked as missing values")
    public void isMissingValueWithDefaultValues(){
        Assertions.assertTrue(new Count(csv).isMissingValue(""));
        Assertions.assertTrue(new Count(csv).isMissingValue(" "));
        Assertions.assertFalse(new Count(csv).isMissingValue("null"));
        Assertions.assertFalse(new Count(csv).isMissingValue("None"));
    }

    @Test
    @DisplayName("When call isMissingValue with values then all values are marked as missing values")
    public void isMissingValueWithMoreValues() throws Exception {
        csv = new CSV(Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("test.csv")).toURI()).toString(),
                ',', "", " ", "null", "None");
        Assertions.assertTrue(new Count(csv).isMissingValue(""));
        Assertions.assertTrue(new Count(csv).isMissingValue(" "));
        Assertions.assertTrue(new Count(csv).isMissingValue("null"));
        Assertions.assertTrue(new Count(csv).isMissingValue("None"));
        Assertions.assertFalse(new Count(csv).isMissingValue("test"));
    }

    @Test
    @DisplayName("When call isMissingValue with no missing values, no value is marked as missing value")
    public void isMissingValueWithNoValues() throws Exception {
        csv = new CSV(Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("test.csv")).toURI()).toString(),
                ',', new String[]{});
        Assertions.assertFalse(new Count(csv).isMissingValue(""));
        Assertions.assertFalse(new Count(csv).isMissingValue(" "));
        Assertions.assertFalse(new Count(csv).isMissingValue("null"));
        Assertions.assertFalse(new Count(csv).isMissingValue("None"));
        Assertions.assertFalse(new Count(csv).isMissingValue("test"));
    }

    @Test
    @DisplayName("When call isNotmissingValue with default missing values then only '' and ' ' are marked as false")
    public void isNotMissingValueWithDefaultValues(){
        Assertions.assertFalse(new Count(csv).isNotMissingValue(""));
        Assertions.assertFalse(new Count(csv).isNotMissingValue(" "));
        Assertions.assertTrue(new Count(csv).isNotMissingValue("null"));
        Assertions.assertTrue(new Count(csv).isNotMissingValue("None"));
    }

    @Test
    @DisplayName("When call isNotmissingValue with user missing values then all values are marked as false")
    public void isNotMissingValueWithMoreValues() throws Exception {
        csv = new CSV(Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("test.csv")).toURI()).toString(),
                ',', "", " ", "null", "None");
        Assertions.assertFalse(new Count(csv).isNotMissingValue(""));
        Assertions.assertFalse(new Count(csv).isNotMissingValue(" "));
        Assertions.assertFalse(new Count(csv).isNotMissingValue("null"));
        Assertions.assertFalse(new Count(csv).isNotMissingValue("None"));
        Assertions.assertTrue(new Count(csv).isNotMissingValue("test"));
    }

    @Test
    @DisplayName("When call isNotmissingValue with no missing values then all results are truee")
    public void isNotMissingValueWithNoValues() throws Exception {
        csv = new CSV(Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("test.csv")).toURI()).toString(),
                ',', new String[]{});
        Assertions.assertTrue(new Count(csv).isNotMissingValue(""));
        Assertions.assertTrue(new Count(csv).isNotMissingValue(" "));
        Assertions.assertTrue(new Count(csv).isNotMissingValue("null"));
        Assertions.assertTrue(new Count(csv).isNotMissingValue("None"));
        Assertions.assertTrue(new Count(csv).isNotMissingValue("test"));
    }

    @Test
    @DisplayName("When call convertToDouble on emptyList and emptyList is returned")
    public void convertToDoubleAnEmptyList(){
        Assertions.assertEquals(List.of(), new Count(csv).convertToDouble(List.of()).boxed().collect(Collectors.toList()));
    }

    @Test
    @DisplayName("When call convertToDouble with a list of strings when all values are numbers, a list of the same size with doubles is returned")
    public void convertToDoubleWithNumbers(){
        Assertions.assertEquals(List.of(1d,2d,3d), new Count(csv).convertToDouble(List.of("1", "2", "3")).boxed().collect(Collectors.toList()));
    }

    @Test
    @DisplayName("When call convertToDouble with a list of strings when values are numbers and chars then a list with only the numbers converted to double is returned")
    public void convertToDoubleWithNumbersAndLetters(){
        Assertions.assertEquals(List.of(1d,2d), new Count(csv).convertToDouble(List.of("1", "2", "a")).boxed().collect(Collectors.toList()));
        Assertions.assertEquals(List.of(3d), new Count(csv).convertToDouble(List.of("a", "b", "3")).boxed().collect(Collectors.toList()));
        Assertions.assertEquals(List.of(), new Count(csv).convertToDouble(List.of("a", "b", "c")).boxed().collect(Collectors.toList()));
    }

    @Test
    @DisplayName("When call convertToDouble with a list of strings when values are numbers and chars and nulls then a list with only the numbers converted to double is returned")
    public void convertToDoubleWithNumbesAndLettersAndNulls(){
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add(null);
        Assertions.assertEquals(List.of(1d), new Count(csv).convertToDouble(list).boxed().collect(Collectors.toList()));

        list.add("c");
        Assertions.assertEquals(List.of(1d), new Count(csv).convertToDouble(list).boxed().collect(Collectors.toList()));
    }

}
