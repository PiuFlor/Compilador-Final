package acciones_semanticas;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import compilador.PalabrasReservadas;
import compilador.Parser;
import compilador.AnalizadorLexico;
import compilador.ConstantesCompilador;
import compilador.TablaDeSimbolos;

/*AS3: Verifica si es una palabra reservada o verifica y/o agrega en la tabla de simbolos, si   
       supera la longitud máxima se trunca, y devuelve el token.*/
public class AS3 implements AccionSemantica {   
    private static final String FILE_PATH = "Archivos/TokensGenerados.txt";
    @Override
    public int run(Reader lector, StringBuilder token) throws IOException{
        String lexema = token.toString();
        int IdToken = PalabrasReservadas.obtenerTokenId(lexema);
        if (IdToken != -1){ //Si es palabra reservada
            System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ", se reconocio la palabra reservada: " + lexema + " codigo: " + IdToken);
            
            FileWriter writer = new FileWriter(FILE_PATH, true); 
            writer.write("Palabra Rervada: " + lexema + " - LINEA: " + AnalizadorLexico.getLineaActual() +"\n");
            writer.close();  
            
            return IdToken;
        } else { //Si es identificador
            IdToken = TablaDeSimbolos.obtenerSimbolo(lexema).getId(); //Si ya esta creado se lo devuelve
                      
            if (IdToken == -1) {
                if (lexema.length() > ConstantesCompilador.LONGITUD_MAXIMA_ID) {//Checkeo de longitud maxima de ID
                    lexema = lexema.substring(0, ConstantesCompilador.LONGITUD_MAXIMA_ID);
                    Parser.agregarError(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "El identificador es demasido largo, se lo corto a la longitud maxima de "+ ConstantesCompilador.LONGITUD_MAXIMA_ID+" caracteres.");
                }
                TablaDeSimbolos.agregarSimbolo(lexema, ConstantesCompilador.ID); //Si no se lo crea
            }

            System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ", se reconocio el identificador: " + lexema + " codigo: " + ConstantesCompilador.ID);
            FileWriter writer = new FileWriter(FILE_PATH, true); 
            writer.write("Identificador: " + lexema + " - LINEA: " + AnalizadorLexico.getLineaActual() +"\n");
            writer.close();
            
        }
        return ConstantesCompilador.ID;
     }
}   