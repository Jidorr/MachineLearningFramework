package uoc.dpoo.exceptions;

public class CSVException extends Exception{

    public static String NO_FEATURE_EXCEPTION = "[ERROR] Feature %s does not exists in CSV";
    public static String NULL_FEATURE_NAME = "[ERROR] Feature name cannot be null";
    public static String INVALID_FEATURE_TYPE = "[ERROR] Feature %s with wrong type";
    public static String ROW_OUT_OF_BOUNDS = "[ERROR] Row %s out of bounds";

    /**
     * Constructor. Throws a Menu exception
     * @param message Message to show
     */
    public CSVException(String message){
        super(message);
    }

    /**
     * Constructor. Throws a Menu exception
     * @param message Message to show
     * @param featureName The feature's name
     */
    public CSVException(String message, String featureName){
        super(String.format(message, featureName));
    }
}
