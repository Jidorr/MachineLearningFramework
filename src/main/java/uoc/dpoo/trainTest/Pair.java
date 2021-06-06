package uoc.dpoo.trainTest;

public class Pair {

    /**
     * Class predicted by the classification model
     */
    private final String prediction;
    /**
     * Ground true class
     */
    private final String value;

    /**
     * Constructor
     * @param value ground true class
     * @param prediction class predicted by the classification model
     */
    public Pair(String value, String prediction){
        this.prediction = prediction;
        this.value = value;
    }

    /**
     * Checks if prediction and value match
     * @return boolean indicatting if prediction and ground true class match
     */
    public boolean areEquals(){
        return prediction.equals(value);
    }

    /**
     * getter for the prediction attribute
     * @return the predicted class
     */
    public String getPrediction() {
        return prediction;
    }

    /** Getter for the ground true class value
     * @return the ground true class value
     */
    public String getValue() {
        return value;
    }

}
