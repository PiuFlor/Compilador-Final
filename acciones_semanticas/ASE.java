package acciones_semanticas;

import compilador.AnalizadorLexico;
import compilador.Parser;
import java.io.IOException;
import java.io.Reader;
import compilador.ConstantesCompilador;

//AS error, devulve un -1 al perser como id del token
public class ASE implements AccionSemantica{

  @Override
  public int run(Reader lector, StringBuilder token_actual) throws IOException{
    	char caracterLeido = (char)lector.read();
        int linea = AnalizadorLexico.getLineaActual();
		Parser.agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.LEXICO, "Error no descripto en declaracion lexica. Verifique el formato de los caracteres ingresados y del salto de linea (LF)");
		
		if (caracterLeido == ConstantesCompilador.SALTO_LINEA) {
		    AnalizadorLexico.setLineaActual(linea + 1);
		}
        return ConstantesCompilador.ERROR; //Se retorna un -1
    }
}