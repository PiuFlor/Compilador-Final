package compilador;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import acciones_semanticas.*;
//import compilador.ConstantesCompilador;


public class AnalizadorLexico {
    
    private static int LINEA_ACTUAL = 1;
    private static final AccionSemantica[][] acciones_semanticas = leerAccionesSemanticas(Paths.get("").normalize().toAbsolutePath()+ConstantesCompilador.RUTA_MATRIZ_ACCIONES, ConstantesCompilador.CANTIDAD_ESTADOS, ConstantesCompilador.CANTIDAD_CARACTERES);
    private static final int[][] transicion_estados = leerMatrizDeTransicion(Paths.get("").normalize().toAbsolutePath()+ConstantesCompilador.RUTA_MATRIZ_ESTADOS, ConstantesCompilador.CANTIDAD_ESTADOS, ConstantesCompilador.CANTIDAD_CARACTERES);
    public static final StringBuilder token_actual = new StringBuilder();
    public static int estado_actual = 0;
   
    static Reader lector;
    private static boolean leerproximocaracter = true;

    public static int getLineaActual() {
        return LINEA_ACTUAL;
    }
    public static void setLineaActual(int numero_linea) {
        LINEA_ACTUAL = numero_linea;
    }

    // Método para leer acciones semánticas
    public static AccionSemantica[][] leerAccionesSemanticas(String ruta, int cant_estados, int cant_caracteres) {
        AccionSemantica[][] matriz = new AccionSemantica[cant_estados][cant_caracteres];
        try {
            Path path = Paths.get(ruta).toAbsolutePath(); // Construye la ruta absoluta
            //System.out.println("Ruta absoluta del archivo de acciones semánticas: " + path.toString()); // Verifica la ruta
            File archivo = new File(path.toString());
            Scanner scanner = new Scanner(archivo);
            for (int i = 0; i < cant_estados; ++i) {
                for (int j = 0; j < cant_caracteres; ++j) {
                    matriz[i][j] = crearAccion(scanner.nextLine());
                }
            }
            scanner.close();
        } catch (FileNotFoundException excepcion) {
            System.out.println("No se pudo leer el archivo de acciones semánticas: " + excepcion.getMessage());
            excepcion.printStackTrace();
        }
        return matriz;
    }

    // Método para leer la matriz de transición de estados
    public static int[][] leerMatrizDeTransicion(String ruta, int cant_estados, int cant_caracteres) {
        int[][] matriz_estados = new int[cant_estados][cant_caracteres];
        try {
            Path path = Paths.get(ruta).toAbsolutePath(); // Construye la ruta absoluta
            //System.out.println("Ruta absoluta de la matriz de transición de estados: " + path.toString()); // Verifica la ruta
            File archivo = new File(path.toString());
            Scanner scanner = new Scanner(archivo);
            for (int i = 0; i < cant_estados; ++i) {
                for (int j = 0; j < cant_caracteres; ++j) {
                    matriz_estados[i][j] = Integer.parseInt(scanner.nextLine());
                }
            }
            scanner.close();
        } catch (FileNotFoundException excepcion) {
            System.out.println("No se pudo leer la matriz de transición de estados: " + excepcion.getMessage());
            excepcion.printStackTrace();
        }
        return matriz_estados;
    }

    public static String getTokenActual(){
        return token_actual.toString();
    }

    public static void setLector(String archivo_a_leer) throws FileNotFoundException {
        lector = new BufferedReader(new FileReader(archivo_a_leer));
    }

    private static char obtenerTipoCar (char car){
    	if (car != '0' && Character.isDigit(car)){
            return ConstantesCompilador.DIGITO;
    	} else if (car != 'd' && car != 'x' && Character.isLowerCase(car)){
            return ConstantesCompilador.MINUSCULA;
        }else if (Character.isUpperCase(car)){
            return ConstantesCompilador.MAYUSCULA;
        } else if (car == '\n') { // Verifica el salto de línea
            return ConstantesCompilador.SALTO_LINEA;
        } else {
            return car;
        }
        
    }

