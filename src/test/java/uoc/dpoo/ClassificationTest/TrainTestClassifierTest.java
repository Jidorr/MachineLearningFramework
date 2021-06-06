package uoc.dpoo.ClassificationTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import uoc.dpoo.classifier.bayes.IClassification;
import uoc.dpoo.classifier.bayes.NaiveBayesClassifierMapImpl;
import uoc.dpoo.io.CSV;
import uoc.dpoo.preprocessing.TrainTestSplitTest;
import uoc.dpoo.preprocessing.impl.SelectFeatures;
import uoc.dpoo.trainTest.Train;

import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;

public class TrainTestClassifierTest {

    private static NaiveBayesClassifierMapImpl trainedClassifier;

    @BeforeAll
    static void init() {
        try {
            String mainPath = Paths.get(Objects.requireNonNull(TrainTestSplitTest.class.getClassLoader().getResource("titanic.csv")).toURI()).toString();
            System.out.println("CSV LOADED");

            CSV csv = new CSV(mainPath, ',');

            SelectFeatures selectFeatures = new SelectFeatures(csv);
            CSV csvSelectedFeatures = selectFeatures.process(
                    new String[]{"Survived", "Pclass", "Sex", "Age", "Cabin"});

            Train train = new Train(csvSelectedFeatures);
            trainedClassifier = train.process("trainedModel", "Survived");

        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST INIT FAILED: Check exception trace");
        }
    }

    @Test
    @Order(1)
    public void whenTestingForClassifierParameters_ShouldCoince() {
        try {
            Assertions.assertEquals(trainedClassifier.getClassifierName(), "trainedModel");
            Assertions.assertEquals(new HashSet<>(Arrays.asList(trainedClassifier.getCategories())),
                    new HashSet<>(Arrays.asList("0", "1")));
            System.out.println(trainedClassifier.dbSize());
            Assertions.assertTrue(trainedClassifier.dbSize() > 170);
        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 1 FAILED: Check exception trace");
        }
    }

    @Test
    @Order(2)
    public void whenTestingForClassification_ShouldCoince() {
        try {
            Map<String, String> features = new HashMap<>();

            features.put("Pclass", "1");
            features.put("Sex", "male");
            features.put("Age", "22");
            features.put("Cabin", "NO");
            IClassification predict = trainedClassifier.classify(features, false);

            Assertions.assertEquals(predict.getClassProbabilities()[0].getCategory(), "0");

        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 2 FAILED: Check exception trace");
        }
    }

    @Test
    @Order(3)
    public void whenTestingForClassification_ShouldCoince_2() {
        try {
            Map<String, String> features = new HashMap<>();

            features.put("Pclass", "1");
            features.put("Sex", "female");
            features.put("Age", "9");
            features.put("Cabin", "YES");
            IClassification predict = trainedClassifier.classify(features, false);

            Assertions.assertEquals(predict.getClassProbabilities()[0].getCategory(), "1");

        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST 3 FAILED: Check exception trace");
        }
    }

}

