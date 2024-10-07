package compilador;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PalabrasReservadas {

    private static final Map<String, Integer> palabras_reservadas = new HashMap<>();
    private static final Map<Integer, String> palabrasReservadasPorId = new HashMap<>();
    
    static {
    	obtenerPalabrasReservadas(Paths.get("").normalize().toAbsolutePath()+ConstantesCompilador.RUTA_DE_PALABRAS_RESERVADAS);
    }
    
    public static Map<String, Integer> obtenerPalabrasReservadas(String ruta) {
        Map<String, Integer> mapa = new HashMap<>();
        try {
            File archivo = new File(ruta);
            Scanner scanner = new Scanner(archivo);
            while (scanner.hasNext()) {
                String palabraReservada = scanner.next();
                int id = scanner.nextInt();
                palabras_reservadas.put(palabraReservada, id);
                palabrasReservadasPorId.put(id, palabraReservada);
            }
            scanner.close();
        } catch (FileNotFoundException excepcion) {
            System.out.println("No se pudieron obtener las palabras reservadas.");
            excepcion.printStackTrace();
        }
        return mapa;
    }

    public static int obtenerTokenId(String palabraReservada) {
        return palabras_reservadas.getOrDefault(palabraReservada, ConstantesCompilador.PALABRA_NO_ENCONTRADA);
    }
    
    public static String obtenerPalabraReservadaPorId(int id) {
        return palabrasReservadasPorId.getOrDefault(id, null);
    }
    
}