package uoc.dpoo.menu;

import java.io.InputStream;
import java.util.Scanner;

public class ReadInput {

    Scanner scanner;

    /**
     * Constructor
     * @param input object to read the user inputs
     */
    public ReadInput(InputStream input){
        this.scanner = new Scanner(input);
    }

    /**
     * Reads one line from the reader
     * @return The user input
     */
    public String read(){
        return scanner.nextLine();
    }


}
