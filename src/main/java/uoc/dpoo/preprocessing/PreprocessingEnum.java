package uoc.dpoo.preprocessing;

public class PreprocessingEnum {
    /**
     * Suffix for selected features
     */
    public static final String SELECTFEATURES_SUFFIX = "_featuresSelected";
    /**
     * Suffix for NA Removal
     */
    public static final String REMOVENA_SUFFIX = "_NARemoved";
    /**
     * Suffix for discretized CSV
     */
    public static final String EQUALWIDTHDISCRETIZE_SUFFIX = "_discretizedEW";
    /**
     * Suffix for normalized CSV
     */
    public static final String NORMALIZE_MINMAX_SUFFIX = "_NormalizedMINMAX";
    /**
     * Suffix for the training split
     */
    public static final String TRAIN_SPLIT_SUFFIX = "_Training";
    /**
     * Suffix for the testing split
     */
    public static final String TEST_SPLIT_SUFFIX = "_Test";


    /**
     * Implemented preprocessing methods
     */
    public enum methods {SELECTFEATURES, REMOVENA, EQUALWIDTHDISCRETIZE, NORMALIZE_MINMAX};

    /**
     * Returns the suffix from the method
     * @param method for which get the suffix
     * @return the suffix corresponding to the method
     */
    static String getMethodSuffix(methods method) {
        switch (method) {
            case REMOVENA:
                return REMOVENA_SUFFIX;
            case EQUALWIDTHDISCRETIZE:
                return EQUALWIDTHDISCRETIZE_SUFFIX;
            case NORMALIZE_MINMAX:
                return NORMALIZE_MINMAX_SUFFIX;
            case SELECTFEATURES:
                return SELECTFEATURES_SUFFIX;
            default:
                return "_NA";
        }
    }
}
