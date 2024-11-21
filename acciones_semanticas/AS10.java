package acciones_semanticas;

import java.io.IOException;
import java.io.Reader;
import compilador.*;

/* AS10:  Lee caracter y lo concatena al string, si es una letra verifica que no supere el rango      
     permitido (A-F). */
public class AS10 implements AccionSemantica{
    @Override
    public int run(Reader lector, StringBuilder token_actual) throws IOException {
    	char caracterLeido = (char)lector.read();
        if (caracterLeido >= 'A' && caracterLeido <= 'F'){
		    token_actual.append(caracterLeido);
		    return ConstantesCompilador.EN_LECTURA;
		    } else {
		        Parser.agregarErrorLex(ConstantesCompilador.ERROR, ConstantesCompilador.LEXICO, "Caracter no valido");
		    	//System.out.println("CARACTER NO VALIDO");
		   
		    	token_actual.append("F");
		    	System.out.println(token_actual);
		 
		        return ConstantesCompilador.EN_LECTURA;
		    }
    }
}