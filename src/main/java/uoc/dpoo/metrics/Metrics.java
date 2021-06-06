package uoc.dpoo.metrics;

import uoc.dpoo.trainTest.Pair;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Class to calculate the Metrics from train and test model.
 * The Class only supports binary classification.
 */
public class Metrics {

    /**
     * List of Tuples that contains the true label and th predicted one.
     */
    private final List<Pair> results;

    /**
     * Constructor
     * @param results List of tuples with true and predicted labels
     */
    public Metrics(List<Pair> results){
        this.results = results;
    }

    /**
     * Calculate the accuracy
     * @link https://developers.google.com/machine-learning/crash-course/classification/accuracy
     * @return the accuracy value
     */
    public double accuracy(){
        long size = this.results.size();
        double matched = this.results.stream().filter(Pair::areEquals).count();
        return matched / size;
    }

    /**
     * Prints the confusionMatrix
     */
    public void confusionMatrix(){
        DecimalFormat format = new DecimalFormat("#.##");
        System.out.println(" |0\t\t1");
        System.out.println("-----------");
        System.out.println("0|"+ format.format(trueNegative()) +"\t\t"+ format.format(falseNegative()));
        System.out.println("1|"+ format.format(falsePositive()) +"\t\t"+ format.format(truePositive()));
    }

    /**
     * Calculate the recall
     * @link https://developers.google.com/machine-learning/crash-course/classification/precision-and-recall
     * @return the recall value
     */
    public double recall(){
        return truePositive() / (truePositive() + falseNegative());
    }

    /**
     * Calculate the precision
     * @link https://developers.google.com/machine-learning/crash-course/classification/precision-and-recall
     * @return the precision value
     */
    public double precision(){
        return truePositive() / (truePositive() + falsePositive());
    }

    /**
     * Calculate the F1
     * @link https://en.wikipedia.org/wiki/F-score
     * @return the f1 value
     */
    public double f1(){
        return 2 / ((1/recall()) + (1/precision()));
    }

    /**
     * Returns the number of true positives.
     * Values that true value is 1 and predicted value is 1
     * @return number of true positives
     */
    private double truePositive(){
        return this.results.stream().filter(f -> f.getValue().equals("1") && f.areEquals()).count();
    }

    /**
     * Returns the number of true negatives.
     * Values that true value is 0 and predicted value is 0
     * @return number of true negatives
     */
    private double trueNegative(){
        return this.results.stream().filter(f -> f.getValue().equals("0") && f.areEquals()).count();
    }

    /**
     * Returns the number of false positives.
     * Values that true value is 0 and predicted value is 1
     * @return number of false positives
     */
    private double falsePositive(){
        return this.results.stream().filter(f -> f.getPrediction().equals("1") && !f.areEquals()).count();
    }

    /**
     * Returns the number of false negatives.
     * Values that true value is 1 and predicted value is 0
     * @return number of true positives
     */
    private double falseNegative(){
        return this.results.stream().filter(f -> f.getPrediction().equals("0") && !f.areEquals()).count();
    }
}
