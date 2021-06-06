package uoc.dpoo.trainTest;

import uoc.dpoo.classifier.bayes.ClassifyException;
import uoc.dpoo.classifier.bayes.INaiveBayesClassifier;
import uoc.dpoo.exceptions.CSVException;
import uoc.dpoo.io.CSV;
import uoc.dpoo.classifier.bayes.IClassification;
import java.util.*;

public class Test {

    /**
     * Uses the trained classifier to compute the classifications over the validation set, and build a Pair containing
     * such classified value along with the true value
     * @param csvFileTest csv to use as test set
     * @param columnClass column containing the true values of the class
     * @param trainedModel trained model to validate
     * @return list of Pair, i.e. list of true values-classifications
     * @throws ClassifyException if there is a classification error
     * @throws CSVException if columnClass not found
     */
    public static List<Pair> comparation(CSV csvFileTest, String columnClass,
                                         INaiveBayesClassifier trainedModel) throws ClassifyException, CSVException {

        List<Pair> results = new LinkedList<>();
        for(int i = 0; i < csvFileTest.getRowsNumber(); i++){
            Map<String, String> row = csvFileTest.getRow(i);
            String classValue = row.remove(columnClass);
            IClassification predict = trainedModel.classify(row, false);
            results.add(new Pair(classValue, predict.getClassProbabilities()[0].getCategory()));
        }
        return results;
    }
}

