package uoc.dpoo.preprocessing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uoc.dpoo.io.CSV;
import uoc.dpoo.preprocessing.impl.SelectFeatures;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

public class FeatureSelectionTest {

    private static CSV csv;

    @BeforeAll
    static void init() {
        try {
            String mainPath = Paths.get(Objects.requireNonNull(FeatureSelectionTest.class.getClassLoader().getResource("titanic.csv")).toURI()).toString();
            csv = new CSV(mainPath, ',');
            System.out.println("CSV LOADED");

        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST INIT FAILED: Check exception trace");
        }
    }

    @Test
    public void whenTestingForSelectedFeatures_ShouldBeEqual_1(){
        try {
            String[] selectedFeatures = new String[]{"Survived", "Pclass", "Sex", "Age", "SibSp", "Parch", "Fare", "Cabin"}; //1,2,4,5,6,7,9,10)
            CSV resultCSV = new SelectFeatures(csv).process(selectedFeatures);

            Assertions.assertEquals(resultCSV.getColumnsNames(), List.of(selectedFeatures).stream().sorted().collect(Collectors.toList()));
            Assertions.assertNotSame(resultCSV.getColumnsNames(), List.of(selectedFeatures));

            Assertions.assertEquals(resultCSV.getFeatures().keySet(), new HashSet<>(Arrays.asList(selectedFeatures)));
        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 1 FAILED: Check exception trace");
        }
    }

    @Test
    public void whenTestingForSelectedFeatures_ShouldBeEqual_2(){
        try {
            String[] selectedFeatures = new String[]{"Survived", "Pclass", "Sex", "Age"};
            CSV resultCSV = new SelectFeatures(csv).process(selectedFeatures);

            Assertions.assertEquals(resultCSV.getColumnsNames(), List.of(selectedFeatures).stream().sorted().collect(Collectors.toList()));
            Assertions.assertNotSame(resultCSV.getColumnsNames(), List.of(selectedFeatures));

            Assertions.assertEquals(resultCSV.getFeatures().keySet(), new HashSet<>(Arrays.asList(selectedFeatures)));
        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 2 FAILED: Check exception trace");
        }
    }

    @Test
    public void whenTestingForSelectedFeatures_ShouldBeEqual_3(){
        try {
            String[] selectedFeatures = new String[]{"Survived"};
            CSV resultCSV = new SelectFeatures(csv).process(selectedFeatures);

            Assertions.assertEquals(resultCSV.getColumnsNames(), List.of(selectedFeatures));
            Assertions.assertNotSame(resultCSV.getColumnsNames(), List.of(selectedFeatures));

            Assertions.assertEquals(resultCSV.getFeatures().keySet(), new HashSet<>(Arrays.asList(selectedFeatures)));
        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 3 FAILED: Check exception trace");
        }
    }
}
