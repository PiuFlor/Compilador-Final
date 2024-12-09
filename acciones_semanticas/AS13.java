package acciones_semanticas;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import compilador.AnalizadorLexico;
import compilador.ConstantesCompilador;
import compilador.TablaDeSimbolos;

/* AS13: Lee y agrega el ultimo caracter a la cadena. Agrega el string a la tabla de simbolos sin saltos de linea y devuelve el token. */
public class AS13 implements AccionSemantica{
    private static final String FILE_PATH = "Archivos/TokensGenerados.txt";
    @Override
    public int run(Reader lector, StringBuilder token_actual) throws IOException {
        char caracterLeido = (char)lector.read();
        token_actual.append(caracterLeido);
        token_actual.toString().replaceAll("\n", " ");
        //System.out.println(token_actual);
        int id_simbolo = TablaDeSimbolos.agregarSimbolo(token_actual.toString(),ConstantesCompilador.CADENA);
        //System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ", se reconocio Cadena: " + token_actual + " codigo: " + ConstantesCompilador.CADENA);
        FileWriter writer = new FileWriter(FILE_PATH, true); 
        writer.write("Cadena: " + token_actual + " - LINEA: " + AnalizadorLexico.getLineaActual() +"\n");
        writer.close(); 
        return id_simbolo;
    }	   
}