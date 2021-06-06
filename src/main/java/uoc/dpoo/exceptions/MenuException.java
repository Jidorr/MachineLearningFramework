package uoc.dpoo.exceptions;

public class MenuException extends Exception{

    public static final String ERROR_WORKSPACE = "[ERROR] You must select a workspace";
    public static final String ERROR_CSV = "[ERROR] You must select a CSV";
    public static final String ERROR_TRAIN_MODEL = "[ERROR] You must train the model";

    /**
     * Constructor. Throws a Menu exception
     * @param message Message to show
     */
    public MenuException(String message){
        super(message);
    }
}
