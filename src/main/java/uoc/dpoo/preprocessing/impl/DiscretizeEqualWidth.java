package uoc.dpoo.preprocessing.impl;

import uoc.dpoo.common.Util;
import uoc.dpoo.io.Feature;
import uoc.dpoo.io.FeatureType;
import uoc.dpoo.preprocessing.Preprocessing;
import uoc.dpoo.io.CSV;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static uoc.dpoo.common.Util.convertToDouble;
import static uoc.dpoo.common.Util.convertToDoubleAndNA;


public class DiscretizeEqualWidth extends Preprocessing {

    /**
     * Constructor
     * @param csv CSV to discretize
     */
    public DiscretizeEqualWidth(CSV csv)  {
        super(csv);
    }

    /**
     * Perform the CSV discretization of a single column
     * @param column name of the column (feature) to discretize
     * @param bins number of bins or categories to use
     * @return csv discretized
     * @throws Exception raised if errors happen
     */
    public CSV process(String column, int bins) throws Exception {
        return process(new String[] {column}, bins);
    }

    /**
     * Perform the CSV discretization of a single column
     * @param columns name of the columns (features) to discretize
     * @param bins number of bins or categories to use
     * @return csv discretized
     * @throws Exception raised if errors happen
     */
    public CSV process(String[] columns, int bins) throws Exception {
        CSV preprocessedCSV = this.csv.clone();
        Util.checkColumnNames(csv, columns);
        Util.checkColumnsType(csv, columns, FeatureType.NUMBER);

        for (String column: columns) {
            List<Double> numericColumn = convertToDoubleAndNA(preprocessedCSV.getFeature(column).getValues()).boxed().collect(Collectors.toList());
            double min = Collections.min(numericColumn);
            double max = Collections.max(convertToDouble(preprocessedCSV.getFeature(column).getValues()).boxed().collect(Collectors.toList()));

            List<String> discretizedColumn = numericColumn.stream().map(s ->
                discretize(s, min, max, bins)
            ).collect(Collectors.toList());
            preprocessedCSV.addOrUpdateFeature(new Feature(column, discretizedColumn));
        }

        //preprocessedCSV.writeCSV(nuevoPath)
        return preprocessedCSV;
    }

    /**
     * Clamps the value to discretize, i.e. returns the bin index in which the value yields
     * @param value numerical value to discretize
     * @param min lower threshold (always 0)
     * @param max upper threshold (binCount - 1)
     * @return clamped value
     */
    private static int clamp(int value, int min, int max) {
        if (value < min) value = min;
        if (value > max) value = max;
        return value;
    }

    /**
     * Discretizes a single value. Should return a character in the A-Z range
     * @param value numerical value to discretize
     * @param min minimum value of the collection to discretize
     * @param max maximum value of the collection to discretize
     * @param binCount number of bins to use
     * @return discretized value in the A-Z range
     */
    private static String discretize(double value, double min, double max, int binCount) {
        if (Double.isNaN(value)) {
            return "NA";
        }
        int discreteValue = (int) (binCount * NormalizeMINMAX.normalize(value, min, max));
        String discreteValues = "ABCDEFGHIJKLMNOPQRSTUVXYZ";
        return String.valueOf(discreteValues.charAt(clamp(discreteValue, 0, binCount - 1)));
    }
}
