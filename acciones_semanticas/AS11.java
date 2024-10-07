package acciones_semanticas;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import compilador.AnalizadorLexico;
import compilador.ConstantesCompilador;
import compilador.Parser;
import compilador.TablaDeSimbolos;

/* AS11: Verifica que sea un rango permitido para constante hexadecimal en integer(-0x8000 a 0x7FFF).
 Lo agrega en la tabla de simbolos y retorna el token. */

public class AS11 implements AccionSemantica {
    private static final String FILE_PATH = "Archivos/TokensGenerados.txt";
	@Override
    public int run(Reader lector, StringBuilder token_actual) throws IOException{
		AnalizadorLexico.setLeerproximocaracter(false);
		String resultado = token_actual.toString();
		// Elimina el prefijo "0x" o "-0x" si está presente
        String sinPrefijo = resultado.startsWith("-0x") 
                            ? resultado.substring(3) 
                            : resultado.startsWith("0x") 
                            ? resultado.substring(2) 
                            : resultado;

        // Convierte la cadena hexadecimal a un entero
        int valor = Integer.parseInt(sinPrefijo, 16);

        // Verifica si el valor está dentro del rango permitido
        try {
        if (valor < -0x8000 || valor > 0x7FFF) {
            Parser.agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.LEXICO, "El valor esta fuera del rango permitido para un HEXADECIMAL.");
            throw new IllegalArgumentException("El valor está fuera del rango permitido para un HEXADECIMAL.");
        }

        TablaDeSimbolos.agregarSimbolo(resultado, ConstantesCompilador.CONSTANTE);
        
        FileWriter writer = new FileWriter(FILE_PATH, true); 
        writer.write("Hexadecimal: " + resultado + " - LINEA: " + AnalizadorLexico.getLineaActual() +"\n");
        writer.close();
        
        return ConstantesCompilador.CONSTANTE;
        
        } catch (IllegalArgumentException e) {
            System.err.println("Error Lexico, Linea "+ AnalizadorLexico.getLineaActual() + ": " + e.getMessage());
            return -1;
        }
    }
}