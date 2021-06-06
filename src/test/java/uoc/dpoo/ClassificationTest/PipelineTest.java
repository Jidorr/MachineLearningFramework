package uoc.dpoo.ClassificationTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uoc.dpoo.classifier.bayes.NaiveBayesClassifierMapImpl;
import uoc.dpoo.io.CSV;
import uoc.dpoo.metrics.Metrics;
import uoc.dpoo.preprocessing.PreprocessingEnum;
import uoc.dpoo.preprocessing.TrainTestSplitTest;
import uoc.dpoo.preprocessing.impl.*;
import uoc.dpoo.trainTest.Pair;
import uoc.dpoo.trainTest.Train;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;

public class PipelineTest {


    @Test
    public void pipelineTest() {
        try {
            String mainPath = Paths.get(Objects.requireNonNull(TrainTestSplitTest.class.getClassLoader().getResource("titanic.csv")).toURI()).toString();
            System.out.println("CSV LOADED");

            CSV csv = new CSV(mainPath, ',');
            String classcolumn = "Survived";

            //1-SELECT FEATURES
            SelectFeatures selectFeatures = new SelectFeatures(csv);
            CSV csvSelectedFeatures = selectFeatures.process(
                    new String[]{"Survived", "Pclass", "Sex", "Age", "SibSp", "Parch", "Fare", "Cabin"}); //1,2,4,5,6,7,9,10

            //2-REMOVE NAs
            RemoveMissingData removeMissingData = new RemoveMissingData(csvSelectedFeatures);
            CSV csvRemovedMissingData = removeMissingData.process();

            //3-NORMALIZE
            NormalizeMINMAX normalizeMINMAX = new NormalizeMINMAX(csvRemovedMissingData);
            CSV csvNormalized = normalizeMINMAX.process("Fare");

            //4-DISCRETIZE
            DiscretizeEqualWidth discretizeEqualWidth = new DiscretizeEqualWidth(csvNormalized);
            CSV csvDiscretized = discretizeEqualWidth.process("Age", 5);

            //5-TRAIN-TEST SPLIT
            TrainTestSplit trainTestSplit = new TrainTestSplit(csvDiscretized);
            ResponseTrainTestSplit responseTrainTestSplit = trainTestSplit.process((float) 0.2, true);
            responseTrainTestSplit.getCsvTest().write(';', PreprocessingEnum.TEST_SPLIT_SUFFIX);

            //6-TRAINING
            Train train = new Train(responseTrainTestSplit.getCsvTrain());
            NaiveBayesClassifierMapImpl trainedClassifier = train.process("trainedModel", classcolumn);

            //7-TEST
            //uoc.dpoo.trainTest.Test test = new uoc.dpoo.trainTest.Test(responseTrainTestSplit.getCsvTest());
            List<Pair> results = uoc.dpoo.trainTest.Test.comparation(responseTrainTestSplit.getCsvTest(), "Survived", trainedClassifier);
            Metrics result_metrics = new Metrics(results);
            System.out.println("Accuracy: " + result_metrics.accuracy() * 100);
            Assertions.assertTrue((result_metrics.accuracy() * 100) > 70);

        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST FAILED: Check exception trace");
        }
    }
}

