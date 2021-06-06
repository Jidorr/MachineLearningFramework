package uoc.dpoo.statistics.impl;

import uoc.dpoo.io.CSV;
import uoc.dpoo.io.Feature;
import uoc.dpoo.io.FeatureType;
import uoc.dpoo.statistics.Statistics;

import java.util.List;

public class Count extends Statistics<Long> {

    /**
     * Constructor
     * @param csv CSV to process
     */
    public Count(CSV csv){
        super(csv);
    }

    /**
     * Calculate the number of elements in the column.
     * @param column Column to process the metric
     * @return The number of elements in the column
     * @throws Exception
     */
    public Long process(String column) throws Exception {
        Feature feature = csv.getFeature(column);
        if(feature.getType().equals(FeatureType.OTHER))
            return feature.getValues().stream().filter(this::isNotMissingValue).count();
        else
            return convertToDouble(feature.getValues()).count();
    }

    /**
     * Calculate the number of no missing elements in the list.
     * @param values list of sting to calculate the number of no missing elements
     * @return the number of no missing elements.
     */
    protected long process(List<String> values){
        return values.stream().filter(this::isNotMissingValue).count();
    }

    /**
     * Calculate the number of numbers in the list.
     * @param values list of doubles, in string format, to calculate the number of no missing elements
     * @return the number of no missing elements.
     */
    protected long countNumbers(List<String> values){
        //TODO call to convertToDouble
        return convertToDouble(values).count();
    }

}
