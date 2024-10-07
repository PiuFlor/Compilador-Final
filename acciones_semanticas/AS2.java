package acciones_semanticas;

import java.io.IOException;
import java.io.Reader;
import compilador.ConstantesCompilador;

// AS2: Lee caracter y lo concatena al token actual.
public class AS2 implements AccionSemantica {
    
    @Override
    public int run(Reader lector, StringBuilder token)
    {
		try {
			char caracterLeido = (char) lector.read();
	        token.append(caracterLeido);
	        return ConstantesCompilador.EN_LECTURA;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return ConstantesCompilador.ERROR;
    }
}
