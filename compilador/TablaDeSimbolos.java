package compilador;
import java.util.*;

public class TablaDeSimbolos {

    private static int identificador_sig = 283;
    private static ArrayList<Simbolo> simbolos = new ArrayList<>();


    //agrega simbolo
    public static int agregarSimbolo(String lexema) {
        ++identificador_sig;
        Simbolo nuevo = new Simbolo(lexema, identificador_sig);
        simbolos.add(nuevo);
        return identificador_sig;
    }

    public static int agregarSimbolo(String lexema, int id) {
        Simbolo nuevo = new Simbolo(lexema,id);
        simbolos.add(nuevo);
        return id;
    }

    public static int agregarSimbolo(String lexema, int id, String ambito) {
        Simbolo nuevo = new Simbolo(lexema,id);
        nuevo.setAmbito(ambito);
        simbolos.add(nuevo);
        return id;
    }

    public static int agregarSimbolo(String lexema, String ambito) {
        ++identificador_sig;
        Simbolo nuevo = new Simbolo(lexema, identificador_sig);
        nuevo.setAmbito(ambito);
        simbolos.add(nuevo);
        return identificador_sig;
    }

    public static Simbolo buscarPorId(int id) {
        for (Simbolo simbolo : simbolos) { // Recorremos la lista de símbolos
            if (simbolo.getId() == id) {   // Comparamos el id del símbolo
                return simbolo;            // Retornamos el símbolo si coincide
            }
        }
        return ConstantesCompilador.SIMBOLO_NO_ENCONTRADO; // Si no se encuentra, retornamos un símbolo por defecto
    }
    

    // Devuelve el primer simbolo con igual lexema
    public static Simbolo obtenerSimbolo(String lexema) {
        for (Simbolo simbolo: simbolos) {
            if (simbolo.getLexema().equals(lexema))
                return simbolo;
        }
        return ConstantesCompilador.SIMBOLO_NO_ENCONTRADO;
    }

    // Devuelve el primer simbolo con igual lexema y ambito
    public static Simbolo obtenerSimbolo(String lexema, String ambito) {
        if(lexema.startsWith("@") || lexema.startsWith("_"))
            lexema = lexema.substring(1);
        for (Simbolo simbolo: simbolos) {
            if (simbolo.getLexema().equals(lexema))
                if (simbolo.getAmbito().equals(ambito)) {
                    return simbolo;
                }
        }
        if (ambito.equals("")) {
            return ConstantesCompilador.SIMBOLO_NO_ENCONTRADO;
        }
        return (obtenerSimbolo(lexema, ambito.substring(0, ambito.lastIndexOf("."))));
    }
    
    public static Simbolo obtenerSimbolodentrodelAmbito(String lexema) {
    	    Simbolo simboloEncontrado = ConstantesCompilador.SIMBOLO_NO_ENCONTRADO;
    	    
    	    // Iterar sobre la tabla de símbolos
    	    for (Simbolo simbolo : simbolos) {
    	        String simboloLexema = simbolo.getLexema();
    	        
    	        // Si el lexema del símbolo es un prefijo del lexema dado, se actualiza simboloEncontrado
    	        if (lexema.startsWith(simboloLexema)) {
    	            // Guardamos la coincidencia más larga que empiece con el prefijo
    	            if (simboloEncontrado == ConstantesCompilador.SIMBOLO_NO_ENCONTRADO || simboloLexema.length() > simboloEncontrado.getLexema().length()) {
    	                simboloEncontrado = simbolo;
    	            }
    	        }
    	    }
    	    
    	    // Devolver el símbolo encontrado (o el valor por defecto si no se encontró)
    	    return simboloEncontrado;
    	}

    //A patir del nombre de la funcion, devuelve el nombre de sus parametros
    public static Simbolo obtenerParametro(String nombreFuncion) {
        Simbolo parametro = null;
        for (Simbolo simbolo: simbolos) {
            if (simbolo.getAmbito().equals(nombreFuncion)) { //Si la ultima parte del ambito es la funcion
                
                if (simbolo.getUso().equals("Parametro")) {//Y si el uso del simbolo es parametro
                	//System.out.println(" nombre parametro: "+simbolo.getLexema());
                    parametro = simbolo;}
        }}
       // System.out.println(" nombre parametro afuera: "+parametro.getLexema());
        return parametro;
    }
    public static Simbolo obtenerParametro2(String nombreFuncion) {
        Simbolo parametro = null;
        for (Simbolo simbolo: simbolos) {
            if (simbolo.getAmbito().contains(nombreFuncion)) { //Si la ultima parte del ambito es la funcion
                
                if (simbolo.getUso().equals("Parametro")) {//Y si el uso del simbolo es parametro
                	//System.out.println(" nombre parametro: "+simbolo.getLexema());
                    parametro = simbolo;}
        }}
       // System.out.println(" nombre parametro afuera: "+parametro.getLexema());
        return parametro;
    }
    
    public static boolean existeSimboloAmbitoActual(String lexema) {
        for (Simbolo simbolo : simbolos) {
            // Verificar si el lexema coincide
            if (simbolo.getLexema().equals(lexema)) {
                return true;
            }
        }
        // Si no se encuentra el símbolo con las condiciones dadas
        return false;
    }

