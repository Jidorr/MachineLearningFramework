package uoc.dpoo.preprocessing.impl;

import uoc.dpoo.io.CSV;
import uoc.dpoo.io.Feature;
import uoc.dpoo.preprocessing.Preprocessing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainTestSplit extends Preprocessing {
    /**
     * Constructor
     * @param csv CSV to normalize
     */
    public TrainTestSplit(CSV csv) {
        super(csv);
    }

    /**
     * Performs the training-test splitting
     * @param testPercentage percentage in the (0-1) interval to withold from the CSV and use it as test set
     * @param randomize indicates whether the split will be performed preserving the natural order of the instances or randomized
     * @return a response containing both the training and test set splits
     * @throws Exception if errors happen
     */
    public ResponseTrainTestSplit process(float testPercentage, boolean randomize) throws Exception {
        CSV csvTrain = this.csv.clone();
        CSV csvTest = this.csv.clone();

        int sizeValues = this.csv.getRowsNumber();
        int cutpoint = (int) (sizeValues * (1 - testPercentage));

        List<Integer> indicesCSV = IntStream.range(0, sizeValues).boxed().collect(Collectors.toList());
        if (randomize) Collections.shuffle(indicesCSV);
        List<Integer> indicesTrain = indicesCSV.subList(0, cutpoint);

        for (String keyColumn : this.csv.getColumnsNames()) { //iteramos por columna
            Feature feature = this.csv.getFeature(keyColumn);
            List<String> values = feature.getValues();
            List<String> trainSplit = new ArrayList<>();
            List<String> testSplit = new ArrayList<>();
            for (int i = 0; i < values.size(); i++) { //iteramos por filas
                if (indicesTrain.contains(i)) {
                    trainSplit.add(values.get(i));
                } else {
                    testSplit.add(values.get(i));
                }
            }
            csvTrain.addOrUpdateFeature(new Feature(keyColumn,trainSplit));
            csvTest.addOrUpdateFeature(new Feature(keyColumn,testSplit));
        }
        // devolver respuesta con los dos csvs
        return new ResponseTrainTestSplit(csvTrain, csvTest);
    }
}

