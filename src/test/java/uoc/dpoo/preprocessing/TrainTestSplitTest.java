package uoc.dpoo.preprocessing;

import org.junit.jupiter.api.*;
import uoc.dpoo.io.CSV;
import uoc.dpoo.io.Feature;
import uoc.dpoo.preprocessing.impl.ResponseTrainTestSplit;
import uoc.dpoo.preprocessing.impl.TrainTestSplit;

import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrainTestSplitTest {

    private static CSV csv;
    private static String mainPath;
    private static ResponseTrainTestSplit trainTestSplit, trainTestSplitRandom;

    @BeforeAll
    static void init() {
        try {
            mainPath = Paths.get(Objects.requireNonNull(TrainTestSplitTest.class.getClassLoader().getResource("titanic.csv")).toURI()).toString();
            System.out.println("CSV LOADED");

            csv = new CSV(mainPath, ',');
            trainTestSplit = new TrainTestSplit(csv).process((float) 0.2, false);

        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST INIT FAILED: Check exception trace");
        }
    }

    @Test
    @Order(1)
    public void whenTestingForConsistency_ShouldCoince() {
        ConsistencyTest(trainTestSplit);
    }

    public void ConsistencyTest(ResponseTrainTestSplit response){
        try {
            Assertions.assertEquals(response.getCsvTrain().getFeatures().keySet(), csv.getFeatures().keySet());
            Assertions.assertEquals(response.getCsvTest().getFeatures().keySet(), csv.getFeatures().keySet());

            Assertions.assertEquals(response.getCsvTrain().getColumnsNames(), csv.getColumnsNames());
            Assertions.assertEquals(response.getCsvTest().getColumnsNames(), csv.getColumnsNames());

            Assertions.assertEquals(csv.getRowsNumber(),
                    response.getCsvTrain().getRowsNumber()+response.getCsvTest().getRowsNumber());

            for (Feature feature : response.getCsvTrain().getFeatures().values()) { //Check every column
                Assertions.assertEquals(feature.getValues().size(), 712); //0.8
            }

            for (Feature feature : response.getCsvTest().getFeatures().values()) { //Check every column
                Assertions.assertEquals(feature.getValues().size(), 179);  //0.2
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("CONSISTENCY FAILED: Check exception trace");
        }
    }

    @Test
    @Order(2)
    public void whenTestingForValues_ShouldCoince(){
        try {
            Assertions.assertEquals(trainTestSplit.getCsvTrain().getFeature("Name").getValues().get(711), "Klaber Mr. Herman");
            Assertions.assertEquals(trainTestSplit.getCsvTest().getFeature("Name").getValues().get(0), "Taylor Mr. Elmer Zebley");
            Assertions.assertEquals(trainTestSplit.getCsvTest().getFeature("Name").getValues().get(trainTestSplit.getCsvTest().getRowsNumber()-1), "Dooley Mr. Patrick");

        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 2 FAILED: Check exception trace");
        }
    }

    @Test
    @Order(3)
    public void whenTestingForConsistencyRandomSplit_ShouldCoince(){
        try {
            trainTestSplitRandom = new TrainTestSplit(csv).process((float) 0.2, true);
            ConsistencyTest(trainTestSplitRandom);
        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 3 FAILED: Check exception trace");
        }
    }

    @Test
    @Order(4)
    public void whenTestingForRandomSplit_ShouldNotBeTheSame(){
        try {
            Assertions.assertNotEquals(trainTestSplit.getCsvTest().getFeature("Name"), trainTestSplitRandom.getCsvTest().getFeature("Name"));

        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 4 FAILED: Check exception trace");
        }
    }

}
