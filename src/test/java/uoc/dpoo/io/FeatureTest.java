package uoc.dpoo.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uoc.dpoo.exceptions.CSVException;

import java.util.List;

public class FeatureTest {

    @Test
    @DisplayName("When Feature is created with null name a NullFeatureNameException is raised")
    public void setNullName(){
        Assertions.assertThrows(CSVException.class, () -> new Feature(null));
    }

    @Test
    @DisplayName("When Feature is created name is set to the parameter, type is OTHER and values is empty")
    public void setDefaultValues() throws CSVException {
        Feature feature = new Feature("test");
        Assertions.assertEquals("test", feature.getName());
        Assertions.assertEquals(FeatureType.OTHER, feature.getType());
        Assertions.assertTrue(feature.getValues().isEmpty());
    }

    @Test
    @DisplayName("When set values with only numbers the type is NUMBER")
    public void checkTypeWithNumbers() throws CSVException {
        Feature feature = new Feature("test");
        feature.setValues(List.of("1", "2", "3"));
        Assertions.assertEquals(FeatureType.NUMBER, feature.getType());
    }

    @Test
    @DisplayName("When set values with numbers and other values the type is OTHER")
    public void checkTypeWithNumbersAndOther() throws CSVException {
        Feature feature = new Feature("test");
        feature.setValues(List.of("1", "2", ""));
        Assertions.assertEquals(FeatureType.OTHER, feature.getType());
    }

    @Test
    @DisplayName("When set values with numbers and letters the type is OTHER")
    public void checkTypeWithNumbersAndText() throws CSVException {
        Feature feature = new Feature("test");
        feature.setValues(List.of("1", "ab", "3"));
        Assertions.assertEquals(FeatureType.OTHER, feature.getType());
    }
}
