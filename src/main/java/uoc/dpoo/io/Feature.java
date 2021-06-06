package uoc.dpoo.io;

import uoc.dpoo.exceptions.CSVException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Feature implements Cloneable{

    /**
     * The name feature
     */
    private String name;
    /**
     * The values stored in the feature
     */
    private List<String> values;
    /**
     * The type of the feature
     */
    private FeatureType type;

    /**
     * Constuctor
     * @param name The name of the feature
     * @throws CSVException Raised if the name is null
     */
    public Feature(String name) throws CSVException {
        this(name, new ArrayList<>());
    }

    /**
     * Constructor
     * @param name The name of the feature
     * @param values The values of the feature
     * @throws CSVException Raised if the name is null
     */
    public Feature(String name, List<String> values) throws CSVException {
        setName(name);
        setValues(values);
    }

    /**
     * Checks if all the values are numbers and change the features' type
     * If the feature has all values as number then the type is FeatureType.NUMBER
     * if is not a number or the values is empty the type is FeatureType.OTHER
     */
    public void fixType(){
        if(values.isEmpty())
            setType(FeatureType.OTHER);
        else if(isNumber()){
            setType(FeatureType.NUMBER);
        }else{
            setType(FeatureType.OTHER);
        }
    }

    /**
     * Check if all the values are numbers, in that case returns true , otherwise false.
     * Before checks the values, filter the nulls
     * @return True if all values are numbers, otherwise returns false
     */
    private boolean isNumber(){
        return values.stream().filter(Objects::nonNull).allMatch(f -> {
            try {
                Double.parseDouble(f);
                return true;
            }catch(Exception ex){
                return false;
            }
        });
    }

    /**
     * Set the name of the Feature, if the name is null it raises NullFeatureException
     * @param name Feature's name
     * @throws CSVException Raised when name is null
     */
    private void setName(String name) throws CSVException{
        if(name == null)
            throw new CSVException(CSVException.NULL_FEATURE_NAME);
        else
            this.name = name;
    }

    /**
     * Returns the  feature's name
     * @return The name of the feature
     */
    public String getName() {
        return name;
    }

    /**
     * Get the feature values.
     * @return All values stored in the feature
     */
    public List<String> getValues() {
        return values;
    }

    /**
     * Set the values of the feature
     * @param values List of the string with the feature values
     */
    public void setValues(List<String> values) {
        this.values = values;
        fixType();
    }

    /**
     * Returns the type of the feature.
     * @return feature type
     */
    public FeatureType getType() {
        return type;
    }

    /**
     * Set the type of the feature
     * @param type feature type
     */
    public void setType(FeatureType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feature feature = (Feature) o;
        return Objects.equals(name, feature.name) &&
                Objects.equals(values, feature.values) &&
                type.equals(feature.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, values, type);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        try {
            List<String> clonedList = new ArrayList<>(this.values);
            return new Feature(this.name, clonedList);
        } catch (CSVException e) {
            throw new CloneNotSupportedException(e.getMessage());
        }
    }
}
