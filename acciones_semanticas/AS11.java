package acciones_semanticas;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import compilador.AnalizadorLexico;
import compilador.ConstantesCompilador;
import compilador.Parser;
import compilador.Simbolo;
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
        if (valor > ConstantesCompilador.MAX_INT_POSITIVO+1) {
            Parser.agregarErrorLex(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "El valor esta fuera del rango permitido para un HEXADECIMAL.");
           resultado = "0x8000";
        }
        Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(String.valueOf(resultado));
        if (simbolo.getId() == -1) {
        	//simbolo.setId(ConstantesCompilador.CONSTANTE);
            TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado), ConstantesCompilador.CONSTANTE);
            TablaDeSimbolos.obtenerSimbolo(String.valueOf(resultado)).setTipo("INTEGER");
            TablaDeSimbolos.obtenerSimbolo(String.valueOf(resultado)).setUso("constante");
            
          //  FileWriter writer = new FileWriter(FILE_PATH, true); 
          //  writer.write("Entero: " + lexema + " - LINEA: " + AnalizadorLexico.getLineaActual() +"\n");
          //  writer.close();
        }  
       // TablaDeSimbolos.agregarSimbolo(resultado, ConstantesCompilador.CONSTANTE);
        
        FileWriter writer = new FileWriter(FILE_PATH, true); 
        writer.write("Hexadecimal: " + resultado + " - LINEA: " + AnalizadorLexico.getLineaActual() +"\n");
        writer.close();
        
        return ConstantesCompilador.CONSTANTE;
        
    }
}