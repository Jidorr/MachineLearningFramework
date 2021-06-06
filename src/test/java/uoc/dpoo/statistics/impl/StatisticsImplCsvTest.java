package uoc.dpoo.statistics.impl;

import org.junit.jupiter.api.*;
import uoc.dpoo.exceptions.CSVException;
import uoc.dpoo.io.CSV;
import uoc.dpoo.preprocessing.DiscretizeTest;
import uoc.dpoo.statistics.Statistics;

import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Objects;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static org.junit.jupiter.api.Assertions.fail;

public class StatisticsImplCsvTest {

    private CSV csv;

    @BeforeEach
    void init() {
        Locale.setDefault(Locale.US);
        try {
            csv = new CSV(Paths.get(Objects.requireNonNull(DiscretizeTest.class.getClassLoader().getResource("titanic.csv")).toURI()).toString(), ',');
            System.out.println("CSV LOADED");
        } catch (Exception e) {
            e.printStackTrace();
            fail("TEST INIT FAILED: Check exception trace");
        }
    }

    @Test
    public void countValues() throws Exception {
        Assertions.assertEquals(714, new Count(csv).process("Age"));
        Assertions.assertEquals(891, new Count(csv).process("Sex"));
        Assertions.assertEquals(891, new Count(csv).process("Fare"));
    }


    @Test
    public void maxValues() throws Exception {
        Assertions.assertEquals(80, new Max(csv).process("Age"));
        Assertions.assertEquals(Double.NaN, new Max(csv).process("Sex"));
        Assertions.assertEquals(512.3292, new Max(csv).process("Fare"));
    }

    @Test
    public void minValues() throws Exception {
        Assertions.assertEquals(0.42, new Min(csv).process("Age"));
        Assertions.assertEquals(Double.NaN, new Min(csv).process("Sex"));
        Assertions.assertEquals(0, new Min(csv).process("Fare"));

    }

    @Test
    public void meanValues() throws CSVException {
        DecimalFormat format = new DecimalFormat("#.##");
        Assertions.assertEquals("29.7", format.format(new Mean(csv).process("Age")));
        Assertions.assertEquals(Double.NaN, new Mean(csv).process("Sex"));
        Assertions.assertEquals("32.2", format.format(new Mean(csv).process("Fare")));
    }

    @Test
    public void medianValues() throws CSVException {
        Assertions.assertEquals(28, new Median(csv).process("Age"));
        Assertions.assertEquals(Double.NaN, new Median(csv).process("Sex"));
        Assertions.assertEquals(14.4542, new Median(csv).process("Fare"));

    }

    @Test
    public void quantilValues() throws Exception {
        Assertions.assertEquals(28, new Quantil(csv).process("Age"));
        Assertions.assertEquals(Double.NaN, new Quantil(csv).process("Sex"));
        Assertions.assertEquals(14.4542, new Quantil(csv).process("Fare"));
    }

    @Test
    public void stdValues() throws Exception {
        DecimalFormat format = new DecimalFormat("#.##");
        Assertions.assertEquals("14.52", format.format(new Std(csv).process("Age")));
        Assertions.assertEquals(Double.NaN, new Std(csv).process("Sex"));
        Assertions.assertEquals("49.67", format.format(new Std(csv).process("Fare")));

    }

    @Test
    public void varianceValues() throws Exception {
        DecimalFormat format = new DecimalFormat("#.##");
        Assertions.assertEquals("210.72", format.format(new Variance(csv).process("Age")));
        Assertions.assertEquals(Double.NaN, new Variance(csv).process("Sex"));
        Assertions.assertEquals("2466.67", format.format(new Variance(csv).process("Fare")));

    }

    @Test
    public void MissingCountValues() throws Exception {
        Assertions.assertEquals(177, new MissingCount(csv).process("Age"));
        Assertions.assertEquals(0, new MissingCount(csv).process("Sex"));
        Assertions.assertEquals(0, new MissingCount(csv).process("Fare"));
    }

    @Test
    public void describe() throws Exception {
        String actual = tapSystemOut(()-> Statistics.describe(csv));
        String expected = "                   COUNT         MAX         MIN        MEAN      MEDIAN         STD          Q1          Q2          Q3" + System.lineSeparator() +
                "         Age    714(177)          80        0.42        29.7          28       14.52          20          28          38" + System.lineSeparator() +
                "       Cabin           -           -           -           -           -           -           -           -           -" + System.lineSeparator() +
                "    Embarked           -           -           -           -           -           -           -           -           -" + System.lineSeparator() +
                "        Fare      891(0)      512.33           0        32.2       14.45       49.67         7.9       14.45          31" + System.lineSeparator() +
                "        Name           -           -           -           -           -           -           -           -           -" + System.lineSeparator() +
                "       Parch      891(0)           6           0        0.38           0        0.81           0           0           0" + System.lineSeparator() +
                " PassengerId      891(0)         891           1         446         446      257.21         223         446         669" + System.lineSeparator() +
                "      Pclass      891(0)           3           1        2.31           3        0.84           2           3           3" + System.lineSeparator() +
                "         Sex           -           -           -           -           -           -           -           -           -" + System.lineSeparator() +
                "       SibSp      891(0)           8           0        0.52           0         1.1           0           0           1" + System.lineSeparator() +
                "    Survived      891(0)           1           0        0.38           0        0.49           0           0           1" + System.lineSeparator() +
                "      Ticket           -           -           -           -           -           -           -           -           -" + System.lineSeparator() + System.lineSeparator();
        Assertions.assertEquals(expected, actual);
        }


}
