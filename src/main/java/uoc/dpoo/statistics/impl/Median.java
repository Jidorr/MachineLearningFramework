package uoc.dpoo.statistics.impl;

import uoc.dpoo.exceptions.CSVException;
import uoc.dpoo.io.CSV;
import uoc.dpoo.io.Feature;
import uoc.dpoo.statistics.Statistics;

import java.util.List;
import java.util.stream.Collectors;

public class Median  extends Statistics<Double> {

    /**
     * Constructor
     * @param csv CSV to process
     */
    public Median(CSV csv) {
        super(csv);
    }

    /**
     * Calculate the median element in the feature.
     * @param column Column to process the metric
     * @return The median element in the feature
     * @throws Exception
     */
    public Double process(String column) throws CSVException {
        Feature feature = csv.getFeature(column);
        return process(feature.getValues());
    }

    /**
     * Calculate the median element in the list.
     * @param values values to process the metric
     * @return The median element in the feature
     * @throws Exception
     */
    protected Double process(List<String> values){
        List<Double> vals = convertToDouble(values).sorted().boxed().collect(Collectors.toList());
        int size = vals.size();
        if(vals.isEmpty())
            return Double.NaN;
        else if(size % 2 == 0)
            return ((vals.get(size/2) + vals.get(size/2 - 1))) / 2;
        else
            return (vals.get(size/2));
    }
}
