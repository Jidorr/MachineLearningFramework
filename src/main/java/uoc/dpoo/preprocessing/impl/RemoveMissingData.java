package uoc.dpoo.preprocessing.impl;

import uoc.dpoo.io.Feature;
import uoc.dpoo.preprocessing.Preprocessing;
import uoc.dpoo.io.CSV;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RemoveMissingData extends Preprocessing {


    /**
     * Constructor
     * @param csv CSV to which remove the missing data
     */
    public RemoveMissingData(CSV csv) {
        super(csv);
    }

    /**
     * Performs the NA removal process on the whole CSV
     * @return CSV without any rows containing NAs
     * @throws Exception when errors happen
     */
    public CSV process() throws Exception {
        CSV preprocessedCSV = this.csv.clone();

        Set<Integer> indexesToRemoveSet = new HashSet<>();
        for (Feature feature : preprocessedCSV.getFeatures().values()) {
            List<String> values = feature.getValues();
            List<Integer> matchingIndices = IntStream.range(0, values.size())
                    .filter(i -> isMissingValue(values.get(i))) // Only keep those indices
                    .boxed()
                    .collect(Collectors.toList());
            indexesToRemoveSet.addAll(matchingIndices);
        }

        for (Feature feature : preprocessedCSV.getFeatures().values()) {
            List<String> values = feature.getValues();
            for(int i= values.size()-1; i>=0; i--) {
                if (indexesToRemoveSet.contains(i)) {
                    values.remove(i);
                }
            }
        }
        //preprocessedCSV.writeCSV(nuevoPath)
        return preprocessedCSV;
    }

}
