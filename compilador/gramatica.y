%{
package compilador;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import GeneracionCodigoIntermedio.ArbolSintactico;
import GeneracionCodigoIntermedio.NodoComun;
import GeneracionCodigoIntermedio.NodoControl;
import GeneracionCodigoIntermedio.NodoHoja;
import GeneracionCodigoIntermedio.NodoMultipleAsignacion;

import java.util.Collections;

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

programa: nombre_programa BEGIN bloque_sentencias_programa END {//System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el programa " +  $1.sval);
                               if (!lista_etiquetas.isEmpty()){
                                  String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: No existe la etiqueta";
                                  erroresSemanticos.add(err);
                               }
                               if ($3.obj instanceof ArbolSintactico) {
                                    ArbolSintactico arbAux = (ArbolSintactico) $3.obj;
                                    buscarErroresEnNodo(arbAux);
                                    generarArbol(arbAux);
                                }} 
        | nombre_programa BEGIN END error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Se esperaban sentencias de ejecucion");}
        | nombre_programa BEGIN bloque_sentencias_programa error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de delimitador de programa: END");}
        | nombre_programa bloque_sentencias_programa END error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de delimitador de programa: BEGIN");}
        | BEGIN bloque_sentencias_programa END error {agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un nombre de programa");}
        | nombre_programa error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error sintactico al compilar no permite terminar de leer el programa de forma correcta");}                                                              
     ;

nombre_programa: ID 
    ;

bloque_sentencias_programa: bloque_sentencias_programa sentencia_programa { 
                      if ($1.obj instanceof ArbolSintactico && $2.obj instanceof ArbolSintactico ){
                           ArbolSintactico Cuerpo_sen1 = (ArbolSintactico) $2.obj; 
                           ArbolSintactico Cuerpo_bloqueI = (ArbolSintactico) $1.obj;
                           $$=new ParserVal(new NodoComun("SentenciaProg",Cuerpo_bloqueI , Cuerpo_sen1));
                     }else
                        if ($1.obj instanceof ArbolSintactico){
                           ArbolSintactico Cuerpo_bloqueI = (ArbolSintactico) $1.obj;
                           $$=$1;
                       }else
                          if ($2.obj instanceof ArbolSintactico){
                           ArbolSintactico Cuerpo_bloqueI = (ArbolSintactico) $2.obj;
                           $$=$2;}
           }
    |sentencia_programa {$$ = $1;}
    ;


sentencia_programa: sentencias_declarativas 
    |sentencias_ejecutables { if ($1.obj instanceof ArbolSintactico) {
                                    ArbolSintactico arbAux = (ArbolSintactico) $1.obj;
                                    
                                    $$=new ParserVal(arbAux);
                                }}
    |etiqueta{ if (lista_etiquetas.contains($1.sval)){
                       lista_etiquetas.remove($1.sval);
                       ArbolSintactico arbAux = (ArbolSintactico) $1;
                       NodoControl nodo= new NodoControl("ETIQUETA_SALTO",arbAux);
                       $$=new ParserVal(nodo);
               }else{
                  String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: No existe el salto";
                  erroresSemanticos.add(err);
                  $$ = new ParserVal( nodoError);
               }         
   }
    ;

sentencias_declarativas: declaracion_variable ';'
    | declaracion_especial ';'
    | declaracion_tipo_subrango ';' //T11
    | declaracion_tipo_pair ';' //T21
    | declaracion_funcion ';' 
    | declaracion_variable error {agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion de variable");}
    | declaracion_tipo_subrango error {agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion de sub-tipo");}
    | declaracion_tipo_pair error {agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion del tipo pair");}
    | declaracion_funcion {agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion de la funcion");}
    ;

declaracion_variable: tipo list_var {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una declaracion de variable");
                                                    for(String s : lista_identificadores){
                                                       if(!TablaDeSimbolos.existeSimboloAmbitoActual(s + ":"+ambitoActual)){
                                                            Simbolo sim = TablaDeSimbolos.obtenerSimbolo(s);
                                                            sim.setTipo($1.sval);
                                                            sim.setLexema(s+":"+ambitoActual);
                                                            sim.setAmbito(ambitoActual);                                                                                                                 
                                                            sim.setUso("identificador");
                                                            sim.setValorAsignado(true);      
                                                        }else{
                                                            String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable re declarada en el mismo ambito";
                                                            erroresSemanticos.add(err);
                                                        }
                                                    }
                                                    lista_identificadores.clear();
                                                }
    ;


declaracion_funcion: encabezado_fun '(' parametro ')' BEGIN cuerpo_funcion END {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la declaracion de funcion con su valor de retorno");                                              
                       String ambitoInicial = ambitoActual;
                       if ($6.obj instanceof ArbolSintactico) {
                                    ArbolSintactico arbAux = (ArbolSintactico) $6.obj;
                                    buscarErroresEnNodo(arbAux);
                                    NodoComun arb = new NodoComun("Cuerpo_Fun",(ArbolSintactico)$1,(ArbolSintactico)  $6.obj);
                                    NodoControl arbol= new NodoControl("Funcion",arb);
                                    agregarMetodoLista(arbol);
                                    generarArbolFunc((ArbolSintactico) arb);
                                    
                                  //  arbAux.recorrerArbol("-");
                                } 
                          int indice = ambitoActual.lastIndexOf(':');
if (indice != -1) {
    ambitoActual = ambitoActual.substring(0, indice);
} else {
    // Manejo seguro si no hay ':' en ambitoActual
    ambitoActual = ambitoInicial; // O un valor predeterminado para el ámbito global
}
              
                                      
}                                              
             
             | encabezado_fun '('  ')' error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parametro");}
             | encabezado_fun  BEGIN cuerpo_funcion END error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parametro");}
             | encabezado_fun '(' parametro ',' parametro ')' error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "error en la cantidad de parametros");}//creo esta mal
             ;
 
 encabezado_fun: tipo FUN ID {
                      if(!TablaDeSimbolos.existeSimboloAmbitoActual($3.sval + ":"+ambitoActual)){
                        Simbolo simboloFuncion = TablaDeSimbolos.obtenerSimbolo($3.sval);
                        simboloFuncion.setTipo($1.sval);
                        simboloFuncion.setUso("funcion");
                        simboloFuncion.setLexema($3.sval+":"+ambitoActual);                        
                        simboloFuncion.setAmbito(ambitoActual);
                        ambitoActual = ambitoActual + ":" + $3.sval ;                       
                        lista_id_func.add($3.sval);
                        //agregarArbol($3.sval);
                        $$ = new NodoHoja($3.sval);
                      }else{
                          String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Identificador funcion re declarado en el mismo ambito";
                          erroresSemanticos.add(err);
                          $$ = nodoError;
                           }  
                        }
                | tipo FUN error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de nombre en la función"); $$ = nodoError;}
                |  FUN ID  {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta tipo"); $$ = nodoError;}
                | tipo  ID {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta palabra reservada FUN"); $$ = nodoError;}
     ;

parametro: tipo ID {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio parametro");
                        Simbolo simboloFuncion = TablaDeSimbolos.obtenerSimbolo($2.sval);
                        simboloFuncion.setTipo($1.sval);
                        simboloFuncion.setLexema($2.sval+":"+ambitoActual);
                        simboloFuncion.setUso("Parametro");
                        simboloFuncion.setAmbito(ambitoActual);
} 
         | tipo error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Se espera un identificador luego del tipo");}
         | error ID { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de tipo de parámetro formal en declaración de función"); }
         ;


cuerpo_funcion:bloque_sentencias_programa sentencia_retorno { ArbolSintactico Cuerpo_ret = (ArbolSintactico) $2.obj; 
                           ArbolSintactico Cuerpo_fun = (ArbolSintactico) $1.obj;
                          
                           NodoComun cuerpo_completo = new NodoComun("Sentencias_fun",Cuerpo_fun , Cuerpo_ret);
                           $$=new ParserVal(cuerpo_completo);
           }
    | bloque_sentencias_programa sentencia_retorno bloque_sentencias_programa   {
                          ArbolSintactico Cuerpo_ret = (ArbolSintactico) $2.obj; 
                           ArbolSintactico Cuerpo_fun1 = (ArbolSintactico) $1.obj;
                          ArbolSintactico Cuerpo_fun2 = (ArbolSintactico) $3.obj;
                          NodoComun cuerpo = new NodoComun("Sentencias_fun",Cuerpo_fun1 , Cuerpo_ret);
                          NodoComun cuerpo_completo = new NodoComun("Sentencias_fun",cuerpo , Cuerpo_fun2);
                          $$=new ParserVal(cuerpo_completo);
    }     
    | bloque_sentencias_programa error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de sentencia RET en la función"); $$ =new ParserVal(nodoError);}
    |sentencia_retorno {$$ = $1;}
    ;

sentencia_retorno: RET '(' expr_aritmetic ')' ';' {
                           // System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de retorno");
                                ArbolSintactico arb = (ArbolSintactico) $3; 
                               $$ = new ParserVal(new NodoComun($1.sval, arb,null));  }                     
                           
   | RET error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Debe especificar el valor a retornar");$$ =new ParserVal(nodoError);}
   | '(' expr_aritmetic ')'{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta RET");$$ =new ParserVal(nodoError);}
   | RET '(' expr_aritmetic ')' error  {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el ';' despues del Retorno");$$ =new ParserVal(nodoError);}
   | RET expr_aritmetic error{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis");$$ =new ParserVal(nodoError);} 
   | RET '(' expr_aritmetic error{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis para cerrar");$$ =new ParserVal(nodoError);} 
   | RET expr_aritmetic ')' error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis para abrir");$$ =new ParserVal(nodoError);} 
   | RET '(' error ')' ';' {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Retorno vacio");}
   ;

invocacion_funcion: ID '(' expr_aritmetic ')'  {// System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el llamado a la funcion " + $1.sval );
         // System.out.println("invocacion func" + $1.sval + (TablaDeSimbolos.existeSimboloAmbitoActual($1.sval +":"+ ambitoActual)));
          if (lista_id_func.contains($1.sval) && (TablaDeSimbolos.existeSimboloAmbitoActual($1.sval +":"+ ambitoActual)) ){    
           Simbolo s = TablaDeSimbolos.obtenerParametro(ambitoActual+":"+$1.sval);        
           ArbolSintactico exprArbol = (ArbolSintactico)$3;
           Simbolo simbol = TablaDeSimbolos.obtenerSimbolo( exprArbol.getLex()+":"+ ambitoActual );
           String tipoExp;
           if (simbol != null && simbol.getId() != -1){
              tipoExp = TablaDeSimbolos.obtenerSimbolo( exprArbol.getLex()+":"+ ambitoActual ).getTipo();  
              //System.out.println("tipoExp" + exprArbol.getLex()+":"+ ambitoActual + "TIPO" + tipoExp ); 
           }else{
                          
                          tipoExp = TablaDeSimbolos.obtenerSimbolo( exprArbol.getLex()).getTipo();  
                         // System.out.println("tipoExp" + exprArbol.getLex()+":"+ ambitoActual + "TIPO" + tipoExp ); 
               
           }          
           if(s.getTipo().equals(tipoExp)){                 
             NodoHoja id_func = new NodoHoja($1.sval);
             ArbolSintactico arb2 = (ArbolSintactico) $3;             
             $$ = new NodoComun("ejecucion funcion", id_func, arb2);  
             }else{
                String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico:Tipo Parametro formal distinto Parametro Real " ;
              erroresSemanticos.add(err);
              $$ = (NodoComun) nodoError;
              }
         }else{
          if(lista_id_func.contains($1.sval)){
              String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: funcion fuera de ambito" ;
              erroresSemanticos.add(err);
              $$ =  nodoError;
          }else{
              String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: funcion no declarada" ;
              erroresSemanticos.add(err);
              $$ =  nodoError;
           }
          $$ =  nodoError;
        } }  
    
list_var: ID {lista_identificadores.add($1.sval); 
                $$ = (ArbolSintactico) new NodoHoja($1.sval);
                } 
         | ID ',' list_var {lista_identificadores.add($1.sval); 
                $$ = (ArbolSintactico) new NodoHoja($1.sval);
                }  
         | ID list_var error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de ',' en la declaracion de variables");}
        ;
        
tipo: INTEGER {$$ = $1;}
    | DOUBLE  {$$ = $1;}
    ; 


sentencias_ejecutables: sentencia_asignacion ';' {$$ = $1; }
		      | sentencia_seleccion ';'{$$ = $1; }
		      | sentencia_out ';'{$$ = $1; }
              | sentencia_while ';' //T13{$$ = $1; }
              | sentencia_goto ';' //T23{$$ = $1; }
		      | sentencia_asignacion error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia asignacion falta ';' "); 
		       $$ = (ArbolSintactico) nodoError;}
		      | sentencia_seleccion error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia seleccion falta ';' ");
		      $$ = (ArbolSintactico) nodoError;
                                                }
		      | sentencia_out error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia out falta ';' ");
		      $$ = (ArbolSintactico) nodoError;
                                                }
              | sentencia_while error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia while falta ';' ");
              $$ = (ArbolSintactico) nodoError;
                                                }
              | sentencia_goto error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia goto falta ';' ");
              $$ = (ArbolSintactico) nodoError;
                                                }
		      ;

 //Asignacion T18
sentencia_asignacion: list_var ASIG list_expresion { //System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ", Se reconoció la asignación múltiple."); 
       List<ArbolSintactico> arbolesAsignacion = new ArrayList<>(); 
       int numVars = lista_identificadores.size(); 
       int numExprs = lista_expresiones.size(); 
       // Supone que list_expresion devuelve una lista de ArbolSintactico  
       // Asegurar que hay al menos tantas expresiones como variables 
       int minSize = Math.min(numVars, numExprs); 
       Collections.reverse(lista_identificadores);    
       // Ajusta el orden si es necesario 
       for (int i = 0; i < minSize; i++) {           
         String varLexema = lista_identificadores.get(i) + ":" + ambitoActual;
         //System.out.println("dato" + varLexema);
         Simbolo simbol;
        if(!TablaDeSimbolos.existeSimboloAmbitoActual($1.sval +":"+ambitoActual)) {
           simbol = TablaDeSimbolos.obtenerSimbolodentrodelAmbito(varLexema);
        }else{
          simbol = TablaDeSimbolos.obtenerSimbolo(varLexema); 
        }
         
         if (simbol != null && simbol.getId() != -1) { 
          // Obtener el tipo de la variable y el tipo de la expresión 
          String tipoVar = simbol.getTipo();
           String[] sub = tipoVar.split(":"); 
           String tip = sub[0];
          if( lista_sub.contains(tip)){
               tipoVar = simbol.getTipoParametro();
          }  
          ArbolSintactico exprArbol = lista_expresiones.get(i); 
          String tipoExpr ; 
          if(exprArbol.getLex().equals("IndicePair")){
                tipoExpr = TablaDeSimbolos.obtenerSimbolo(exprArbol.getIzq().getLex()).getTipoParametro();                
               
            }else{
                 Simbolo a = TablaDeSimbolos.obtenerSimbolo(exprArbol.getLex() +":" + ambitoActual);
                 if (a.getUso().equals("identificador") || a.getUso().equals("PAIR")){
                         tipoExpr = TablaDeSimbolos.obtenerSimbolo(exprArbol.getLex()+":" + ambitoActual ).getTipo(); 
                 }else{
                     if (exprArbol.getLex().equals("ejecucion funcion")){
                       tipoExpr = TablaDeSimbolos.obtenerSimbolo(exprArbol.getIzq().getLex()+":" + ambitoActual ).getTipo();
                     }else{
                       tipoExpr = TablaDeSimbolos.obtenerSimbolo(exprArbol.getLex() ).getTipo(); 
                     }
                 }
            }
           if (exprArbol.getLex().equals("+") || 
                             exprArbol.getLex().equals("-") || 
                            exprArbol.getLex().equals("*") || 
                            exprArbol.getLex().equals("/")){
                           tipoExpr = TablaDeSimbolos.obtenerSimbolo(exprArbol.getIzq().getLex()).getTipo();
                            
                            }
        //  System.out.println("tipo izq:" + tipoVar + " " + "tipo der:" + tipoExpr); 
          if (tipoVar.equals(tipoExpr)) { 
         //  System.out.println("tipo var" + tipoVar);
            NodoHoja hoja = new NodoHoja(simbol.getLexema()); 
            arbolesAsignacion.add(new NodoComun(":=", hoja, exprArbol)); 
          } else { 
            if (tipoVar.equals("DOUBLE") && !lista_pair.contains(tipoExpr)) { 
              Simbolo exp = TablaDeSimbolos.obtenerSimbolo(exprArbol.getLex()); 
              //exp.setTipo("DOUBLE"); 
              NodoHoja hoja = new NodoHoja(simbol.getLexema()); 
              arbolesAsignacion.add(new NodoComun(":=", hoja, new NodoControl("CONVERSION", exprArbol))); 
              //System.out.println("cambio de tipo"); 
              } else {
                 if(lista_pair.contains(tipoExpr) || lista_sub.contains(tipoExpr)){ 
        						 String Subtipo = TablaDeSimbolos.obtenerSimbolo(exprArbol.getLex()).getTipoParametro(); 
        						 if (tipoVar.equals(Subtipo)) { 
           						 NodoHoja hoja = new NodoHoja(simbol.getLexema()); 
           						 arbolesAsignacion.add(new NodoComun(":=", hoja, exprArbol)); }
           						 else{
           						 erroresSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semántico: Incompatibilidad de tipo en la asignación de " + lista_identificadores.get(i) + " := " + exprArbol.getLex());
                  arbolesAsignacion.add(nodoError); 
                  $$ = new ParserVal(nodoError);
           						 }
        		}else{				
                    erroresSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semántico: Incompatibilidad de tipo en la asignación de " + lista_identificadores.get(i) + " := " + exprArbol.getLex());
                    arbolesAsignacion.add(nodoError); 
                    $$ = new ParserVal(nodoError);
                } 
                } 
            } } else {
                   erroresSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semántico: La variable " + varLexema + " no existe.");
                   $$ = new ParserVal(nodoError);
                    } } 

        if (numVars > numExprs) {
            for (int i = numExprs; i < numVars; i++) {
                String varLexema = lista_identificadores.get(i) + ":" + ambitoActual;
                Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(varLexema);
                if (simbol != null && simbol.getId() != -1) {
                  if (simbol.getTipo().equals("INTEGER")||simbol.getTipoParametro().equals("INTEGER")){
                    NodoHoja hoja = new NodoHoja(simbol.getLexema());
                    NodoHoja cero = new NodoHoja("0");
                    TablaDeSimbolos.agregarSimbolo(String.valueOf(0), ConstantesCompilador.CONSTANTE);
                    TablaDeSimbolos.obtenerSimbolo(String.valueOf(0)).setTipo("INTEGER");
                    TablaDeSimbolos.obtenerSimbolo(String.valueOf(0)).setUso("constante");
                    arbolesAsignacion.add(new NodoComun(":=", hoja, cero));
                  }else{
                    NodoHoja hoja = new NodoHoja(simbol.getLexema());
                    NodoHoja cero = new NodoHoja("0.0");
                    TablaDeSimbolos.agregarSimbolo(String.valueOf(0.0), ConstantesCompilador.CONSTANTE);
                    TablaDeSimbolos.obtenerSimbolo(String.valueOf(0.0)).setTipo("DOUBLE");
                    TablaDeSimbolos.obtenerSimbolo(String.valueOf(0.0)).setUso("constante");
                    arbolesAsignacion.add(new NodoComun(":=", hoja, cero));  
                  }
                   // System.out.println("Warning: Variable " + varLexema + " asignada a 0.");
                     warningsSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + ". Warning Semántico: Variable " + varLexema + " asignada a 0");
                   
                }
            }
        } else if (numExprs > numVars) {
           // System.out.println("Warning: Se descartaron expresiones adicionales.");
            warningsSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + ". Warning Semántico: Se descartaron expresiones adicionales.");
        }

        // Construir el nodo para la asignación múltiple
        $$ = new ParserVal(new NodoMultipleAsignacion("AsignacionM",arbolesAsignacion));
        lista_identificadores.clear();
        lista_expresiones.clear(); 
    }

    | list_var ASIG error{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la expresion de la asignacion");}
    | var_pair ASIG list_expresion {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la asignacion ");
        List<ArbolSintactico> arbolesAsignacion = new ArrayList<>();
        
        int numVars = lista_identificadores.size();
        int numExprs = lista_expresiones.size(); 
        int minSize = Math.min(numVars, numExprs);
        Collections.reverse(lista_identificadores);
        for (int i = 0; i < minSize; i++) {
            String varLexema = lista_identificadores.get(i) + ":" + ambitoActual;
            Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(varLexema);
            
            if (simbol != null && simbol.getId() != -1) {
                // Obtener el tipo de la variable y el tipo de la expresión
                String tipoVar = simbol.getTipo();
                String tipoVarSub = simbol.getTipoParametro();
                ArbolSintactico exprArbol = lista_expresiones.get(i);
                String tipoExpr = TablaDeSimbolos.obtenerSimbolo(exprArbol.getLex()).getTipo();
                if (tipoVar.equals(tipoExpr) || tipoVarSub.equals(tipoExpr)) {
                    // Si los tipos coinciden, se agrega el nodo de asignación al árbol
                    NodoHoja hoja = new NodoHoja(simbol.getLexema());
                    arbolesAsignacion.add(new NodoComun(":=",(ArbolSintactico) $1 , exprArbol));
                } else {
                    // Si los tipos no coinciden, registra un error semántico
                    if(tipoVarSub.equals("DOUBLE")){
                        Simbolo exp = TablaDeSimbolos.obtenerSimbolo(exprArbol.getLex());
                       // exp.setTipo("DOUBLE");
                        NodoHoja hoja = new NodoHoja(simbol.getLexema());
                        arbolesAsignacion.add(new NodoComun(":=", hoja, new NodoControl("CONVERSION", exprArbol)));
                        //System.out.println("cambio de tipo");
                    }else{
                    erroresSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + 
                                          ". Error Semántico: Incompatibilidad de tipo en la asignación de " + 
                                          lista_identificadores.get(i) + " := " + exprArbol.getLex());
                    arbolesAsignacion.add(nodoError);  // Agrega un nodo de error en el árbol
                    $$ = new ParserVal(nodoError);
                }}
            } else {
                erroresSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + 
                                      ". Error Semántico: La variable " + varLexema + " no existe.");
                                      $$ = new ParserVal(nodoError);
            }
        }

        // Agregar nodos de valor cero o descartar sobrantes con warning
        if (numVars > numExprs) {
            for (int i = numExprs; i < numVars; i++) {
                String varLexema = lista_identificadores.get(i) + ":" + ambitoActual;
                Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(varLexema);
                if (simbol != null && simbol.getId() != -1) {
                    NodoHoja hoja = new NodoHoja(simbol.getLexema());
                    NodoHoja cero = new NodoHoja("0");
                    arbolesAsignacion.add(new NodoComun(":=", hoja, cero));
                   // System.out.println("Warning: Variable " + varLexema + " asignada a 0.");
                    warningsSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + ". Warning Semántico: Variable " + varLexema + " asignada a 0.");
                }
            }
        } else if (numExprs > numVars) {
           // System.out.println("Warning: Se descartaron expresiones adicionales.");
            warningsSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + ". Warning Semántico: Se descartaron expresiones adicionales.");
        }

        // Construir el nodo para la asignación múltiple
        $$ = new ParserVal(new NodoMultipleAsignacion("AsignacionM",arbolesAsignacion));
        lista_identificadores.clear();
        lista_expresiones.clear(); 
    }
     ;

list_expresion: expr_aritmetic {
                lista_expresiones.add((ArbolSintactico) $1); 
                $$ = $1;                
                } 
    | var_pair {
    lista_identificadores.remove(lista_identificadores.size() - 1);
    lista_expresiones.add((ArbolSintactico) $1);
             $$ = $1;}
	| list_expresion ',' expr_aritmetic {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio lista multiple");
	            lista_expresiones.add((ArbolSintactico)$3); 
                $$ = $3;
                }
	| list_expresion ' ' expr_aritmetic error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error, Falta ','");
          $$ = (ArbolSintactico) nodoError;}
    ;


    // Sentencia IF-THEN-ELSE 
sentencia_seleccion:  IF '(' condicion ')' THEN bloque_sentencias_del ELSE bloque_sentencias_del END_IF  {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de control IF");
                                    if((ArbolSintactico)$3 != nodoError){
                                        ArbolSintactico nodoThen = (ArbolSintactico) $6.obj;
                                        NodoControl then = new NodoControl("then",nodoThen); //Bloque del THEN
                                        ArbolSintactico nodoElse = (ArbolSintactico) $8.obj;
                                        NodoControl _else = new NodoControl("else", nodoElse); //Bloque del ELSE
                                        NodoComun cuerpo = new NodoComun("cuerpo", then, _else);
                                        ArbolSintactico nodocond = (ArbolSintactico) $3;
                                        NodoControl condicion = new NodoControl("Condicion",nodocond);
                                      //  $$ = (ArbolSintactico) new NodoComun($1.sval, condicion , cuerpo); 
                                        $$ = new ParserVal(new NodoComun($1.sval, condicion , cuerpo));
                                    }else{
                                        $$ = $3;
                                    }                                                                                                          
                                }

    | IF '(' condicion ')' THEN bloque_sentencias_del END_IF {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de control IF");

                                    if((ArbolSintactico)$3 != nodoError){          
                                        ArbolSintactico nodoThen = (ArbolSintactico) $6.obj; 
                                        ArbolSintactico nodoCon = (ArbolSintactico) $3;                                                         
                                        NodoControl then = new NodoControl("then",nodoThen);
                                        NodoControl condicion = new NodoControl("Condicion", nodoCon);
                                        $$ = new ParserVal(new NodoComun($1.sval, condicion , then));                                                      
                                    }else{
                                        $$ = $3;
                                    }
                                }
    
    | IF condicion THEN bloque_sentencias_del END_IF {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de paréntesis en condición de selección");   $$ = (ArbolSintactico) nodoError; }
    | IF '(' condicion ')' THEN bloque_sentencias_del error {  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de END_IF");   $$ = (ArbolSintactico) nodoError; }
    | IF '(' condicion ')' THEN error END_IF { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque THEN");   $$ = (ArbolSintactico) nodoError;}
    | IF '(' condicion ')' THEN bloque_sentencias_del ELSE error END_IF { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque ELSE");  $$ = (ArbolSintactico) nodoError; }
    ;

bloque_sentencias_del: sentencias_ejecutables { $$=$1;}
    | BEGIN bloque_ejecutable END { $$ = $2;}
    | BEGIN error END { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque");$$ = new ParserVal(nodoError); }
    | error bloque_ejecutable END { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el BEGIN"); $$ = new ParserVal(nodoError);}
    | BEGIN bloque_ejecutable error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el END"); $$ = new ParserVal(nodoError);}
    ;

bloque_ejecutable: bloque_ejecutable sentencias_ejecutables { ArbolSintactico Cuerpo_bloqueD = (ArbolSintactico) $2.obj; 
                           ArbolSintactico Cuerpo_bloqueI = (ArbolSintactico) $1.obj;
                           $$=new ParserVal(new NodoComun("Sentencia_Dentro_IF",Cuerpo_bloqueI , Cuerpo_bloqueD));
           }
    | sentencias_ejecutables { $$ = $1; }
    | sentencias_declarativas { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "No se permiten sentencias declarativas");$$ = new ParserVal(nodoError); }
    ;

condicion: expr_aritmetic comparador expr_aritmetic {
    ArbolSintactico arbIzq = (ArbolSintactico) $1;
    ArbolSintactico arbDer = (ArbolSintactico) $3;
    Simbolo simbolo1;
    Simbolo simbolo2;
    if (!arbIzq.getLex().equals("+") && 
                            ! arbIzq.getLex().equals("-") && 
                            !arbIzq.getLex().equals("*") &&  
                           ! arbIzq.getLex().equals("/")){
    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
    }else{
        simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
    }
    if(!arbDer.getLex().equals("+") && 
                            ! arbDer.getLex().equals("-") && 
                            !arbDer.getLex().equals("*") &&  
                           ! arbDer.getLex().equals("/") ){
      simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
    }else{
       simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
      }

        if (arbIzq != nodoError && arbDer != nodoError) {
            if (simbolo1.getId() != -1 && simbolo2.getId() != -1) {
                if (chequearTipos(simbolo1) && chequearTipos(simbolo2)) {
                    $$ = new NodoComun($2.sval, arbIzq, arbDer); // Se agrega el comparador al árbol
                } else {
                    agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error Semantico: El tipo de la comparación es distinto.");
                    $$ = nodoError;
                }
            } else {
                agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Lado izquierdo o derecho de la comparación incorrecto.");
                $$ = nodoError;
            }
        } else {
            agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de expresión aritmética en comparación.");
            $$ = nodoError;
        }
    }   
    | expr_aritmetic comparador error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de expresion aritmetica en comparación"); 
    $$ = nodoError;}
    | comparador expr_aritmetic error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de expresion aritmetica en comparación"); 
    $$ = nodoError;}
    
    | expr_aritmetic error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de comparador en expresion"); 
    $$ = nodoError;}
    
    ;

comparador: '='          {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por igual");
                            $$.sval = "=";}
          | DIST            {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por distinto");
                          $$.sval = "DIST";}
          | '>'           {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por mayor");
                         $$.sval = ">";}
          | '<'           {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por menor"); 
          					$$.sval = "<";}
          | MAYOR_IGUAL     {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por mayor igual");
          					 $$.sval = "MAYOR_IGUAL";}
          | MENOR_IGUAL     {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por menor igual");
          					 $$.sval = "MENOR_IGUAL";}
    ;


sentencia_out: OUTF '(' CADENA ')'   {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia OUTF");
                                        Simbolo CAD = TablaDeSimbolos.obtenerSimbolo($3.sval);
                                        CAD.setUso("cadena");
                                        NodoHoja cadena = new NodoHoja($3.sval);                                        
                                        NodoControl bloque = new NodoControl("OUTF",(ArbolSintactico) cadena); 
                                      
                                        $$ = new ParserVal(bloque);
}
      | OUTF '(' expr_aritmetic ')' {// {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia OUTF");
       
                                        ArbolSintactico cadena = (ArbolSintactico)$3;                                        
                                        NodoControl bloque = new NodoControl("OUTF",cadena); 
                                      
                                        $$ = new ParserVal(bloque);
      }
      | OUTF '(' ')'  error {  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parámetro en sentencia OUTF");$$ = new ParserVal(nodoError); }
      | OUTF '(' error  { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Parámetro incorrecto en sentencia OUTF");$$ = new ParserVal(nodoError);}
      | OUTF '(' CADENA error {  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el parentesis de cierre"); $$ = new ParserVal(nodoError);}
      | OUTF CADENA ')' error{  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el parentesis de apertura");$$ = new ParserVal(nodoError); }
    ;


//TEMA 11 
declaracion_tipo_subrango: TYPEDEF ID ASIG tipo rango {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una declaracion tipo_subrango");
                      if(!TablaDeSimbolos.existeSimboloAmbitoActual($2.sval + ":"+ambitoActual)){
                        Simbolo simboloFuncion = TablaDeSimbolos.obtenerSimbolo($2.sval);
                        simboloFuncion.setTipo($4.sval);
                        simboloFuncion.setUso("SUBTIPO");
                        simboloFuncion.setLexema($2.sval+":"+ambitoActual);                        
                        simboloFuncion.setAmbito(ambitoActual);  
                        simboloFuncion.setRango1(rango1);
                        simboloFuncion.setRango2(rango2);                    
                        lista_sub.add($2.sval);
                      }else{
                          String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Identificador re declarado en el mismo ambito";
                          erroresSemanticos.add(err);
                           }  
}
     | TYPEDEF error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta nombre del tipo definido"); }
     | TYPEDEF ID ASIG error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta tipo base"); } 
    ;

rango: '{' const ',' const '}' {
        NodoHoja a = (NodoHoja) $2 ;
        rango1 = Integer.parseInt(a.getLex()) ;
        a = (NodoHoja) $4 ;
        rango2 = Integer.parseInt(a.getLex());
        if (rango1 >= rango2){
         String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: LimiteInf mayor que LimiteSup";
         erroresSemanticos.add(err);
        }
       }
       | error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de {} en el rango");}
    ;

//TEMA 13 (WHILE)
sentencia_while: WHILE '(' condicion ')'  bloque_sentencias_del   {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia  WHILE");
                   if((ArbolSintactico)$3 != nodoError){
                                        ArbolSintactico nodobloque = (ArbolSintactico) $5.obj;
                                        NodoControl bloque = new NodoControl("sentencia",nodobloque); //Bloque del THEN
                                        NodoComun cuerpo = new NodoComun("cuerpo", bloque,null);
                                        ArbolSintactico nodocond = (ArbolSintactico) $3;
                                        NodoControl condicion = new NodoControl("Condicion",nodocond);
                                      
                                        $$ = new ParserVal(new NodoComun($1.sval, condicion , cuerpo));
                                    }else{
                                        $$ = $3;
                                    }         
                  }
            | WHILE  condicion   bloque_sentencias_del  { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de parentesis en la iteración");$$ = new ParserVal(nodoError); }
            | error '(' condicion ')'  bloque_sentencias_del   { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta WHILE");$$ = new ParserVal(nodoError); }
             ;


//TEMA 21 (PAIR)
declaracion_tipo_pair: TYPEDEF PAIR '<'tipo'>' ID  {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio PAIR");
                      if(!TablaDeSimbolos.existeSimboloAmbitoActual($6.sval + ":"+ambitoActual)){
                        Simbolo simboloFuncion = TablaDeSimbolos.obtenerSimbolo($6.sval);
                        simboloFuncion.setTipo($4.sval);
                        simboloFuncion.setUso("PAIR");
                        simboloFuncion.setLexema($6.sval+":"+ambitoActual);                        
                        simboloFuncion.setAmbito(ambitoActual);                                          
                        lista_pair.add($6.sval);
                      }else{
                          String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Identificador re declarado en el mismo ambito";
                          erroresSemanticos.add(err);
                           }  
}
   | TYPEDEF error '<' tipo '>' ID { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta PAIR"); }
   | TYPEDEF PAIR  tipo  ID  { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta '<>' en la declaración de PAIR"); }
   | TYPEDEF PAIR '<' tipo '>' error { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta identificador al final de la declaración"); }
   | TYPEDEF PAIR '<' error '>' ID  { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta tipo en la declaración de PAIR"); }
   ;

//TEMA 23
sentencia_goto: llamado_etiqueta //{System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia GOTO");$$=$1;}
    ;

llamado_etiqueta: GOTO etiqueta{//System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia GOTO");                                                                    
                                      lista_etiquetas.add($2.sval);
                                      NodoHoja etiqueta = new NodoHoja($2.sval);                                        
                                      NodoControl bloque = new NodoControl($1.sval,(ArbolSintactico) $2); 
                                      
                                        $$ = new ParserVal(bloque);}
                | GOTO error  { agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta etiqueta en la sentencia GOTO");$$ = new ParserVal(nodoError); }
                ;
 
etiqueta: ETIQUETA {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la etiqueta " + $1.sval);
                     NodoHoja etiqueta = new NodoHoja($1.sval);
                    $$=etiqueta;
                }
      ;

var_pair: ID '{' CTE '}' {
    String indice1 = "1";
    String indice2 = "2";
    if (indice1.equals($3.sval) || indice2.equals($3.sval)){
         lista_identificadores.add($1.sval);
        
         NodoHoja izq = new NodoHoja($1.sval + ":"+ ambitoActual);
         NodoHoja Der = new NodoHoja($3.sval);
         $$ = new NodoComun("IndicePair",izq,Der);
       //  $$ = new NodoHoja($1.sval);
    }else{
         String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Indice Pair fuera de rango ";
         erroresSemanticos.add(err);
         $$ = nodoError;
     }
} //T21
;  

declaracion_especial: ID list_var {//System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una declaracion de variable");
                                                   if (lista_pair.contains($1.sval) || lista_sub.contains($1.sval)){                                                   
                                                    for(String s : lista_identificadores){
                                                       if(!TablaDeSimbolos.existeSimboloAmbitoActual(s + ":"+ambitoActual)){
                                                            Simbolo sim = TablaDeSimbolos.obtenerSimbolo(s);
                                                            sim.setTipo($1.sval+ ":"+ ambitoActual);
                                                            sim.setLexema(s+":"+ambitoActual);
                                                            sim.setAmbito(ambitoActual);                                                                                                                 
                                                            sim.setUso("identificador");
                                                            sim.setValorAsignado(true); 
                                                            Simbolo simEspecial = TablaDeSimbolos.obtenerSimbolo($1.sval + ":"+ ambitoActual);  
                                                            sim.setTipoParametro(simEspecial.getTipo()); 
                                                            sim.setRango1(simEspecial.getRango1());
                                                            sim.setRango2(simEspecial.getRango2());
                                                           
                                                        }else{
                                                            String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable re declarada en el mismo ambito";
                                                            erroresSemanticos.add(err);
                                                        }
                                                     }
                                                    }else{
                                                            String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Tipo no declarado ";
                                                            erroresSemanticos.add(err);
                                                    }
                                                    lista_identificadores.clear();
                                                }
                 ;
                   


expr_aritmetic: expr_aritmetic '+' termino {
    ArbolSintactico arbIzq = (ArbolSintactico) $1;
    ArbolSintactico arbDer = (ArbolSintactico) $3;
    
    Simbolo simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
    Simbolo simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
    //System.out.println("simbolo izq:" + simboloIzq.getUso());
    //System.out.println("simbolo der:" + simboloDer.getUso());
    if(simboloIzq.getUso().equals("constante") && simboloDer.getUso().equals("constante")){
       if(simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("INTEGER")){
              $$ = new NodoComun("+", arbIzq, arbDer);
       }else{
          if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("DOUBLE")){
             $$ = new NodoComun("+", arbIzq, arbDer);
          }else{
              if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")){
                      $$ = new NodoComun("+", arbIzq, new NodoControl("CONVERSION", arbDer));
              }else{
                     $$ = new NodoComun("+",new NodoControl("CONVERSION", arbIzq),arbDer);
              }
          }
      }     
     }else{ simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex()+":"+ambitoActual);
            simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex()+":"+ambitoActual);
         if (simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("DOUBLE")) {
                    //simboloIzq.setTipo("DOUBLE");
                    $$ = new NodoComun("+",new NodoControl("CONVERSION", arbIzq) , arbDer);
           } else if (simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")) {
                 //simboloDer.setTipo("DOUBLE");
                 $$ = new NodoComun("+", arbIzq,new NodoControl("CONVERSION", arbDer));
           } else if (simboloIzq.getTipo().equals(simboloDer.getTipo())) {
               $$ = new NodoComun("+", arbIzq, arbDer);
       }}
}
              | expr_aritmetic '-' termino {
ArbolSintactico arbIzq = (ArbolSintactico) $1;
    ArbolSintactico arbDer = (ArbolSintactico) $3;
    
    Simbolo simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
    Simbolo simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
    if(simboloIzq.getUso().equals("constante") && simboloDer.getUso().equals("constante")){
       if(simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("INTEGER")){
              $$ = new NodoComun("-", arbIzq, arbDer);
       }else{
          if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("DOUBLE")){
             $$ = new NodoComun("-", arbIzq, arbDer);
          }else{
              if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")){
                      $$ = new NodoComun("-", arbIzq, new NodoControl("CONVERSION", arbDer));
              }else{
                     $$ = new NodoComun("-",new NodoControl("CONVERSION", arbIzq),arbDer);
              }
          }
      }      
     }else{ simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex()+":"+ambitoActual);
            simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex()+":"+ambitoActual);
     
         if (simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("DOUBLE")) {
                    //System.out.println("EXP_ARIT:se modifico tipo lado izq");
                   // simboloIzq.setTipo("DOUBLE");
                    $$ = new NodoComun("-", new NodoControl("CONVERSION", arbIzq), arbDer);
           } else if (simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")) {
              // System.out.println("EXP_ARIT:se modifico tipo lado der");
                 simboloDer.setTipo("DOUBLE");
                 $$ = new NodoComun("-", arbIzq, new NodoControl("CONVERSION", arbDer));
           } else if (simboloIzq.getTipo().equals(simboloDer.getTipo())) {
               //System.out.println("EXP_ARIT:NO SE MODIFICO NINGUN TIPO");
               $$ = new NodoComun("-", arbIzq, arbDer);
       }}

}
              | error '+' termino  {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");$$ = nodoError;}
              | expr_aritmetic '+' error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");$$ = nodoError;}
              | error '-' termino {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión." );$$ = nodoError;}
              | expr_aritmetic '-' error {agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");$$ = nodoError;}
              | termino { $$ = $1;}
              ;

termino: termino '*' factor {
   ArbolSintactico arbIzq = (ArbolSintactico) $1;
    ArbolSintactico arbDer = (ArbolSintactico) $3;
    
    Simbolo simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
    Simbolo simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
    //System.out.println("simbolo izq:" + simboloIzq.getUso());
    //System.out.println("simbolo der:" + simboloDer.getUso());
    if(simboloIzq.getUso().equals("constante") && simboloDer.getUso().equals("constante")){
       if(simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("INTEGER")){
              $$ = new NodoComun("*", arbIzq, arbDer);
       }else{
          if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("DOUBLE")){
             $$ = new NodoComun("*", arbIzq, arbDer);
          }else{
              if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")){
                      $$ = new NodoComun("*", arbIzq, new NodoControl("CONVERSION", arbDer));
              }else{
                     $$ = new NodoComun("*",new NodoControl("CONVERSION", arbIzq),arbDer);
              }
          }
      }      
     }else{ simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex()+":"+ambitoActual);
            simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex()+":"+ambitoActual);
     
         if (simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("DOUBLE")) {
                    //System.out.println("EXP_ARIT:se modifico tipo lado izq");
                   // simboloIzq.setTipo("DOUBLE");
                    $$ = new NodoComun("*", new NodoControl("CONVERSION", arbIzq), arbDer);
           } else if (simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")) {
                    //System.out.println("EXP_ARIT:se modifico tipo lado der");
                    //simboloDer.setTipo("DOUBLE");
                 $$ = new NodoComun("*", arbIzq, new NodoControl("CONVERSION", arbDer));
           } else if (simboloIzq.getTipo().equals(simboloDer.getTipo())) {
                    //System.out.println("EXP_ARIT:NO SE MODIFICO NINGUN TIPO");
                    $$ = new NodoComun("*", arbIzq, arbDer);
       }}
    

}
   
       | termino '/' factor {
    ArbolSintactico arbIzq = (ArbolSintactico) $1;
    ArbolSintactico arbDer = (ArbolSintactico) $3;
    
    Simbolo simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
    Simbolo simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
   // System.out.println("simbolo izq:" + simboloIzq.getUso());
    //System.out.println("simbolo der:" + simboloDer.getUso());
    if(simboloIzq.getUso().equals("constante") && simboloDer.getUso().equals("constante")){
       if(simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("INTEGER")){
              $$ = new NodoComun("/", arbIzq, arbDer);
       }else{
          if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("DOUBLE")){
             $$ = new NodoComun("/", arbIzq, arbDer);
          }else{
              if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")){
                      $$ = new NodoComun("/", arbIzq, new NodoControl("CONVERSION", arbDer));
              }else{
                     $$ = new NodoComun("/",new NodoControl("CONVERSION", arbIzq),arbDer);
              }
          }
      }      
     }else{ simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex()+":"+ambitoActual);
            simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex()+":"+ambitoActual);
     
         if (simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("DOUBLE")) {
                    //System.out.println("EXP_ARIT:se modifico tipo lado izq");
                   // simboloIzq.setTipo("DOUBLE");
                    $$ = new NodoComun("/", new NodoControl("CONVERSION", arbIzq), arbDer);
           } else if (simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")) {
               //System.out.println("EXP_ARIT:se modifico tipo lado der");
               //  simboloDer.setTipo("DOUBLE");
                 $$ = new NodoComun("/", arbIzq, new NodoControl("CONVERSION", arbDer));
           } else if (simboloIzq.getTipo().equals(simboloDer.getTipo())) {
               //System.out.println("EXP_ARIT:NO SE MODIFICO NINGUN TIPO");
               $$ = new NodoComun("/", arbIzq, arbDer);
       }}
    

}
       | factor { $$ = $1;} 
       | invocacion_funcion {$$ =  $1;}
       ;

factor: ID {  if(TablaDeSimbolos.existeSimboloAmbitoActual($1.sval +":"+ambitoActual)) {             
                $$ = (ArbolSintactico) new NodoHoja($1.sval+":"+ambitoActual);
                }else {
                   Simbolo simbol = TablaDeSimbolos.obtenerSimbolodentrodelAmbito($1.sval+":"+ambitoActual);
                   if (TablaDeSimbolos.existeSimboloAmbitoActual(simbol.getLexema())){
                     $$ = (ArbolSintactico) new NodoHoja(simbol.getLexema());
                   }else{
                   String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable no declarada en el ambito:" + ambitoActual ;
                   erroresSemanticos.add(err);
                   $$ = (ArbolSintactico) nodoError;
                   }
        }
                }
      | const { $$ = $1;}
      ;

const: CTE { //System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + $1.sval + " CTE positiva");
  // if (($1.sval).contains(".")){ }
     if (!$1.sval.contains(String.valueOf(".")))
        ConstantePositiva($1.sval);
     $$ = (ArbolSintactico) new NodoHoja(String.valueOf($1.sval)); // padre ------- $1
            if(auxTipoAsig.equals("")){
                //Esto me sirve para resolver la comparacion del parametro de una funcion
                auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).getTipo();
            } }
     | '-' CTE { //System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + $1.sval + $2.sval + " CTE negativa");
   // if (($2.sval).contains(".")){$2.sval = String.valueOf(PasarAFloatJava($2.sval));}
     if (!$1.sval.contains(String.valueOf(".")))
     ConstanteNegativa($2.sval);
     $$ = (ArbolSintactico) new NodoHoja("-" + String.valueOf($2.sval)); // padre ------- $1
        if(auxTipoAsig.equals("")){
            auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).getTipo();
        }}

     ;

     

