package acciones_semanticas;

import java.io.IOException;
import java.io.Reader;
import compilador.ConstantesCompilador;

//AS1: Inicializa string y aniade el caracter leido 
public class AS1 implements AccionSemantica
{
    @Override
    public int run(Reader lector, StringBuilder token)
    {   
		try {
			char caracterLeido = (char) lector.read();
			token.setLength((0));
			token.append(caracterLeido);
	        return  ConstantesCompilador.EN_LECTURA;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ConstantesCompilador.ERROR;
    }
}