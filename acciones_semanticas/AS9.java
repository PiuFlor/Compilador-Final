package acciones_semanticas;

import compilador.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

/* AS9: Verifica que el punto flotante no supere el rango permitido, verifica y/o agrega en la tabla         
     de simbolos y devuelve el token. */
 public class AS9 implements AccionSemantica {
    public static final double MAX_DOUBLE = 1.7976931348623157e+308; // Valor máximo para double
    public static final double MIN_DOUBLE = -1.7976931348623157e+308; // Valor mínimo para double
    public static final double MIN_POSITIVE = 2.2250738585072014e-308; // Valor mínimo positivo para double
     
    private static final String FILE_PATH = "Archivos/TokensGenerados.txt";
 
    @Override
    public int run(Reader lector, StringBuilder token) throws IOException{
        String lexema = token.toString().trim(); // Obtiene el token y elimina espacios en blanco
        
        try {
            // Comprueba si el token contiene una parte exponencial
        	AnalizadorLexico.setLeerproximocaracter(false);
            if (lexema.contains("d")) {
                // Reemplaza 'd' por 'e' para el parseo en Java
                lexema = lexema.replace('d', 'e');
             }
             // Intenta convertir el lexema a un número flotante
            double valor = Double.parseDouble(lexema);
             // Verifica si el valor está dentro del rango permitido
            if (valor > MAX_DOUBLE || valor < MIN_DOUBLE || 
                (valor < MIN_POSITIVE && valor > 0.0) || (valor > -MIN_POSITIVE && valor < 0.0)) {
                // Agrega un error si el valor esta fuera de rango
                Parser.agregarErrorLex(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, 
                		"El numero flotante esta fuera del rango permitido, se ajustara al valor mas cercano permitido.");
            
                // Ajusta el valor al máximo o mínimo permitido
                if (valor > MAX_DOUBLE) {
                    Parser.agregarErrorLex(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "El numero es demasiado grande. Se lo ha reemplazado por el mas cercano posible: " + MAX_DOUBLE);
                    valor = MAX_DOUBLE;
                } else if (valor < MIN_DOUBLE) {
                	Parser.agregarErrorLex(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "El numero es demasiado chico. Se lo ha reemplazado por el mas cercano posible: " + MIN_DOUBLE);
                    valor = MIN_DOUBLE;
                 }
             }
             
            // Convierte el número ajustado de nuevo a cadena para usarlo en la tabla de símbolos
            String valorAjustado = String.valueOf(valor);
             
            // Obtiene o crea el símbolo en la Tabla de Símbolos
            Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(valorAjustado);
            if (simbolo.getId() == -1) {
                 TablaDeSimbolos.agregarSimbolo(valorAjustado, ConstantesCompilador.CONSTANTE);
                 simbolo = TablaDeSimbolos.obtenerSimbolo(valorAjustado);
                 simbolo.setTipo("DOUBLE");
                 simbolo.setUso("constante");
                 //System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ", se reconocio Constante pf: " + lexema + " codigo: " + simbolo.getId());
             
                }
            FileWriter writer = new FileWriter(FILE_PATH, true); 
            writer.write("Punto Flotante: " + valorAjustado + " - LINEA: " + AnalizadorLexico.getLineaActual() +"\n");
            writer.close();
            return ConstantesCompilador.CONSTANTE;
             
         } catch (NumberFormatException e) {
             // Si ocurre una excepcion durante el parseo, agrega un error
        	 Parser.agregarErrorLex(ConstantesCompilador.ERROR, ConstantesCompilador.LEXICO, "El numero flotante introducido no es valido.");
             return ConstantesCompilador.ERROR;
         }
     }
 }