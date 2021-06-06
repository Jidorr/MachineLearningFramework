package uoc.dpoo.preprocessing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uoc.dpoo.common.Util;
import uoc.dpoo.io.CSV;
import uoc.dpoo.preprocessing.impl.NormalizeMINMAX;

import java.nio.file.Paths;
import java.util.Locale;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public class NormalizationTest {

    private static CSV csv;
    private static String mainPath;

    @BeforeAll
    static void init() {
        try {
            mainPath = Paths.get(Objects.requireNonNull(NormalizationTest.class.getClassLoader().getResource("titanic.csv")).toURI()).toString();
            System.out.println("CSV LOADED");
            Locale.setDefault(new Locale("en", "US"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST INIT FAILED: Check exception trace");
        }
    }

    @Test
    public void whenTestingForNormalization_ShouldCoince_1(){
        try {
            String columnToNormalize = "Fare";
            csv = new CSV(mainPath, ',');
            CSV resultCSV = new NormalizeMINMAX(csv).process(columnToNormalize);

            Assertions.assertEquals(resultCSV.getFeatures().keySet(), csv.getFeatures().keySet()); //Test consistency
            Assertions.assertEquals(resultCSV.getColumnsNames(), csv.getColumnsNames());

            double sum = Util.convertToDouble(resultCSV.getFeature(columnToNormalize).getValues()).sum();

            Assertions.assertEquals(Util.convertToDouble(resultCSV.getFeature(columnToNormalize).getValues()).min().getAsDouble(), 0);
            Assertions.assertEquals(Util.convertToDouble(resultCSV.getFeature(columnToNormalize).getValues()).max().getAsDouble(), 1);
            Assertions.assertEquals(sum, 56.73, 0.9);

            Assertions.assertEquals(Double.parseDouble(resultCSV.getFeature(columnToNormalize).getValues().get(50)), 0.02, 0.1);
            Assertions.assertEquals(Double.parseDouble(resultCSV.getFeature(columnToNormalize).getValues().get(200)), 0.02, 0.1);
            Assertions.assertEquals(Double.parseDouble(resultCSV.getFeature(columnToNormalize).getValues().get(600)), 0.05, 0.1);
            Assertions.assertEquals(Double.parseDouble(resultCSV.getFeature(columnToNormalize).getValues().get(700)), 0.44, 0.1);
            Assertions.assertEquals(Double.parseDouble(resultCSV.getFeature(columnToNormalize).getValues().get(resultCSV.getRowsNumber()-1)), 0.02, 0.1);

        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 1 FAILED: Check exception trace");
        }
    }

}