//FIN GRAMATICA
%%

//Funciones
public static ArrayList<String> errores = new ArrayList<String>();
public static  List<String> warnings = new ArrayList<>();
public static ArrayList<String> erroreslex = new ArrayList<String>();
public static  List<String> warningslex = new ArrayList<>();

public static ArrayList<String> erroresSemanticos = new ArrayList<String>();
public static  List<String> warningsSemanticos = new ArrayList<>();

private static ArrayList<String> lista_identificadores = new ArrayList<String>();
private static ArrayList<ArbolSintactico> lista_expresiones = new ArrayList<ArbolSintactico>();
private static ArrayList<String> lista_pair = new ArrayList<String>();
private static ArrayList<String> lista_sub = new ArrayList<String>();
private static ArrayList<String> lista_etiquetas = new ArrayList<String>();

private static ArrayList<NodoControl> lista_func = new ArrayList<NodoControl>();

public static ArrayList<String> lista_id_func = new ArrayList<String>();

private static NodoControl raiz;
private static NodoComun ptrRaizPrograma;

private static NodoComun ptrRaizFuncion;
private static NodoComun aux_raiz_func;

private static String ambitoActual = "global";

private static int rango1 = 0;
private static int rango2 = 0;

public static boolean tieneErrores = false;
private static final NodoComun nodoError = new NodoComun("ERROR", null,null);

