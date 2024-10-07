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

    //A patir del nombre de la funcion, devuelve el nombre de sus parametros
    public static List<Simbolo> obtenerParametros(String nombreFuncion) {
        List<Simbolo> parametros = new ArrayList<>();
        if(nombreFuncion.startsWith("@") || nombreFuncion.startsWith("_"))
            nombreFuncion = nombreFuncion.substring(1);
        for (Simbolo simbolo: simbolos) {
            if (simbolo.getAmbito().substring((simbolo.getAmbito().lastIndexOf(".")+1)).equals(nombreFuncion)) //Si la ultima parte del ambito es la funcion
                System.out.println(" nombre parametro: "+simbolo.getLexema());
                if (simbolo.getUso().equals("parametro declaracion")) //Y si el uso del simbolo es parametro
                    parametros.add(simbolo);
        }
        return parametros;
    }

    //elimina simbolo
    public static void eliminarSimbolo(String lexema) {
        //simbolos.remove(lexema);
    }

    //imprime los simbolos
    public static void imprimirTabla() {
        System.out.println("\nTabla de Simbolos:" + " (longitud: "+simbolos.size()+")");
        for (Simbolo simbolo: simbolos) {
            System.out.println(simbolo.getLexema() + ": ID " + simbolo.getId() + ": Tipo " + simbolo.getTipo() + " Uso " + simbolo.getUso()+ " Ambito " + simbolo.getAmbito());
        }
        System.out.println("");
    }

    public static ArrayList<Simbolo> obtenerTablaDeSimbolos() {
        return simbolos;
    }
}