    public static int proximoEstado(Reader lector, char car) throws IOException {
    	
        int car_actual;
        switch (obtenerTipoCar(car)){
	        case ConstantesCompilador.MINUSCULA: 
	            car_actual = 0;
	            break;
	        case ConstantesCompilador.MAYUSCULA: 
	            car_actual = 0; 
	            break;
	        case 'd':
	            car_actual = 1;
	            break;   
	        case 'x':
	            car_actual = 2;
	            break; 
	        case ConstantesCompilador.BLANCO:
	            car_actual = 3;
	            break;
	        case ConstantesCompilador.TAB:
	            car_actual = 3;
	            break;  
	        case ConstantesCompilador.SALTO_LINEA:
	            car_actual = 4;
	            break;
	        case '0':
	            car_actual = 5;
	            break;
	        case ConstantesCompilador.DIGITO:
	            car_actual = 6;
	            break;   
	        case '/':
	            car_actual = 7;
	            break;    
	        case '*':
	            car_actual = 8;
	            break;
	        case '+':
	            car_actual = 9;
	            break;
	        case '-':
	            car_actual = 10;
	            break;   
	        case '_':
	            car_actual = 11;
	            break;
	        case ':':
	            car_actual = 12;
	            break;  
	        case '!':
	            car_actual = 13;
	            break;   
	        case '=':
	            car_actual = 14;
	            break;
	        case ',':
	            car_actual = 15;
	            break;
	        case '(':
	            car_actual = 16;
	            break;
	        case ')':
	            car_actual = 17;
	            break;
	        case '.':
	            car_actual = 18;
	            break;
	        case ';':
	            car_actual = 19;
	            break;   
	        case '<':
	            car_actual = 20;
	            break;
	        case '>':
	            car_actual = 21;
	            break;
	        case '[':
	            car_actual = 22;
	            break;
	        case ']':
	            car_actual = 23;
	            break;  
	        case '#':
	            car_actual = 24;
	            break; 
	        case '{':
	            car_actual = 25;
	            break; 
	        case '}':
	            car_actual = 26;
	            break; 
	        case '@':
	        	car_actual = 27;
	        	break;
	        default: 
	            car_actual = 28; //Si es un caracter no reconocido, lo manda a ASE
	            break;    
        }
        
        if (estado_actual == -1 ) { //no se si esta bien,es para que despues de un error continue
        	 estado_actual = 0 ;
        }
        AccionSemantica accion_semantica = acciones_semanticas[estado_actual][car_actual];
        int id_token = accion_semantica.run(lector, token_actual);
        estado_actual = transicion_estados[estado_actual][car_actual];
        //System.out.println("estado actual:  " + estado_actual);
        //System.out.println("Debug AL178: as: " + accion_semantica + " car: " + car + " ea: " + estado_actual + " id: " + id_token);
        return id_token;
    }

    private static AccionSemantica crearAccion(String action_name) {
        switch (action_name) {
			
            case "1":
                return new AS1();
            case "2":
                return new AS2();
            case "3":
                return new AS3();
            case "4":
                return new AS4();
            case "5":
                return new AS5();
            case "6":
                return new AS6();
            case "7":
                return new AS7();
            case "8":
                return new AS8();
            case "9":
                return new AS9();
            case "10":
                return new AS10();
            case "11":
                return new AS11();
            case "12":
                return new AS12();
            case "13":
                return new AS13();
            case "14":
                return new AS14();  
            case "-1":
                return new ASE(); 
            default:
                return null;
        }
    }
	public static boolean isLeerproximocaracter() {
		return leerproximocaracter;
	}
	public static void setLeerproximocaracter(boolean leerproximocaracter) {
		AnalizadorLexico.leerproximocaracter = leerproximocaracter;
	}
}
