package compilador;


public class ConstantesCompilador {


    public static final int NO_ENCONTRADO = -1;
    public static final char BLANCO = ' ';
    public static final char TAB = '\t';
    public static final char SALTO_LINEA = '\n';
    public static final int DIGITO = 0;
    public static final char MINUSCULA = 'a';
    public static final char MAYUSCULA = 'A';
    public static final int CANTIDAD_ESTADOS = 15;
    public static final int CANTIDAD_CARACTERES = 30;
    public static final String FUNC_TYPE = "funcion";
    
    public static final String RUTA_MATRIZ_ESTADOS = "/archivos/matriz_de_transicion_de_estados.txt";
    public static final String RUTA_SIMBOLOS_ASCII = "/archivos/caracteres_ASCII.txt";
    public static final String RUTA_MATRIZ_ACCIONES = "/archivos/matriz_de_acciones_semanticas.txt";
    public static final String RUTA_DE_PALABRAS_RESERVADAS = "/archivos/lista_de_palabras_reservadas.txt";
    
    public static final int EN_LECTURA = 0;
    public static final int ERROR = -1;
    public static final int LONGITUD_MAXIMA_ID = 15;
    public static final int ID = 257;
    public static final int CONSTANTE = 277;
    public static final int DOUBLE = 276;
    public static final int CADENA = 278;
    
    public static final int ETIQUETA = 279;
    
    public static final int WARNING = -2;
    public static final String LEXICO = "Se ha encontrado un error lexico ";
    public static final String SINTACTICO = "Se ha encontrado un error sintactico ";
    public static final String SEMANTICO = "Se ha entonctrado un error semantico";
    public static final int MAX_INT_POSITIVO = 32767;
    public static final Simbolo SIMBOLO_NO_ENCONTRADO= new Simbolo("no_encontrado.no",-1);
    public static final int ESTADO_FINAL = -2;
    public static final int PALABRA_NO_ENCONTRADA = -1;

}