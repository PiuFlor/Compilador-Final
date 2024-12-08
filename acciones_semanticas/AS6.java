package acciones_semanticas;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import compilador.PalabrasReservadas;
import compilador.Parser;
import compilador.TablaDeSimbolos;
import compilador.AnalizadorLexico;
import compilador.ConstantesCompilador;

/*AS6: Verifica si el carácter leido es un ‘=’. Si es asi, busca en las palabras reservadas para determinar si se está formando el operador 
<= o >= y retorna el token correspondiente. Si no se encuentra un ‘=’, devuelve el ultimo caracter leido, que sera > o <, segun corresponda.
 */

public class AS6 implements AccionSemantica {
	private static final String FILE_PATH = "Archivos/TokensGenerados.txt";
    @Override
    public int run(Reader lector, StringBuilder token_actual) throws IOException{
    	char caracterLeido = (char)lector.read();
        if (caracterLeido == '='){
		    token_actual.append(caracterLeido);
		    int id_token = PalabrasReservadas.obtenerTokenId(token_actual.toString());
		    if (id_token != ConstantesCompilador.NO_ENCONTRADO){
		    	// System.out.println (id_token);
				FileWriter writer = new FileWriter(FILE_PATH, true); 
                writer.write("Literal: " + id_token + " - LINEA: " + AnalizadorLexico.getLineaActual() +"\n");
                writer.close();  

		        return id_token;
		    } else {
		        Parser.agregarErrorLex(ConstantesCompilador.ERROR, ConstantesCompilador.LEXICO, "TOKEN NO VALIDO: "+ token_actual.toString());
		        return ConstantesCompilador.ERROR; 
		    }
		} else { //Devuelve el ultimo caracter leido
			
			String str = token_actual.toString();     
		    char char1 = str.charAt(0);
		    //System.out.println (char1);
		    int id = (int) char1;
			
		    token_actual.deleteCharAt(token_actual.length()-1);
			
			FileWriter writer = new FileWriter(FILE_PATH, true); 
            writer.write("Literal: " + char1 + " - LINEA: " + AnalizadorLexico.getLineaActual() +"\n");
            writer.close(); 

			return (int) id;
		}    
    }
}