    public static Simbolo obtenerSimboloSinAmbito(String lexema) {
        for (Simbolo simbolo : simbolos) {
            // Comparar solo el lexema del símbolo
            if (simbolo.getLexema().equals(lexema)) {
                return simbolo; // Devolver el símbolo si coincide el lexema
            }
        }
        // Si no se encuentra, devolver un símbolo por defecto
        return ConstantesCompilador.SIMBOLO_NO_ENCONTRADO;
    }
    public static Simbolo obtenerSimboloFuncion(String lexema) {
        for (Simbolo simbolo : simbolos) {
            // Comparar solo el lexema del símbolo
            if (simbolo.getLexema().startsWith(lexema)) {
                return simbolo; // Devolver el símbolo si coincide el lexema
            }
        }
        // Si no se encuentra, devolver un símbolo por defecto
        return ConstantesCompilador.SIMBOLO_NO_ENCONTRADO;
    }
    //elimina simbolo
    public static void eliminarSimbolo(String lexema) {
        simbolos.remove(lexema);
    }

    //imprime los simbolos
    public static void imprimirTabla() {
        System.out.println("\nTabla de Simbolos:" + " (longitud: "+simbolos.size()+")");
        for (Simbolo simbolo: simbolos) {
        	
           if (simbolo.getLexema().contains(":") ||simbolo.getLexema().contains("$") || simbolo.getLexema().contains("@") ) {
        	 if (simbolo.getUso().equals("SUBTIPO")) {
                     System.out.println(simbolo.getLexema() + ": ID " + simbolo.getId() + ": Tipo " + simbolo.getTipo() + " Uso " + simbolo.getUso()+ " Ambito " + simbolo.getAmbito()+ " Indice Inf:" + simbolo.getRango1()
                      + " Indice Sup:" + simbolo.getRango2() );
        	 }else if (simbolo.getUso().equals("funcion")) {
                 System.out.println(simbolo.getLexema() + ": ID " + simbolo.getId() + ": Tipo " + simbolo.getTipo() + " Uso " + simbolo.getUso()+ " Ambito " + simbolo.getAmbito());
        	       }else if (simbolo.getTipo().equals("INTEGER") || simbolo.getTipo().equals("INTEGER")) {
        	    	   System.out.println(simbolo.getLexema() + ": ID " + simbolo.getId() + ": Tipo " + simbolo.getTipo() + " Uso " + simbolo.getUso()+ " Ambito " + simbolo.getAmbito());
        	            } else if (obtenerSimbolo(simbolo.getTipo()).getUso().equals("SUBTIPO")) {
        	            	System.out.println(simbolo.getLexema() + ": ID " + simbolo.getId() + ": Tipo " + simbolo.getTipo() + " Uso " + simbolo.getUso()+ " Ambito " + simbolo.getAmbito()+" Subtipo: " + simbolo.getTipoParametro() +  " Indice Inf:" + simbolo.getRango1()
                            + " Indice Sup:" + simbolo.getRango2() );
        	            }else {
        	            	System.out.println(simbolo.getLexema() + ": ID " + simbolo.getId() + ": Tipo " + simbolo.getTipo() + " Uso " + simbolo.getUso()+ " Ambito " + simbolo.getAmbito()+" Subtipo: " + simbolo.getTipoParametro());
        	            } 
           }else if (simbolo.getUso().equals("constante")) {
        	   System.out.println(simbolo.getLexema() + ": ID " + simbolo.getId() + ": Tipo " + simbolo.getTipo() + " Uso " + simbolo.getUso());
	           
           }
        }
        System.out.println("");
    }

    public static ArrayList<Simbolo> obtenerTablaDeSimbolos() {
        return simbolos;
    }
    
    public static boolean existeSimbolo(String lexema) {
        // Separar el lexema por ':' para obtener las partes del símbolo y el ámbito
        String[] partes = lexema.split(":");

        // Chequear que el lexema tenga al menos un ámbito
        if (partes.length < 2) {
            return false; // Si no hay suficientes partes, no puede existir en un ámbito
        }

        // El símbolo buscado sería todo lo que está antes del primer ':'
        String simboloBuscado = lexema.substring(0, lexema.indexOf(':'));

        // El ámbito sería todo lo que sigue después del primer ':'
        String ambitoBuscado = lexema.substring(lexema.indexOf(':') + 1).replace(':', '.');

        // Buscar si existe el símbolo y si pertenece al mismo ámbito o subámbito
        for (Simbolo simbolo : simbolos) {
            // Verificar si el lexema coincide con el símbolo buscado
            if (simbolo.getLexema().equals(simboloBuscado)) {
                // Comprobar si el ámbito del símbolo buscado es igual o está contenido en el ámbito actual
                if (simbolo.getAmbito().startsWith(ambitoBuscado)) {
                    return true; // El símbolo y su ámbito coinciden o el símbolo está en un subámbito
                }
            }
        }

        // Si no se encuentra el símbolo con el ámbito dado o un subámbito
        return false;
    }
    
    public static boolean esTipoEspecial (String tipo) {
   	 for (Simbolo simbolo : simbolos) {
            
            if (simbolo.getLexema().equals(tipo)) {
                return true; // Devolver el símbolo si coincide el lexema
            
        }
        // Si no se encuentra, devolver un símbolo por defecto
   }
	return false;
   }
}   
