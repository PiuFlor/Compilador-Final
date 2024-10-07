package acciones_semanticas;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import compilador.AnalizadorLexico;
import compilador.ConstantesCompilador;
import compilador.Parser;

//AS4: Reconoce literal y devuelve token.
public class AS4 implements AccionSemantica{
    private static final String FILE_PATH = "Archivos/TokensGenerados.txt";
    @Override
    public int run (Reader lector, StringBuilder token_actual) throws IOException {
    	 try{
            char literal = (char) lector.read();
            //System.out.println (literal);             
        
            FileWriter writer = new FileWriter(FILE_PATH, true); 
            writer.write("Literal reconocido: " + literal + " - LINEA: " + AnalizadorLexico.getLineaActual() +"\n");
            writer.close();  
            
            return (int) literal;
         } catch (IOException ex){
             ex.printStackTrace();
         } 
         Parser.agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.LEXICO, "No se reconoce el caracter introducido.");
         return ConstantesCompilador.ERROR;
     }
}