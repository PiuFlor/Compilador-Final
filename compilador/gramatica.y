%{
package compilador;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

%}

%token ID IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET WHILE PAIR GOTO
    ASIG MAYOR_IGUAL MENOR_IGUAL DIST INTEGER DOUBLE CTE CADENA ETIQUETA


%left '+' '-'
%left '*' '/'

%start program

%%

//Gramatica del lenguaje

program: programa
    ;

programa: nombre_programa BEGIN bloque_sentencias_programa END {System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el programa " +  $1.sval);}
     | nombre_programa BEGIN END error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Se esperaban sentencias de ejecucion");}
        | nombre_programa BEGIN bloque_sentencias_programa error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de delimitador de programa: END");}
        | nombre_programa bloque_sentencias_programa END error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de delimitador de programa: BEGIN");}
        | BEGIN bloque_sentencias_programa END error {agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un nombre de programa");}
        | nombre_programa error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error sintactico al compilar no permite terminar de leer el programa de forma correcta");}                                                              
     ;

nombre_programa: ID 
    ;

bloque_sentencias_programa: bloque_sentencias_programa sentencia_programa
    |sentencia_programa
    ;


sentencia_programa: sentencias_declarativas 
    |sentencias_ejecutables
    |etiqueta
    ;

sentencias_declarativas: declaracion_variable ';'
    | declaracion_tipo_subrango ';' //T11
    | declaracion_tipo_pair ';' //T21
    | declaracion_funcion ';'
    | declaracion_variable error {agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion de variable");}
    | declaracion_tipo_subrango error {agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion de sub-tipo");}
    | declaracion_tipo_pair error {agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion del tipo pair");}
    | declaracion_funcion {agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion de la funcion");}
    ;

declaracion_variable: tipo list_var {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una declaracion de variable");}
    ;


declaracion_funcion: tipo FUN ID '(' parametro ')' BEGIN cuerpo_funcion END {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la declaracion de funcion con su valor de retorno");}
             | tipo FUN error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de nombre en la función");}
             | tipo FUN ID '('  ')' error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parametro");}
             | tipo FUN ID  BEGIN cuerpo_funcion END error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parametro");}
             |  FUN ID '(' parametro ')' BEGIN cuerpo_funcion END {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta tipo");}
             | tipo FUN ID '(' parametro ',' parametro ')' error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "error en la cantidad de parametros");}//creo esta mal
             | tipo  ID '(' parametro ')' BEGIN cuerpo_funcion END {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta palabra reservada FUN");}
     ;

parametro: tipo ID {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio parametro");} 
         | tipo error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Se espera un identificador luego del tipo");}
         | error ID { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de tipo de parámetro formal en declaración de función"); }
         ;

   
cuerpo_funcion: bloque_sentencias_programa sentencia_retorno
    | bloque_sentencias_programa error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de sentencia RET en la función");}
    |sentencia_retorno
    ;

sentencia_retorno: RET '(' expr_aritmetic ')' ';' {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de retorno");}
   | RET error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Debe especificar el valor a retornar");}
   | '(' expr_aritmetic ')'{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta RET");}
   | RET '(' expr_aritmetic ')' error  {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el ';' despues del Retorno");}
   | RET expr_aritmetic error{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis");} 
   | RET '(' expr_aritmetic error{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis para cerrar");} 
   | RET expr_aritmetic ')' error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis para abrir");} 
   | RET '(' error ')' ';' {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Retorno vacio");}
    ;

invocacion_funcion: ID '(' expr_aritmetic ')'  { System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el llamado a la funcion " + $1.sval );}

    
list_var: ID  
         | ID ',' list_var   
         | ID list_var error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de ',' en la declaracion de variables");}
        ;
        
tipo: INTEGER
    | DOUBLE
    ; 


sentencias_ejecutables: sentencia_asignacion ';'
		      | sentencia_seleccion ';'
		      | sentencia_out ';'
              | sentencia_while ';' //T13
              | sentencia_goto ';' //T23
		      | sentencia_asignacion error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia asignacion falta ';' ");}
		      | sentencia_seleccion error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia seleccion falta ';' ");}
		      | sentencia_out error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia out falta ';' ");}
              | sentencia_while error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia while falta ';' ");}
              | sentencia_goto error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia goto falta ';' ");}
		      ;

 //Asignacion T18
sentencia_asignacion: list_var ASIG list_expresion {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la asignacion ");}
    | list_var ASIG error{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la expresion de la asignacion");}
    | var_pair ASIG list_expresion {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la asignacion ");}
     ;

list_expresion: expr_aritmetic
    | var_pair
	| list_expresion ',' expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio lista multiple");}
	| list_expresion ' ' expr_aritmetic error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error, Falta ','");}
    ;


    // Sentencia IF-THEN-ELSE 
sentencia_seleccion:  IF '(' condicion ')' THEN bloque_sentencias_del ELSE bloque_sentencias_del END_IF  {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de control IF");}
    | IF '(' condicion ')' THEN bloque_sentencias_del END_IF {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de control IF");}
    | IF condicion THEN bloque_sentencias_del END_IF {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de paréntesis en condición de selección"); }
    | IF '(' condicion ')' THEN bloque_sentencias_del error {  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de END_IF"); }
    | IF '(' condicion ')' THEN error END_IF { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque THEN"); }
    | IF '(' condicion ')' THEN bloque_sentencias_del ELSE error END_IF { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque ELSE"); }
    ;

bloque_sentencias_del: sentencias_ejecutables
    | BEGIN bloque_ejecutable END 
    | BEGIN error END { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque"); }
    | error bloque_ejecutable END { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el BEGIN"); }
    | BEGIN bloque_ejecutable error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el END"); }
    ;

bloque_ejecutable: bloque_ejecutable sentencias_ejecutables
    | sentencias_ejecutables
    | sentencias_declarativas { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "No se permiten sentencias declarativas"); }
    ;

condicion: expr_aritmetic comparador expr_aritmetic
    | expr_aritmetic comparador error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de expresion aritmetica en comparación"); }
    | comparador expr_aritmetic error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de expresion aritmetica en comparación"); }
    | expr_aritmetic error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de comparador en expresion"); }
    ;

comparador: '='          {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por igual");}
          | DIST            {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por distinto");}
          | '>'           {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por mayor");}
          | '<'           {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por menor");}
          | MAYOR_IGUAL     {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por mayor igual");}
          | MENOR_IGUAL     {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por menor igual");}
    ;


sentencia_out: OUTF '(' CADENA ')'   {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia OUTF");}
      | OUTF '(' expr_aritmetic ')'  {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia OUTF");}
      | OUTF '(' ')'  error {  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parámetro en sentencia OUTF"); }
      | OUTF '(' error  { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Parámetro incorrecto en sentencia OUTF");}
      | OUTF '(' CADENA error {  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el parentesis de cierre"); }
      | OUTF CADENA ')' error{  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el parentesis de apertura"); }
    ;


//TEMA 11 
declaracion_tipo_subrango: TYPEDEF ID ASIG tipo rango {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una declaracion tipo_subrango");}
     | TYPEDEF error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta nombre del tipo definido"); }
     | TYPEDEF ID ASIG error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta tipo base"); } 
    ;

rango: '{' const ',' const '}' 
       | error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de {} en el rango");}
    ;

//TEMA 13 (WHILE)
sentencia_while: WHILE '(' condicion ')'  bloque_sentencias_del   {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia  WHILE");}
            | WHILE  condicion   bloque_sentencias_del  { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de parentesis en la iteración"); }
            | error '(' condicion ')'  bloque_sentencias_del   { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta WHILE"); }
             ;


//TEMA 21 (PAIR)
declaracion_tipo_pair: TYPEDEF PAIR '<'tipo'>' ID  {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio PAIR");}
   | TYPEDEF error '<' tipo '>' ID { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta PAIR"); }
   | TYPEDEF PAIR  tipo  ID  { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta '<>' en la declaración de PAIR"); }
   | TYPEDEF PAIR '<' tipo '>' error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta identificador al final de la declaración"); }
   | TYPEDEF PAIR '<' error '>' ID  { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta tipo en la declaración de PAIR"); }
   ;

//TEMA 23
sentencia_goto: llamado_etiqueta {System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia GOTO");}
    ;

llamado_etiqueta: GOTO etiqueta{System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia GOTO");}
                | GOTO error  { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta etiqueta en la sentencia GOTO"); }
                ;
 
etiqueta: ETIQUETA {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la etiqueta " + $1.sval);}
      ;

var_pair: ID '{' CTE '}' //T21
;  


expr_aritmetic: expr_aritmetic '+' termino 
              | expr_aritmetic '-' termino      
              | error '+' termino  {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");}
              | expr_aritmetic '+' error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");}
              | error '-' termino {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión." );}
              | expr_aritmetic '-' error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");}
              | termino
              ;

termino: termino '*' factor 
       | termino '/' factor 
       | factor
       | invocacion_funcion
       ;

factor: ID 
      | const 
      ;

const: CTE { System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + $1.sval + " CTE positiva");
     ConstantePositiva($1.sval);
     $$.sval = $1.sval;}
     | '-' CTE { System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + $1.sval + $2.sval + " CTE negativa");
     ConstanteNegativa($2.sval);
     $$.sval = ('-' + $2.sval);}
     ;
     

//FIN GRAMATICA
%%

//Funciones
public static ArrayList<String> errores = new ArrayList<String>();
public static  List<String> warnings = new ArrayList<>();
public static ArrayList<String> erroreslex = new ArrayList<String>();
public static  List<String> warningslex = new ArrayList<>();

int yylex() throws IOException {

        int identificador_token = 0;
        Reader lector = compilador.AnalizadorLexico.lector;
        compilador.AnalizadorLexico.estado_actual = 0;
        while (! EOF(lector)) {
          char caracter = nextChar(lector);
          identificador_token = AnalizadorLexico.proximoEstado(lector, caracter);
          if (identificador_token != ConstantesCompilador.EN_LECTURA) {
            yylval = new ParserVal(AnalizadorLexico.getTokenActual());
            AnalizadorLexico.token_actual.delete(0, AnalizadorLexico.token_actual.length());
            return identificador_token;
          }
        }
        return identificador_token;
}

 public static char nextChar(Reader reader) throws IOException {
        reader.mark(1);
        char next_char = (char) reader.read();
        reader.reset();
        return next_char;
      }

      
      private boolean EOF(Reader lector) throws IOException {
        lector.mark(1);
        int value = lector.read();
        lector.reset();
        return value == -1;
      }

public static void agregarError(int tipo, String clase, String error)
      {
        if ( tipo == ConstantesCompilador.ERROR && clase == ConstantesCompilador.SINTACTICO)
          errores.add("Linea: "+ AnalizadorLexico.getLineaActual()+ " (ERROR) " + clase + ": "+ error);
        else
          warnings.add("Linea: "+AnalizadorLexico.getLineaActual()+ " (WARNING) "+ clase + ": "+ error);

      }
     
public static void agregarErrorLex(int tipo, String clase, String error)
      {
        if ( tipo == ConstantesCompilador.ERROR && clase == ConstantesCompilador.LEXICO)
          erroreslex.add("Linea: "+ AnalizadorLexico.getLineaActual()+ " (ERROR) " + clase + ": "+ error);
        else
          warningslex.add("Linea: "+AnalizadorLexico.getLineaActual()+ " (WARNING) "+ clase + ": "+ error);

      }

public static void imprimirErrores()
{   System.out.println("Errores Lexicos: ");
    if ( erroreslex.size() > 0)
        {
            System.out.println("No se ha podido compilar el programa debido a los siguientes errores: ");
            for (String error : erroreslex)
                System.err.println(error);
        } else {
            System.out.println("Se ha terminado de compilar correctamente.");
        }
            if ( warningslex.size() > 0)
        {
            for (String Warning : warningslex)
                System.out.println(Warning);
        }
     

    System.out.println("Errores Sintacticos: ");
    if ( errores.size() > 0)
        {
            System.out.println("No se ha podido compilar el programa debido a los siguientes errores: ");
            for (String error : errores)
                System.err.println(error);
        } else {
            System.out.println("Se ha terminado de compilar correctamente.");
        }
            if ( warnings.size() > 0)
        {
            for (String Warning : warnings)
                System.out.println(Warning);
        }
}


public static void ConstanteNegativa(String lexema){

    if(lexema.contains("x")){
      String sinPrefijo = lexema.startsWith("-0x") 
                            ? lexema.substring(3) 
                            : lexema.startsWith("0x") 
                            ? lexema.substring(2) 
                            : lexema;
       lexema = sinPrefijo;
        if (Integer.parseInt(lexema,16) > ConstantesCompilador.MAX_INT_POSITIVO+1) {
             Parser.agregarErrorLex(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "Constante Hexadecimal negativa entera fuera del rango permitido " );
             lexema = "0x" + Integer.toHexString(ConstantesCompilador.MAX_INT_POSITIVO+1).toUpperCase();
             
             
             
           }
      }else{
     if(! lexema.contains(".")) {
         if (Integer.parseInt(lexema) > ConstantesCompilador.MAX_INT_POSITIVO+1) {
             Parser.agregarErrorLex(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "Constante negativa entera fuera del rango permitido " );
             lexema = String.valueOf(ConstantesCompilador.MAX_INT_POSITIVO+1);
             
           }
     } }
     
     TablaDeSimbolos.obtenerSimbolo(lexema).setLexema("-" + lexema);
 }

public static void ConstantePositiva(String lexema){
    if(lexema.contains("x")){
      String sinPrefijo = lexema.startsWith("-0x") 
                            ? lexema.substring(3) 
                            : lexema.startsWith("0x") 
                            ? lexema.substring(2) 
                            : lexema;
       lexema = sinPrefijo;
       if (Integer.parseInt(lexema,16) > ConstantesCompilador.MAX_INT_POSITIVO) {
            Parser.agregarErrorLex(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "Constante Hexadecimal positiva entera fuera del rango permitido " );
            lexema = "0x" + Integer.toHexString(ConstantesCompilador.MAX_INT_POSITIVO+1).toUpperCase();
            TablaDeSimbolos.obtenerSimbolo(lexema).setLexema("0x7FFF");
          }
      }else{
    if(! lexema.contains(".")) {
        if (Integer.parseInt(lexema) > ConstantesCompilador.MAX_INT_POSITIVO) {
            Parser.agregarErrorLex(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "Constante positiva entera fuera del rango permitido " );
            lexema = String.valueOf(ConstantesCompilador.MAX_INT_POSITIVO+1);
            TablaDeSimbolos.obtenerSimbolo(lexema).setLexema(String.valueOf(ConstantesCompilador.MAX_INT_POSITIVO));
          }
      }}
   
}


void yyerror(String error) {
    // funcion utilizada para imprimir errores que produce yacc
    System.out.println("Yacc reporto un error: " + error);
}
