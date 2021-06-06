package uoc.dpoo.preprocessing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uoc.dpoo.io.CSV;
import uoc.dpoo.preprocessing.impl.DiscretizeEqualWidth;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public class DiscretizeTest {

    private static CSV csv;
    private static String mainPath;

    @BeforeAll
    static void init() {
        try {
            mainPath = Paths.get(Objects.requireNonNull(DiscretizeTest.class.getClassLoader().getResource("titanic.csv")).toURI()).toString();
            System.out.println("CSV LOADED");
            //Locale.setDefault(new Locale("en", "US"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST INIT FAILED: Check exception trace");
        }
    }

    @Test
    public void whenTestingForDiscretization_ShouldCoince_1(){
        try {
            String columnToDiscretize = "Age";
            csv = new CSV(mainPath, ',');
            CSV resultCSV = new DiscretizeEqualWidth(csv).process(columnToDiscretize, 3);

            Assertions.assertEquals(resultCSV.getFeatures().keySet(), csv.getFeatures().keySet()); //Test consistency
            Assertions.assertEquals(resultCSV.getColumnsNames(), csv.getColumnsNames());

            Assertions.assertEquals((int) resultCSV.getFeature(columnToDiscretize).getValues().stream().filter(row -> row.equals("A")).count(), 319);
            Assertions.assertEquals((int) resultCSV.getFeature(columnToDiscretize).getValues().stream().filter(row -> row.equals("B")).count(), 345);
            Assertions.assertEquals((int) resultCSV.getFeature(columnToDiscretize).getValues().stream().filter(row -> row.equals("C")).count(), 50);
            Assertions.assertEquals((int) resultCSV.getFeature(columnToDiscretize).getValues().stream().filter(row -> row.equals("D")).count(), 0);

            Assertions.assertEquals(resultCSV.getFeature(columnToDiscretize).getValues().get(100), "B");
            Assertions.assertEquals(resultCSV.getFeature(columnToDiscretize).getValues().get(200), "B");
            Assertions.assertEquals(resultCSV.getFeature(columnToDiscretize).getValues().get(800), "B");
            
            

        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 1 FAILED: Check exception trace");
        }
    }

    @Test
    public void whenTestingForDiscretization_ShouldCoince_2(){
        try {
            String columnToDiscretize = "Age";
            csv = new CSV(mainPath, ',');
            CSV resultCSV = new DiscretizeEqualWidth(csv).process(columnToDiscretize, 5);

            Assertions.assertEquals(resultCSV.getFeatures().keySet(), csv.getFeatures().keySet()); //Test consistency
            Assertions.assertEquals(resultCSV.getColumnsNames(), csv.getColumnsNames());

            Assertions.assertEquals((int) resultCSV.getFeature(columnToDiscretize).getValues().stream().filter(row -> row.equals("A")).count(), 100);
            Assertions.assertEquals((int) resultCSV.getFeature(columnToDiscretize).getValues().stream().filter(row -> row.equals("B")).count(), 346);
            Assertions.assertEquals((int) resultCSV.getFeature(columnToDiscretize).getValues().stream().filter(row -> row.equals("C")).count(), 188);
            Assertions.assertEquals((int) resultCSV.getFeature(columnToDiscretize).getValues().stream().filter(row -> row.equals("D")).count(), 69);
            Assertions.assertEquals((int) resultCSV.getFeature(columnToDiscretize).getValues().stream().filter(row -> row.equals("E")).count(), 11);
            Assertions.assertEquals((int) resultCSV.getFeature(columnToDiscretize).getValues().stream().filter(row -> row.equals("F")).count(), 0);

            Assertions.assertEquals(resultCSV.getFeature(columnToDiscretize).getValues().get(0), "B");
            Assertions.assertEquals(resultCSV.getFeature(columnToDiscretize).getValues().get(6), "D");
            Assertions.assertEquals(resultCSV.getFeature(columnToDiscretize).getValues().get(10), "A");
            Assertions.assertEquals(resultCSV.getFeature(columnToDiscretize).getValues().get(22), "A");
            Assertions.assertEquals(resultCSV.getFeature(columnToDiscretize).getValues().get(25), "C");

        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 2 FAILED: Check exception trace");
        }
    }

    @Test
    public void whenTestingForDiscretization_ShouldCoince_3(){
        try {
            String columnToDiscretize = "Fare";
            csv = new CSV(mainPath, ',');
            CSV resultCSV = new DiscretizeEqualWidth(csv).process(columnToDiscretize, 3);

            Assertions.assertEquals(resultCSV.getFeatures().keySet(), csv.getFeatures().keySet()); //Test consistency
            Assertions.assertEquals(resultCSV.getColumnsNames(), csv.getColumnsNames());

            Assertions.assertEquals((int) resultCSV.getFeature(columnToDiscretize).getValues().stream().filter(row -> row.equals("A")).count(), 871);
            Assertions.assertEquals((int) resultCSV.getFeature(columnToDiscretize).getValues().stream().filter(row -> row.equals("B")).count(), 17);
            Assertions.assertEquals((int) resultCSV.getFeature(columnToDiscretize).getValues().stream().filter(row -> row.equals("C")).count(), 3);
            Assertions.assertEquals((int) resultCSV.getFeature(columnToDiscretize).getValues().stream().filter(row -> row.equals("D")).count(), 0);

            Assertions.assertEquals(resultCSV.getFeature(columnToDiscretize).getValues().get(50), "A");
            Assertions.assertEquals(resultCSV.getFeature(columnToDiscretize).getValues().get(700), "B");

        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 3 FAILED: Check exception trace");
        }
    }

}
