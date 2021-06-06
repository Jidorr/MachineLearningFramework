package uoc.dpoo.exceptions;

public class StatisticsException extends Exception
{
    public static String QUANTIL_OUT_OF_RANGE = "[ERROR] Quantil out of range (1-4)";

    /**
     * Constructor
     * @param message message to show
     */
    public StatisticsException(String message){
        super(message);
    }
}
