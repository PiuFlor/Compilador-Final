package acciones_semanticas;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import compilador.PalabrasReservadas;
import compilador.Parser;
import compilador.AnalizadorLexico;
import compilador.ConstantesCompilador;

/*AS5: Verifica si el token obtenido pertenece a una palabra reservada(del tipo !=, >=, etc)
 Luego obtiene el símbolo y retorna el identificador del mismo.*/
public class AS5 implements AccionSemantica {
    private static final String FILE_PATH = "Archivos/TokensGenerados.txt";
    @Override
    public int run(Reader lector, StringBuilder token_actual) throws IOException{
    	char caracterLeido = (char)lector.read();
        token_actual.append(caracterLeido);

        int id_token = PalabrasReservadas.obtenerTokenId(token_actual.toString());
                if (id_token != ConstantesCompilador.NO_ENCONTRADO){
                	 //System.out.println (token_actual);
                    FileWriter writer = new FileWriter(FILE_PATH, true); 
                    writer.write("Literal: " + token_actual + " - LINEA: " + AnalizadorLexico.getLineaActual() +"\n");
                    writer.close();  

                    return id_token;
                } else {
                    Parser.agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.LEXICO, "TOKEN NO VALIDO: "+ token_actual.toString());
                    return ConstantesCompilador.ERROR; 
                 }
    }
}
