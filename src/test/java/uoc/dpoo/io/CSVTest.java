package uoc.dpoo.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uoc.dpoo.exceptions.CSVException;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;

public class CSVTest {

    String file;

    @BeforeEach
    public void createFile() throws Exception {
        file = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("test.csv")).toURI()).toString();
    }

    @Test
    @DisplayName("When create a CSV with path,then separator is ';' and missing values '' and  ' '")
    public void createCSVWithPath() throws Exception {
        CSV csv = new CSV(file);
        Assertions.assertEquals(';', csv.getSep());
        Assertions.assertEquals(List.of("", " "), csv.getMissingValues());
    }

    @Test
    @DisplayName("When create a CSV with path and sep, the separator is ',' and missing values '' and  ' '")
    public void createCSVWithPathAndSep() throws Exception {
        file = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("testWithComma.csv")).toURI()).toString();;
        CSV csv = new CSV(file, ',');
        Assertions.assertEquals(',', csv.getSep());
        Assertions.assertEquals(file, csv.getPath());
        Assertions.assertEquals(List.of("", " "), csv.getMissingValues());
    }

    @Test
    @DisplayName("When create a CSV with path, sep and missing values, the separator is ',' and missing values 'missing' and  'othe'")
    public void createCSVWithPathAndMissingValues() throws Exception {
        CSV csv = new CSV(file, ';', "missing", "other");
        Assertions.assertEquals(';', csv.getSep());
        Assertions.assertEquals(file, csv.getPath());
        Assertions.assertEquals(List.of("missing", "other"), csv.getMissingValues());
    }

    @Test
    @DisplayName("When get columns names then the result is count, sum")
    public void checkColumnsName() throws Exception {
        CSV csv = new CSV(file);
        Assertions.assertEquals(List.of("count", "sum"), csv.getColumnsNames());
    }

    @Test
    @DisplayName("When get featuresByName then the result a map with name as key and Feature as value")
    public void checkFeaturesByName() throws Exception {
        CSV csv = new CSV(file);
        Map<String, Feature> expected = new HashMap<>();
        Feature count = new Feature("count",List.of("1", "2", "3") );
        Feature sum = new Feature("sum", List.of("1", "2", "4"));
        expected.put("count", count);
        expected.put("sum", sum);
        Assertions.assertEquals(expected, csv.getFeatures());
    }


    @Test
    @DisplayName("Given no missing values when GetValuesWithoutMissing then all values are returned")
    public void givenNoMissingValuesWhenGetValuesWithoutMissingThenAllValuesAreReturned() throws Exception {
        CSV csv = new CSV(file);
        String[] test = {"1", "2", "3"};
        Assertions.assertEquals(List.of("1", "2", "3"),csv.getValuesWithoutMissing(test));
    }

    @Test
    @DisplayName("Given missing values when GetValuesWithoutMissing then all no missing values are returned")
    public void givenMissingValuesWhenGetValuesWithoutMissingThenAllNoMissingValuesAreReturned() throws Exception {
        CSV csv = new CSV(file);
        String[] test = {"1", "2", ""};
        List<String> expected = new ArrayList<>();
        expected.add("1");
        expected.add("2");
        expected.add(null);
        Assertions.assertEquals(expected,csv.getValuesWithoutMissing(test));
    }

    @Test
    @DisplayName("Given empty values when GetValuesWithoutMissing then empty values are returned")
    public void givenEmptyValuesWhenGetValuesWithoutMissingThenEmptyValuesAreReturned() throws Exception {
        CSV csv = new CSV(file);
        String[] test = {null};
        List<String> expected = new ArrayList<>();
        expected.add(null);
        Assertions.assertEquals(expected,csv.getValuesWithoutMissing(test));
        Assertions.assertEquals(List.of(),csv.getValuesWithoutMissing(new String[]{}));
    }

    @Test
    @DisplayName("Given a valid number row then a List is returned with the values")
    public void givenARowThenTheValuesAreReturned() throws Exception {
        CSV csv = new CSV(file);
        Map<String,String> actual = csv.getRow(1);
        Map<String,String> expected = new HashMap<>();
        expected.put("count", "2");
        expected.put("sum", "2");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Given a row that is less than 0 then an exception must be raised")
    public void givenRowLessThan0ThenExceptionIsRaised() throws Exception {
        CSV csv = new CSV(file);
        CSVException thrown = Assertions.assertThrows(CSVException.class, () -> csv.getRow(-1));
        Assertions.assertEquals(thrown.getMessage(),"[ERROR] Row -1 out of bounds");
    }

    @Test
    @DisplayName("Given a row equals to the total number of rows then an exception must be raised")
    public void givenRowEqualsToTotalRowsThenExceptionIsRaised() throws Exception {
        CSV csv = new CSV(file);
        CSVException thrown = Assertions.assertThrows(CSVException.class, () -> csv.getRow(csv.getRowsNumber()));
        Assertions.assertEquals(thrown.getMessage(),"[ERROR] Row 3 out of bounds");
    }

    @Test
    @DisplayName("Given a row higher than the total number of rows then an exception must be raised")
    public void givenRowHigherThanTotalRowsThenExceptionIsRaised() throws Exception {
        CSV csv = new CSV(file);
        CSVException thrown = Assertions.assertThrows(CSVException.class, () -> csv.getRow(csv.getRowsNumber()+1));
        Assertions.assertEquals(thrown.getMessage(),"[ERROR] Row 4 out of bounds");
    }

    @Test
    @DisplayName("Given a name of non existing feature then an exception is raised")
    public void givenAFeatureNameNotInCSVThenAnExceptionIsRaised() throws Exception {
        CSV csv = new CSV(file);
        CSVException thrown = Assertions.assertThrows(CSVException.class, () -> csv.getFeature("test"));
        Assertions.assertEquals(thrown.getMessage(),"[ERROR] Feature test does not exists in CSV");
    }

    @Test
    @DisplayName("Given a Feature name, the feature returned has the correct name, values and type")
    public void givenAFeatureNameInCSVThenReturnsTheFeature() throws Exception {
        CSV csv = new CSV(file);
        Feature actual =  csv.getFeature("count");
        Feature expected = new Feature("count", List.of("1", "2", "3"));
        Assertions.assertEquals(expected, actual);
    }
}
