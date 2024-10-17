package acciones_semanticas;

import java.io.IOException;
import java.io.Reader;

import compilador.AnalizadorLexico;
import compilador.ConstantesCompilador;

public class AS0 implements AccionSemantica {
   

	@Override
	public int run(Reader lector, StringBuilder token_actual) throws IOException {
		char caracterLeido = (char) lector.read();
		if (caracterLeido == ConstantesCompilador.SALTO_LINEA) {
		    AnalizadorLexico.setLineaActual(AnalizadorLexico.getLineaActual() + 1);
		}
	        return ConstantesCompilador.EN_LECTURA; //Sigue leyendo
		
	}
}
