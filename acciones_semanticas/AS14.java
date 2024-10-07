package acciones_semanticas;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import compilador.AnalizadorLexico;
import compilador.ConstantesCompilador;
import compilador.Parser;
import compilador.TablaDeSimbolos;

/* AS14: Lee caracter y lo concatena al string, si supera la longitud maxima se trunca.
Verifica y/o agrega en la tabla de simbolos y devuelve el token. (para etiqueta) */
public class AS14 implements AccionSemantica{
	private static final String FILE_PATH = "Archivos/TokensGenerados.txt";
	@Override
		public int run(Reader lector, StringBuilder token) throws IOException{
	    	char caracterLeido = (char)lector.read();
		 	token.append(caracterLeido);
			
	        String lexema = token.toString();
	   
	        int IdToken = TablaDeSimbolos.obtenerSimbolo(lexema).getId(); //Si ya existe se lo devuelve
	        if (IdToken == -1) {
	           if (lexema.length() > ConstantesCompilador.LONGITUD_MAXIMA_ID) {//Checkeo de longitud maxima de ID
	        	   lexema = lexema.substring(0, ConstantesCompilador.LONGITUD_MAXIMA_ID-1);
	        	   lexema = lexema + "@"; 
                   Parser.agregarError(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "La etiqueta supera la longitud maxima de "+ ConstantesCompilador.LONGITUD_MAXIMA_ID+" caracteres.");
	            }

			}
			TablaDeSimbolos.agregarSimbolo(lexema, ConstantesCompilador.ETIQUETA); //Si no se lo crea
			System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ", se reconocio la etiqueta: " + lexema + " codigo: " + ConstantesCompilador.ETIQUETA);
	        
			FileWriter writer = new FileWriter(FILE_PATH, true); 
			writer.write("Etiqueta: " + lexema + " - LINEA: " + AnalizadorLexico.getLineaActual() +"\n");
            writer.close(); 

	        return ConstantesCompilador.ETIQUETA;
	    }
}