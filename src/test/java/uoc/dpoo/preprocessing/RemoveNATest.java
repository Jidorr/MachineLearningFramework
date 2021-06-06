package uoc.dpoo.preprocessing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uoc.dpoo.io.CSV;
import uoc.dpoo.io.Feature;
import uoc.dpoo.preprocessing.impl.RemoveMissingData;

import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public class RemoveNATest {

    private static CSV csv;
    private static String mainPath;

    @BeforeAll
    static void init() {
        try {
            mainPath = Paths.get(Objects.requireNonNull(RemoveNATest.class.getClassLoader().getResource("titanic.csv")).toURI()).toString();
            System.out.println("CSV LOADED");

        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST INIT FAILED: Check exception trace");
        }
    }

    @Test
    public void whenTestingForRemovedNA_ShouldCoince_1(){
        try {
            csv = new CSV(mainPath, ',');
            CSV resultCSV = new RemoveMissingData(csv).process();

            Assertions.assertEquals(resultCSV.getFeatures().keySet(), csv.getFeatures().keySet()); //Test consistency

            Assertions.assertEquals(resultCSV.getColumnsNames(), csv.getColumnsNames());

            Assertions.assertEquals(resultCSV.getRowsNumber(), 712); //Check only first column

            for (Feature feature : resultCSV.getFeatures().values()) { //Check every column
                Assertions.assertEquals(feature.getValues().size(), 712);
            }

            Assertions.assertEquals(resultCSV.getFeature("Name").getValues().get(100), "Ekstrom Mr. Johan");
            Assertions.assertEquals(resultCSV.getFeature("Name").getValues().get(200), "Carter Rev. Ernest Courtenay");
            Assertions.assertEquals(resultCSV.getFeature("Name").getValues().get(300), "Widener Mr. Harry Elkins");
            Assertions.assertEquals(resultCSV.getFeature("Name").getValues().get(resultCSV.getRowsNumber()-1), "Dooley Mr. Patrick");


        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 1 FAILED: Check exception trace");
        }
    }

    @Test
    public void whenTestingForRemoveNAWithNotusedNAMark_ShouldCoincide(){
        try {
            csv = new CSV(mainPath, ',', "~");
            CSV resultCSV = new RemoveMissingData(csv).process();

            Assertions.assertEquals(resultCSV.getFeatures().keySet(), csv.getFeatures().keySet()); //Test consistency
            Assertions.assertEquals(resultCSV.getColumnsNames(), csv.getColumnsNames());

            Assertions.assertEquals(resultCSV.getRowsNumber(), csv.getRowsNumber()); //Check only first column

            for (Feature feature : resultCSV.getFeatures().values()) { //Check every column
                Assertions.assertEquals(feature.getValues().size(), csv.getRowsNumber());
            }

            Assertions.assertEquals(resultCSV.getFeature("Name").getValues().get(resultCSV.getRowsNumber()-1),
                    csv.getFeature("Name").getValues().get(resultCSV.getRowsNumber()-1));

        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 2 FAILED: Check exception trace");
        }
    }


}
