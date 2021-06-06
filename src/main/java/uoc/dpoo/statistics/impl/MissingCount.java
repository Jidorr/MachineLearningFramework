package uoc.dpoo.statistics.impl;

import uoc.dpoo.io.CSV;
import uoc.dpoo.io.Feature;
import uoc.dpoo.io.FeatureType;
import uoc.dpoo.statistics.Statistics;

import java.util.List;


public class MissingCount extends Statistics<Long> {

    /**
     * Constructor
     * @param csv CSV to process
     */
    public MissingCount(CSV csv) {
        super(csv);
    }

    /**
     * Calculate the missing elements element in the feature.
     * @param column Column to process the metric
     * @return The missing elements in the feature
     * @throws Exception
     */
    public Long process(String column) throws Exception{
        Feature feature = csv.getFeature(column);
        if(feature.getType().equals(FeatureType.OTHER))
            return missingValuesOther(feature.getValues());
        else
            return missingValuesNumber(feature.getValues());
    }

    /**
     * Calculate the missing elements element in the list.
     * @param values list to process the metric
     * @return The missing elements in the list
     * @throws Exception
     */
    protected long missingValuesOther(List<String> values){
        return values.stream().filter(this::isMissingValue).count();
    }

    protected long missingValuesNumber(List<String> values){
        return values.size() - convertToDouble(values).count();
    }


}
