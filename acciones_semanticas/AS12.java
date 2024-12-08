package acciones_semanticas;

import java.io.IOException;
import java.io.Reader;
import compilador.AnalizadorLexico;
import compilador.ConstantesCompilador;

/* AS12: Lee caracter y lo concatena al string. Si es un salto de linea, se actualiza la línea actual. 
 */
public class AS12 implements AccionSemantica{
    @Override
    public int run(Reader lector, StringBuilder token_actual) throws IOException{
    	char caracterLeido = (char)lector.read();
        if (caracterLeido == ConstantesCompilador.SALTO_LINEA) {
		    AnalizadorLexico.setLineaActual(AnalizadorLexico.getLineaActual() + 1);
		    //System.out.println("SALTO DE LINEA " +  AnalizadorLexico.getLineaActual());
		    token_actual.append(" ");
		}
        else {
        	token_actual.append(caracterLeido);
        }
        return ConstantesCompilador.EN_LECTURA;
    }	   
}