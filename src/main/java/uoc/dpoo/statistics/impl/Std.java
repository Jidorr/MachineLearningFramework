package uoc.dpoo.statistics.impl;

import uoc.dpoo.io.CSV;
import uoc.dpoo.io.Feature;
import uoc.dpoo.statistics.Statistics;

import java.util.List;

public class Std extends Statistics<Double> {

    /**
     * Constructor
     * @param csv CSV to process
     */
    public Std(CSV csv) {
        super(csv);
    }

    /**
     * Calculate the std element in the feature.
     * @param column column to process the metric
     * @return The std element in the feature
     * @throws Exception
     */
    public Double process(String column) throws Exception{
        Feature feature = csv.getFeature(column);
        return process(feature.getValues());
    }

    /**
     * Calculate the std element in the list.
     * @param values values to process the metric
     * @return The std element in the list
     * @throws Exception
     */
    protected double process(List<String> values){
        return Math.sqrt(new Variance(csv).process(values));
    }

}