public static String auxTipoAsig = "";
public static String auxTipoComp = "";

public static boolean chequearTipos(Simbolo s1){
    if(s1.getId() != -1){
        if(!auxTipoComp.equals("")){
            //es  una asignacion
            if(s1.getTipo().equals(auxTipoComp) || s1.getTipoParametro().equals(auxTipoComp)){
                return true;
            }else{
                String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable " + s1.getLexema() + " con tipo incorrecto";
                erroresSemanticos.add(err);
                return false; 
            }
        }else{
            //es una expr_aritmetic suelta, por ejemplo en una COMPARACION
            if (s1.getTipo().equals("INTEGER") || s1.getTipo().equals("DOUBLE")){
                      auxTipoComp = s1.getTipo();
            }else{
                     auxTipoComp = s1.getTipoParametro();
            }
            return true;
        }
    }else{
        return true;
    }
}

/*public static boolean chequearTipos(Simbolo s1){
    return true;
}*/

public static void generarArbol(ArbolSintactico arbl){
    if(raiz == null){
        //Si la raiz no existe la creo
        NodoComun sentencia = new NodoComun("SentenciaArbol",null,null);
        NodoControl program = new NodoControl("Programa", sentencia);
        raiz = program;
        ptrRaizPrograma = sentencia;
    }
    if(ptrRaizPrograma.getHijoIzq() == null){
        //Si el nodo izquierdo es null, agrego la nueva sentencia ahi
        ptrRaizPrograma.setHijoIzq(arbl);

    }else{
        //si el nodo izq ya esta ocupado, creo un nuevo NodoControl "Sentencia" y añado la sentencia nueva en el nodo izq
        NodoComun nuevo = new NodoComun("SentenciaArbol", arbl,null);
        ptrRaizPrograma.setHijoDer(nuevo);
        ptrRaizPrograma = nuevo; //seteo puntero;
    }
}

