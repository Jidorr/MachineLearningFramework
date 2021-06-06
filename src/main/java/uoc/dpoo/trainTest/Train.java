package uoc.dpoo.trainTest;

import uoc.dpoo.exceptions.CSVException;
import uoc.dpoo.io.CSV;
import uoc.dpoo.classifier.bayes.NaiveBayesClassifierMapImpl;

import java.util.*;

public class Train {
    /**
     * CSV used to train the classification model
     */
    private final CSV csv;

    /**
     * Constructor
     * @param csv used to train the classification model
     */
    public Train(CSV csv) {
        this.csv = csv;
    }

    /**
     *
     * @param classifierName name required internally by the NaiveBayesClassifierMapImpl's constructor
     * @param classColumn name of the class column, or dependent variable
     * @return a trained NaiveBayesClassifierMapImpl object
     * @throws Exception if errors happen
     */
    public NaiveBayesClassifierMapImpl process(String classifierName, String classColumn) throws Exception {
        List<String> featuresList = new ArrayList<>(csv.getColumnsNames());
        featuresList.remove(classColumn);

        NaiveBayesClassifierMapImpl bayes = new NaiveBayesClassifierMapImpl(classifierName,
                getPossibleValues(classColumn));

        for (int i = 0; i < this.csv.getRowsNumber(); i++) {
            Map<String, String> features = new HashMap<>();
            for (String column : featuresList) {
                features.put(column, this.csv.getFeature(column).getValues().get(i));
            }
            bayes.learn(this.csv.getFeature(classColumn).getValues().get(i), features);
        }
        return bayes;
    }

    private String[] getPossibleValues(String column) throws CSVException {
        Set<String> possibleValues = new HashSet<>(csv.getFeature(column).getValues());
        return possibleValues.toArray(String[]::new);
    }
}
