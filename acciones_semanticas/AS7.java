package acciones_semanticas;

import java.io.IOException;
import java.io.Reader;
import compilador.AnalizadorLexico;
import compilador.ConstantesCompilador;

/*AS7: Esta Accion Semantica se encarga de leer los espacios en blanco, tabulaciones y saltos de linea, sin agregarlos al token actual. 
En caso de ser un salto de linea tambien aumenta uno la variable linea_actual del analizador lexico. */
public class AS7 implements AccionSemantica {
    @Override
    public int run(Reader lector, StringBuilder token_actual) throws IOException{
    	char caracterLeido = (char)lector.read();
        if (caracterLeido == ConstantesCompilador.SALTO_LINEA) {
		    AnalizadorLexico.setLineaActual(AnalizadorLexico.getLineaActual() + 1);
		    //System.out.println("LINEA " +  AnalizadorLexico.getLineaActual() );  
		}
        return ConstantesCompilador.EN_LECTURA; //Sigue leyendo
    }
}