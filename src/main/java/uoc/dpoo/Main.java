package uoc.dpoo;

import uoc.dpoo.menu.Menu;
import uoc.dpoo.menu.ReadInput;


public class Main {


    public static void main(String... args) throws Exception {
        Menu menu = new Menu(new ReadInput(System.in));
        menu.start();
    }
}
