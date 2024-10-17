package compilador;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Main {
    private static final Map<Integer, String> archivos = new HashMap<>();
    private static final String FILE_PATH = "Archivos/TokensGenerados.txt";

    static {
        archivos.put(1, "compilador/CodigoPrueba/CasosPruebaAsigMult.txt");
        archivos.put(2, "compilador/CodigoPrueba/CasosPruebaCadenas.txt");
        archivos.put(3, "compilador/CodigoPrueba/CasosPruebaComentario.txt");
        archivos.put(4, "compilador/CodigoPrueba/CasosPruebaCTE.txt");
        archivos.put(5, "compilador/CodigoPrueba/CasosPruebaGOTO.txt");
        archivos.put(6, "compilador/CodigoPrueba/CasosPruebaPair.txt");
        archivos.put(7, "compilador/CodigoPrueba/CasosPruebaPRMay.txt");
        archivos.put(8, "compilador/CodigoPrueba/CasosPruebaPRMin.txt");
        archivos.put(9, "compilador/CodigoPrueba/CasosPruebaSentenciasDeclarativas.txt");
        archivos.put(10, "compilador/CodigoPrueba/CasosPruebaSentenciasEjec.txt");
        archivos.put(11, "compilador/CodigoPrueba/CasosPruebaSubtiposT11.txt");
        archivos.put(12, "compilador/CodigoPrueba/CasosPruebaVAR.txt");
        archivos.put(13, "compilador/CodigoPrueba/ProbarAsignacion.txt");
        archivos.put(14, "compilador/CodigoPrueba/CasosPruebaErrorCadenas.txt");
        archivos.put(15, "compilador/CodigoPrueba/CasosPruebaErrorComentario.txt");
        archivos.put(16, "compilador/CodigoPrueba/CasosPruebaErrorCTE.txt");
        archivos.put(17, "compilador/CodigoPrueba/CasosPruebaErrorVAR.txt");

        archivos.put(18, "compilador/errores/CantidadErroneaParametros.txt");
        archivos.put(19, "compilador/errores/errorfuncionnombre.txt");
        archivos.put(20, "compilador/errores/errorfuncionret.txt");
        archivos.put(21, "compilador/errores/ErrorParametroOUFT.txt");
        archivos.put(22, "compilador/errores/Faltacoma.txt");
        archivos.put(23, "compilador/errores/FaltaComparadores.txt");
        archivos.put(24, "compilador/errores/FaltaContenidoBloque.txt");
        archivos.put(25, "compilador/errores/FaltaCuerpoIt.txt");
        archivos.put(26, "compilador/errores/FaltaDelimitadorPrograma.txt");
        archivos.put(27, "compilador/errores/FaltaEND_IF.txt");
        archivos.put(28, "compilador/errores/Faltanombreprograma.txt");
        archivos.put(29, "compilador/errores/FaltaOperandoExp.txt");
        archivos.put(30, "compilador/errores/FaltaParametroFormal.txt");
        archivos.put(31, "compilador/errores/FaltaParametroOUFT.txt");
        archivos.put(32, "compilador/errores/FaltaParEnConSenItySel.txt");
        archivos.put(33, "compilador/errores/Tema11.txt");
        archivos.put(34, "compilador/errores/Tema21.txt");
        archivos.put(35, "compilador/errores/Tema23.txt");
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Archivos disponibles:");
        limpiarArchivo();

        for (Map.Entry<Integer, String> entry : archivos.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }

        System.out.println(" ");
        //Ejemplo de ruta:  C:\\Users\\TuUsuario\\Documents\\archivo.txt
        System.out.println("0. Ingresar ruta de archivo personalizada");
        System.out.println("Ejemplo de ruta:  C:\\Users\\TuUsuario\\Documents\\archivo.txt");
        System.out.println(" ");
        System.out.print("Seleccione el archivo a procesar: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada

        String archivo = null;

        if (opcion == 0) {
            System.out.print("Ingrese la ruta del archivo a procesar: ");
            String rutaIngresada = scanner.nextLine();
            File archivoPersonalizado = new File(rutaIngresada);

            if (archivoPersonalizado.exists() && archivoPersonalizado.isFile()) {
                archivo = rutaIngresada;
            } else {
                System.out.println("La ruta ingresada no es válida o el archivo no existe.");
            }
        } else {
            archivo = archivos.get(opcion);
        }

        if (archivo != null) {
            System.out.println("Archivo seleccionado: " + archivo);
            AnalizadorLexico.setLector(archivo);
            Parser parser = new Parser();
            parser.run();

            System.out.println("\nSe ha terminado de compilar\n");
            imprimirTokensGenerados();
            TablaDeSimbolos.imprimirTabla();
            Parser.imprimirErrores();
        } else {
            System.out.println("Opción inválida.");
        }

        scanner.close();
    }

    public static void limpiarArchivo() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                FileWriter writer = new FileWriter(file, false);
                writer.write("");
                writer.close();
                System.out.println("Archivo limpio al iniciar la ejecución.");
            } else {
                file.createNewFile();
                System.out.println("Archivo creado.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void imprimirTokensGenerados() {
    	System.out.println("TOKENS: ");
    	try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}