package uoc.dpoo.menu;

import uoc.dpoo.classifier.bayes.INaiveBayesClassifier;
import uoc.dpoo.exceptions.MenuException;
import uoc.dpoo.preprocessing.PreprocessingEnum;
import uoc.dpoo.preprocessing.impl.*;
import uoc.dpoo.metrics.Metrics;
import uoc.dpoo.io.CSV;
import uoc.dpoo.statistics.Statistics;
import uoc.dpoo.trainTest.Pair;
import uoc.dpoo.trainTest.Test;
import uoc.dpoo.trainTest.Train;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Menu {

    /**
     * Directory to use as workspace
     */
    private String directory = "";
    /**
     * Model
     */
    private INaiveBayesClassifier model;
    /**
     * CSV to preprocess
     */
    private CSV csv;
    /**
     * CSV to used to train
     */
    private CSV trainCSV;
    /**
     * CSV to test
     */
    private CSV testCSV;

    /**
     * Separator used to write the output csvs.
     */
    private final char sep = ';';

    /**
     * Reader used to read the user input
     */
    private final ReadInput reader;

    /**
     * Constructor
     * @param readInput reader to read the user input
     */
    public Menu(ReadInput readInput) {
        this.reader = readInput;
    }

    /**
     * Return the workspace directory
     * @return directory value
     */
    public String getDirectory(){
        return this.directory;
    }

    /**
     * Prints the principal menu, and ask the user to input an option
     * @return The selected option by the user
     */
    private String printPrincipalMenu(){
        System.out.println("FICHERO ACTIVO: " + (csv != null ? csv.getPath() : "NONE"));
        System.out.println("1.- Seleccionar directorio");
        System.out.println("2.- Listar ficheros");
        System.out.println("3.- Seleccionar fichero");
        System.out.println("4.- Mostrar estadisticas");
        System.out.println("5.- Preprocesamiento");
        System.out.println("6.- Generar Train/Test");
        System.out.println("7.- Entrenar modelo");
        System.out.println("8.- Validar modelo");
        System.out.println("9.- Salir");
        System.out.println("Selecciona una opcion");
        return this.reader.read();
    }

    /**
     * Prints the menu to select the directory to use as workspace
     */
    void selectDirectory(){
        System.out.println("Selecciona directorio de trabajo");
        this.directory = this.reader.read();
        if(!directory.endsWith(File.separator))
            directory += File.separator;
        System.out.println("Seleccionado: "+ this.directory);
    }

    /**
     * Print a list of all files in the workspace
     */
    private void listFiles(){
        for(String file : Objects.requireNonNull((new File(directory)).list())){
            System.out.println(file);
        }
    }

    /**
     * Ask the user to select the file and the separator to read the work CSV.
     * @throws Exception
     */
    private void selectFileToCSV() throws Exception {
        csv = new CSV(selectFile(), selectSeparator());
        System.out.println("Fichero cargado correctamente");
    }

    /**
     * Ask the user to select the file to work
     * @return The Path where the file is allocated
     */
    private String selectFile(){
        System.out.println("Indica fichero de entrada");
        return directory + this.reader.read();
    }

    /**
     * Ask the user to select the separator to use when the CSV is readed.
     * @return The separator
     */
    private char selectSeparator(){
        System.out.println("Indica separador del fichero");
        return this.reader.read().charAt(0);
    }

    /**
     * Ask the user the parameters to execute the Discretization and execute it.
     * @throws Exception
     */
    private void doDiscretization() throws Exception {
        if(csv == null)
            throw new MenuException(MenuException.ERROR_WORKSPACE);

        System.out.println("Indica el nombre de la columnas a discretizar (separados por comas)");
        String[] columns = this.reader.read().split(",");

        System.out.println("Indica número de bins");
        int bins = Integer.parseInt(this.reader.read());

        csv = new DiscretizeEqualWidth(csv).process(columns, bins);
        String output = csv.write(sep, PreprocessingEnum.EQUALWIDTHDISCRETIZE_SUFFIX);

        System.out.println("Generado fichero: "+ output);
        System.out.println("Discretización realizada correctamente");
    }

    /**
     * Ask the user the parameters to execute the FeatureSelection and execute it.
     * @throws Exception
     */
    private void doFeatureSelection() throws Exception {
        if(csv == null)
            throw new MenuException(MenuException.ERROR_CSV);

        System.out.println("Lista de features (nombre de columnas), separadas por comas");
        String[] values =  this.reader.read().split(",");

        csv = new SelectFeatures(csv).process(values);
        String output = csv.write(sep, PreprocessingEnum.SELECTFEATURES_SUFFIX);
        System.out.println("Generado fichero: "+ output);
        System.out.println("Finalizada la selección de características");
    }

    /**
     * Ask the user the parameters to execute the MissingData and execute it.
     * @throws Exception
     */
    private void doMissingData() throws Exception {
        if(csv == null)
            throw new MenuException(MenuException.ERROR_CSV);

        csv = new RemoveMissingData(csv).process();
        String output = csv.write(sep, PreprocessingEnum.REMOVENA_SUFFIX);
        System.out.println("Generado fichero: "+ output);
        System.out.println("Eliminadas columnas con datos nulos");
    }

    /**
     * Ask the user the parameters to execute the normalization and execute it.
     * @throws Exception
     */
    private void doNormalization() throws Exception {
        if(csv == null)
            throw new MenuException(MenuException.ERROR_CSV);

        System.out.println("Indica el nombre de la columnas a normalizar(separado por comas)");
        String[] columns  = this.reader.read().split(",");

        csv = new NormalizeMINMAX(csv).process(columns);
        String output = csv.write(sep, PreprocessingEnum.NORMALIZE_MINMAX_SUFFIX);
        System.out.println("Generado fichero: "+ output);
        System.out.println("Columnas normalizadas con éxito");
    }


    /**
     * Shows the statistics.
     * @throws Exception
     */
    private void showStatistics() throws Exception {
        if(csv == null)
            throw new MenuException(MenuException.ERROR_CSV);

        Statistics.describe(csv);
    }

    /**
     * Shows the preprocesing menu and ask the user to enter an option.
     */
    private void showPreprocessing(){
        String value;
        do {
            System.out.println("Preprocesamiento");
            System.out.println("----------------");
            System.out.println("1.- Discretización");
            System.out.println("2.- Selección de características");
            System.out.println("3.- Missing Data");
            System.out.println("4.- Normalización");
            System.out.println("5.- Atrás");

            Scanner scanner = new Scanner(System.in);
            value = scanner.nextLine();

            try {
                switch (value){
                    case "1":
                        doDiscretization();
                        break;
                    case "2":
                        doFeatureSelection();
                        break;
                    case "3":
                        doMissingData();
                        break;
                    case "4":
                        doNormalization();
                        break;
                    default:
                        break;
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }while(!value.equals("5"));
    }

    /**
     * Ask the user the parameters to execute the Split and execute it.
     * @throws Exception
     */
    private void doSplit() throws Exception {
        if(csv == null)
            throw new MenuException(MenuException.ERROR_CSV);

        System.out.println("Indica el porcentaje del tamaño del test");
        float test = Float.parseFloat(this.reader.read());

        System.out.println("Indica si la generación es random (T|F)");
        boolean random = Boolean.parseBoolean(this.reader.read());

        ResponseTrainTestSplit response = new TrainTestSplit(csv).process(test, random);
        trainCSV = response.getCsvTrain();
        testCSV = response.getCsvTest();
        String outputTrain = trainCSV.write(sep, PreprocessingEnum.TRAIN_SPLIT_SUFFIX);
        String outputTest = testCSV.write(sep, PreprocessingEnum.TEST_SPLIT_SUFFIX);
        System.out.println("Generado fichero: "+ outputTrain);
        System.out.println("Generado fichero: "+ outputTest);
        System.out.println("Ficheros de entrenamiento y test generado correctamente");
    }

    /**
     * Train the model
     * If there are not train CSV loaded the method ask the user to load it.
     * @throws Exception
     */
    private void trainModel() throws Exception {
        if(trainCSV == null)
            trainCSV = new CSV(selectFile(), selectSeparator());

        System.out.println("Indica el nombre de la columna que contiene el valor de clasificación");
        String column = this.reader.read();

        model = new Train(trainCSV).process("bayes", column);
        System.out.println("El modelo se ha entrado satisfactoriamente");
    }

    /**
     * Show the prediction metrics metrics
     * @throws Exception
     */
    private void doMetrics() throws Exception {
        if(model == null)
            throw new MenuException(MenuException.ERROR_TRAIN_MODEL);
        if(testCSV == null) {
            System.out.println("Indica el fichero de test");
            String test = this.reader.read();
            char sep = selectSeparator();
            testCSV = new CSV(test,sep);
        }

        System.out.println("Indica el nombre de la columna que contiene el valor de clasificación");
        String column = this.reader.read();

        List<Pair> results = Test.comparation(testCSV, column, model);
        Metrics metrics = new Metrics(results);
        System.out.println("Accuracy:"+ String.format("%2f", metrics.accuracy()));
        System.out.println("Precision:"+ String.format("%2f", metrics.precision()));
        System.out.println("Recall:"+ String.format("%2f", metrics.recall()));
        System.out.println("F1:"+ String.format("%2f", metrics.f1()));

        metrics.confusionMatrix();

    }

    /**
     * Starts the execution of the menu, showing the principalMenu
     */
    public void start(){
        String value;
        do {
            value = printPrincipalMenu();
            try {
                switch (value) {
                    case "1":
                        selectDirectory();
                        break;
                    case "2":
                        listFiles();
                        break;
                    case "3":
                        selectFileToCSV();
                        break;
                    case "4":
                        showStatistics();
                        break;
                    case "5":
                        showPreprocessing();
                        break;
                    case "6":
                        doSplit();
                        break;
                    case "7":
                        trainModel();
                        break;
                    case "8":
                        doMetrics();
                        break;
                    default:
                        break;
                }
            }catch(NoSuchFieldException ex){
                System.err.println("[WARN] File not found."+ ex.getMessage());
            }catch(Exception ex){
                System.err.println(ex.getMessage());
            }
        }while (!value.equals("9"));
    }

}
