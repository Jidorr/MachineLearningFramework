package uoc.dpoo.statistics.impl;

import uoc.dpoo.exceptions.StatisticsException;
import uoc.dpoo.io.CSV;
import uoc.dpoo.statistics.Statistics;

import java.util.List;
import java.util.stream.Collectors;

public class Quantil extends Statistics<Double> {

    /**
     * Constructor
     * @param csv CSV to process
     */
    public Quantil(CSV csv) {
        super(csv);
    }

    /**
     * Calculate the quantil two in the feature.
     * @param column Column to process the metric
     * @return The quantil element in the feature
     * @throws Exception
     */
    @Override
    public Double process(String column) throws Exception {
        return process(column, 2);
    }

    /**
     * Calculate the quantil element in the feature.
     * @param column feature to process the metric
     * @param quantil the quantil to use
     * @return The quantil element in the feature
     * @throws Exception
     */
    public Double process(String column, int quantil) throws Exception{
        if(quantil < 1 || quantil >= 4)
            throw new StatisticsException(StatisticsException.QUANTIL_OUT_OF_RANGE);
        return process(csv.getFeature(column).getValues(), quantil);
    }

    /**
     * Calculate the quantil element in the list.
     * @param values list to process the metric
     * @param quantil the quantil to use
     * @return The quantil element in the list
     * @throws Exception
     */
    protected Double process(List<String> values, int quantil) throws StatisticsException {
        if(quantil < 1 || quantil >= 4)
            throw new StatisticsException(StatisticsException.QUANTIL_OUT_OF_RANGE);

        List<Double> sorted = convertToDouble(values).sorted().boxed().collect(Collectors.toList());
        if(sorted.isEmpty()) return Double.NaN;

        int firstPosition = 0, lastPosition = sorted.size();
        if(quantil == 1)
            lastPosition = (int) Math.floor(sorted.size() / 2d);
        else if(quantil == 3)
            firstPosition = (int) Math.ceil(sorted.size() / 2d);
        return new Median(csv).process(sorted.subList(firstPosition, lastPosition).stream().map(Object::toString).collect(Collectors.toList()));
    }
}
