package acciones_semanticas;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import compilador.*;

/* AS8: Verifica que el entero no exceda el rango permitido, verifica y/o agrega en la tabla de            
simbolos y devuelve el token. */
 public class AS8 implements AccionSemantica{
    public static final int MAX_INT = 32768;
    private static final String FILE_PATH = "Archivos/TokensGenerados.txt";

    @Override
    public int run(Reader lector, StringBuilder token) throws IOException{
    	AnalizadorLexico.setLeerproximocaracter(false);
        String lexema = token.toString();
        int leido = Integer.parseInt(lexema);
        if (leido > MAX_INT){
            Parser.agregarErrorLex(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, " El entero ingresado se encuentra fuera de rango, sera cambiado por el entero mas cercano permitido ");
            leido = MAX_INT;

            FileWriter writer = new FileWriter(FILE_PATH, true); 
            writer.write("Entero: " + leido + " - LINEA: " + AnalizadorLexico.getLineaActual() +"\n");
            writer.close();
        }
        Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(String.valueOf(leido));
        if (simbolo.getId() == -1) {
        	//simbolo.setId(ConstantesCompilador.CONSTANTE);
            TablaDeSimbolos.agregarSimbolo(String.valueOf(leido), ConstantesCompilador.CONSTANTE);
            TablaDeSimbolos.obtenerSimbolo(String.valueOf(leido)).setTipo("i16");
            TablaDeSimbolos.obtenerSimbolo(String.valueOf(leido)).setUso("constante");
            
            FileWriter writer = new FileWriter(FILE_PATH, true); 
            writer.write("Entero: " + lexema + " - LINEA: " + AnalizadorLexico.getLineaActual() +"\n");
            writer.close();
        }      
        return ConstantesCompilador.CONSTANTE;
    }
}