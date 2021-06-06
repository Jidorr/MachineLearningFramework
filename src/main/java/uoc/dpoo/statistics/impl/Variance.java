package uoc.dpoo.statistics.impl;

import uoc.dpoo.io.CSV;
import uoc.dpoo.statistics.Statistics;

import java.util.List;
import java.util.stream.DoubleStream;

public class Variance extends Statistics<Double> {

    /**
     * Constructor
     * @param csv CSV to process
     */
    public Variance(CSV csv) {
        super(csv);
    }

    /**
     * Calculate the variance element in the feature.
     * @param column column to process the metric
     * @return The variance element in the feature
     * @throws Exception
     */
    public Double process(String column) throws Exception{
        return process(csv.getFeature(column).getValues());
    }

    /**
     * Calculate the variance element in the list.
     * @param values values to process the metric
     * @return The variance element in the list
     * @throws Exception
     */
    protected Double process(List<String> values){
        DoubleStream doubleValues = convertToDouble(values);
        long size = convertToDouble(values).count();
        try {
            double mean = new Mean(csv).process(values);
            return doubleValues.map((value) -> Math.pow(value - mean, 2)).sum() / size;
        }catch(NullPointerException ex){
            return Double.NaN;
        }
    }
}
