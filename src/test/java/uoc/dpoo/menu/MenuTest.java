package uoc.dpoo.menu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static org.mockito.Mockito.*;

import java.io.File;

public class MenuTest {

    @Test
    public void printPrincipalMenu() throws Exception {
        ReadInput read = mock(ReadInput.class);
        Menu menu = new Menu(read);
        when(read.read()).thenReturn("9");
        String text = tapSystemOut(menu::start);

        String expected = new StringBuilder("FICHERO ACTIVO: NONE" + System.lineSeparator())
                .append("1.- Seleccionar directorio" + System.lineSeparator())
                .append("2.- Listar ficheros" + System.lineSeparator())
                .append("3.- Seleccionar fichero" + System.lineSeparator())
                .append("4.- Mostrar estadisticas" + System.lineSeparator())
                .append("5.- Preprocesamiento" + System.lineSeparator())
                .append("6.- Generar Train/Test" + System.lineSeparator())
                .append("7.- Entrenar modelo" + System.lineSeparator())
                .append("8.- Validar modelo" + System.lineSeparator())
                .append("9.- Salir" + System.lineSeparator())
                .append("Selecciona una opcion" + System.lineSeparator()).toString();
        Assertions.assertEquals(expected, text);
    }

    @Test
    public void selectDirectory() {
        ReadInput read = mock(ReadInput.class);
        Menu menu = new Menu(read);
        when(read.read()).thenReturn(System.getProperty("user.dir"));
        menu.selectDirectory();
        Assertions.assertEquals(System.getProperty("user.dir") + File.separator, menu.getDirectory());
    }
}