public static void generarArbolFunc(ArbolSintactico arbl){
    if(aux_raiz_func == null){
        //Si la raiz no existe la creo
        NodoComun sentencia = new NodoComun("SentenciaArbolFun",null,null);
        aux_raiz_func = sentencia;
        ptrRaizFuncion = sentencia;
    }
    if(ptrRaizFuncion.getHijoIzq() == null){
        //Si el nodo izquierdo es null, agrego la nueva sentencia ahi
        ptrRaizFuncion.setHijoIzq(arbl);
    }else{
        //si el nodo izq ya esta ocupado, creo un nuevo NodoControl "Sentencia" y añado la sentencia nueva en el nodo izq
        NodoComun nuevo = new NodoComun("SentenciaArbolFun", arbl,null);
        ptrRaizFuncion.setHijoDer(nuevo);
        ptrRaizFuncion = nuevo; //seteo puntero;
    }
}




public static void buscarErroresEnNodo(ArbolSintactico arb){
    if(!tieneErrores){
        if(arb == nodoError){
            tieneErrores = true;
        }if(arb.getDer() != null) {
            buscarErroresEnNodo(arb.getDer());
        }
        if(arb.getIzq() != null) {
            buscarErroresEnNodo(arb.getIzq());
        }
    }
}

public static void agregarMetodoLista(NodoControl raiz){
    lista_func.add(raiz);
}

