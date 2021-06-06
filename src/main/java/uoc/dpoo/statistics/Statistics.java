package uoc.dpoo.statistics;

import uoc.dpoo.io.CSV;
import uoc.dpoo.io.FeatureType;
import uoc.dpoo.statistics.impl.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.DoubleStream;


public abstract class Statistics<T> {

    /**
     * CSV to process the statistics
     */
    protected final CSV csv;

    /**
     * Array with the Enum of all statistics, used to print the statistics matrix
     */
    private static final List<StatisticsEnum> statisticsNames =
            Arrays.asList(StatisticsEnum.COUNT,
                    StatisticsEnum.MAX,
                    StatisticsEnum.MIN,
                    StatisticsEnum.MEAN,
                    StatisticsEnum.MEDIAN,
                    StatisticsEnum.STD,
                    StatisticsEnum.Q1,
                    StatisticsEnum.Q2,
                    StatisticsEnum.Q3);


    /**
     * Constructor
     * @param csv CSV to process the Statistics
     */
    public Statistics(CSV csv){
        this.csv = csv;
    }

    /**
     * Process the metric
     * @param column Column to process the metric
     * @return A value than contains the metric.
     * @throws Exception Exception raised when something wrong happens
     */
    public abstract T process(String column) throws Exception;


    /**
     * Prints a matrix with all the metrics to all the statistics
     * @param csv CSV to process the statistics
     * @throws Exception Exception raised when something wrong happens
     */
    public static void describe(CSV csv) throws Exception{
        int maxColumnName = csv.getColumnsNames().size();

        Map<String, Map<StatisticsEnum,String>> values = new HashMap<>();
        for(String column : csv.getColumnsNames()) {
            Map<StatisticsEnum, String> statistics = new HashMap<>();
            String count = "-";
            String max = "-";
            String min = "-";
            String mean = "-";
            String median = "-";
            String q1 = "-";
            String q2 = "-";
            String q3 = "-";
            String std = "-";
            if (csv.getFeature(column).getType().equals(FeatureType.NUMBER)) {
                String countPlusMissing = new Count(csv).process(column) + "(" + new MissingCount(csv).process(column) + ")";
                count = fill(countPlusMissing, maxColumnName);
                max = fill(String.valueOf(new Max(csv).process(column)), maxColumnName);
                min = fill(String.valueOf(new Min(csv).process(column)), maxColumnName);
                mean = fill(String.valueOf(new Mean(csv).process(column)), maxColumnName);
                median = fill(String.valueOf(new Median(csv).process(column)), maxColumnName);
                q1 = fill(String.valueOf(new Quantil(csv).process(column, 1)), maxColumnName);
                q2 = fill(String.valueOf(new Quantil(csv).process(column, 2)), maxColumnName);
                q3 = fill(String.valueOf(new Quantil(csv).process(column, 3)), maxColumnName);
                std = fill(String.valueOf(new Std(csv).process(column)), maxColumnName);
            }
            statistics.put(StatisticsEnum.COUNT, count);
            statistics.put(StatisticsEnum.MAX, max);
            statistics.put(StatisticsEnum.MIN, min);
            statistics.put(StatisticsEnum.MEAN, mean);
            statistics.put(StatisticsEnum.MEDIAN, median);
            statistics.put(StatisticsEnum.Q1, q1);
            statistics.put(StatisticsEnum.Q2, q2);
            statistics.put(StatisticsEnum.Q3, q3);
            statistics.put(StatisticsEnum.STD, std);
            values.put(column, statistics);
        }

        StringBuilder builder = new StringBuilder();
        builder.append(fill("", maxColumnName));
        for(StatisticsEnum statistics : statisticsNames){
            builder.append(fill(statistics.name(), maxColumnName));
        }
        builder.append(System.lineSeparator());
        for(String column: csv.getColumnsNames()){
            builder.append(fill(column, maxColumnName));
            for(StatisticsEnum statisticName : statisticsNames)
                builder.append(fill(values.get(column).get(statisticName), maxColumnName));
            builder.append(System.lineSeparator());
        }
        System.out.println(builder);
    }

    /**
     * Fill a double value to a String where size is the number of decimals to show.
     * @param value Double value in String format
     * @param size Number of decimals to show
     * @return A String in the corresponding format
     */
    private static String fill(String value, int size){
        try{
            Double doubleValue = Double.valueOf(value);
            DecimalFormat format = new DecimalFormat("#.##");
            return String.format("%1$" + size + "s", format.format(doubleValue));
        }catch(NumberFormatException ex){
            return String.format("%1$" + size + "s", value);

        }
    }

    /**
     * Returns true if the value is a missing value defined in CSV class, otherwise returns false.
     * @param value String value to check if it´s a missing value.
     * @return True if it´s a missing value, false in other cases.
     */
    protected boolean isMissingValue(String value){
        return value == null || csv.getMissingValues().contains(value);
    }

    /**
     * Returns false if the value is a missing value defined in CSV class, otherwise returns true.
     * @param value String value to check if it´s a missing value.
     * @return False if it´s a missing value, true in other cases.
     */
    protected boolean isNotMissingValue(String value){
        return !isMissingValue(value);
    }

    /**
     * Converts a list of string in a List of Doubles.
     * If a value is not a number or the value is null then the value is dropped from the Stream
     * @param values List of values
     * @return a DoubleStream that contains the values converted to doubles
     */
    protected DoubleStream convertToDouble(List<String> values) {
        return values.stream().map(f -> {
            try {
                return Double.parseDouble(f);
            } catch (NumberFormatException | NullPointerException ex) {
                return null;
            }
        }).filter(Objects::nonNull).mapToDouble(Double::doubleValue);
    }
}
