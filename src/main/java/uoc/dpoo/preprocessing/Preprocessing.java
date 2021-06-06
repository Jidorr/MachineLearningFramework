package uoc.dpoo.preprocessing;

import uoc.dpoo.io.CSV;


public abstract class Preprocessing {

    /**
     * CSV to preprocess
     */
    protected final CSV csv;

    /**
     * Constructor
     * @param csv CSV to preprocess
     */
    public Preprocessing(CSV csv){
        this.csv = csv;
    }

    /**
     * Checks if a value is a missing value
     * @param value to check
     * @return boolean indicating whether if the value is a missing value or not
     */
    protected boolean isMissingValue(String value){
        return value == null || csv.getMissingValues().contains(value);
    }

    /**
     * Checks if a value is not a missing value
     * @param value to check
     * @return boolean indicating whether if the value is not missing value or indeed it is
     */
    protected boolean isNotMissingValue(String value){
        return !isMissingValue(value);
    }

}