public static void agregarArbol(String nombreFunc){
    NodoControl func = new NodoControl(nombreFunc,aux_raiz_func); 
    aux_raiz_func = null;
    ptrRaizFuncion = null;
    lista_func.add(func);
}

public static ArrayList<NodoControl> get_arboles(){
    ArrayList<NodoControl> aux = new ArrayList<NodoControl>();
    for(NodoControl n : lista_func){
        aux.add(n);
    }
    //lista_func.clear();
    return aux;
}


public static NodoControl getRaiz(){
	return raiz;
}


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
            if ( warningsSemanticos.size() > 0)
        {
            for (String Warning : warnings)
                System.out.println(Warning);
        }

        System.out.println("Errores Semanticos: ");
    if ( erroresSemanticos.size() > 0)
        {
            System.out.println("No se ha podido compilar el programa debido a los siguientes errores: ");
            for (String error : erroresSemanticos)
                System.err.println(error);
        } else {
            System.out.println("Se ha terminado de compilar correctamente.");
        }
      if ( warningsSemanticos.size() > 0)
        {
            for (String Warning : warningsSemanticos)
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
             TablaDeSimbolos.obtenerSimbolo(lexema).setLexema("-" + lexema);
    }}
    
    Simbolo s = TablaDeSimbolos.obtenerSimbolo(lexema);
        String tipo = s.getTipo();
        TablaDeSimbolos.agregarSimbolo('-'+lexema, ConstantesCompilador.CONSTANTE);
        TablaDeSimbolos.obtenerSimbolo('-'+lexema).setUso("constante");
        TablaDeSimbolos.obtenerSimbolo('-'+lexema).setTipo(tipo);


   /*    if(!lexema.contains(".")){

        TablaDeSimbolos.agregarSimbolo('-'+lexema, ConstantesCompilador.CONSTANTE);
        TablaDeSimbolos.obtenerSimbolo('-'+lexema).setUso("constante");
        TablaDeSimbolos.obtenerSimbolo('-'+lexema).setTipo("INTEGER");

        //Constantes.tokens.put(lexemanegativo, Constantes.CTE);
    }else{
        TablaDeSimbolos.agregarSimbolo('-'+lexema, ConstantesCompilador.CONSTANTE);
        TablaDeSimbolos.obtenerSimbolo('-'+lexema).setUso("constante");
        TablaDeSimbolos.obtenerSimbolo('-'+lexema).setTipo("DOUBLE");
     //   Constantes.tokens.put(lexemanegativo, Constantes.CTE);

    } */
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
          }else

          if (Integer.parseInt(lexema) > ConstantesCompilador.MAX_INT_POSITIVO-1) {
            Parser.agregarError(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "El numero es demasiado grande. Se lo ha reemplazado por el mas cercano posible: " + ConstantesCompilador.MAX_INT_POSITIVO);
            lexema = String.valueOf(ConstantesCompilador.MAX_INT_POSITIVO-1);
          }
 
}
 }





void yyerror(String error) {
    // funcion utilizada para imprimir errores que produce yacc
    System.out.println("Yacc reporto un error: " + error);
}