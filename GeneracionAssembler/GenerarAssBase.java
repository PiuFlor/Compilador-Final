package GeneracionAssembler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import GeneracionCodigoIntermedio.ArbolSintactico;
import GeneracionCodigoIntermedio.NodoControl;
import compilador.AnalizadorLexico;
import compilador.Parser;
import compilador.Simbolo;
import compilador.TablaDeSimbolos;


public class GenerarAssBase {

	ArbolSintactico arbol;
	String contenidoTabla, assemblerMain, codigo, AssemblerFuncs;
    private static final String FILE_PATH = "GeneracionAssembler/Test/output1.asm";

	
	public GenerarAssBase(ArbolSintactico arb) throws IOException {
		this.arbol = arb;
		contenidoTabla = "";
		assemblerMain = arbol.getAssembler(); 
		codigo = "";
		AssemblerFuncs = generarAssemblerFuncs();
		generarDataTabla();
	}
	public GenerarAssBase() throws IOException {
		contenidoTabla = "";
		codigo = "";
		generarDataTabla();
	}
	
	private void generarDataTabla(){
		ArrayList<Simbolo> tabla = TablaDeSimbolos.obtenerTablaDeSimbolos();
		
		contenidoTabla += "$errorDivisionPorCero db \" Error Assembler:No se puede realizar la division por cero\" , 0 \n";  
		contenidoTabla += "$errorOverflowSumaDouble db \" Error Assembler: overflow en la suma de flotantes \", 0 \n";
		contenidoTabla += "$errorOverRangoSubtipoMenor db \" Error Assembler: valor menor a rango minimo \", 0 \n";
		contenidoTabla += "$errorOverRangoSubtipoMayor db \" Error Assembler: valor mayor a rango superior \", 0 \n";
		contenidoTabla += "$constMaximoDouble dq 1.7976931348623157E308 \n";
		contenidoTabla += "$auxIntCompIzq dw ? \n";
		contenidoTabla += "$auxIntCompDer dw ? \n";
		contenidoTabla += "$auxDoubleCompIzq dq ? \n";
		contenidoTabla += "$auxDoubleCompDer dq ? \n";
		contenidoTabla += "auxCadena db 50 dup(?) \n"; //para imprimir expresiones
		contenidoTabla += "msg db 'Resultado: %d' , 0  \n";
		contenidoTabla += "msg2 db 'Resultado: %d.%02d' , 0 \n";
		contenidoTabla += "buffer db 64 dup (?) \n";
		contenidoTabla += "$partEnt dd ? \n";
		contenidoTabla += "$partDec dd ? \n";
		contenidoTabla += "fpu_cw WORD ? \n";
		contenidoTabla += "factor dq 100.0 \n";
		
		
	
		
		for (Simbolo simbolo : TablaDeSimbolos.obtenerTablaDeSimbolos()) {			
            String valor = "";

            switch(simbolo.getUso()) {
            	case "constante": 
            		switch(simbolo.getTipo()) {
            		case "INTEGER":
                        if (!simbolo.getLexema().contains("x")) {
            			valor = simbolo.getLexema(); //me quedo con el simbolo sin el sufijo
            			contenidoTabla += "$"+simbolo.getLexema().replace(".","_").replace("+","$").replace("-","$") + " dw " + valor + "\n";
                        }else {
                        	String resultado = simbolo.getLexema();
                        	String sinPrefijo = resultado.startsWith("-0x") 
                                    ? resultado.substring(3) 
                                    : resultado.startsWith("0x") 
                                    ? resultado.substring(2) 
                                    : resultado;
              
                           // Convierte la cadena hexadecimal a un entero
                            int valorinteger = Integer.parseInt(sinPrefijo, 16);
                            valor = String.valueOf(valorinteger);
                            contenidoTabla += "$"+simbolo.getLexema().replace(".","_").replace("+","$").replace("-","$") + " dw " + valor + "\n";
                        }
    	            	break;
            		case "DOUBLE":
            			
            			valor = simbolo.getLexema();
            			contenidoTabla += "$"+simbolo.getLexema().replace(".","_").replace("+","$").replace("-","$") + " dq " + valor + "\n";
    	            	break;            		
            		}
            		break;
            	case "identificador":
            		switch(simbolo.getTipo()) {
            		
            		case "INTEGER":
            			if(!simbolo.getValor().isEmpty()) {
	            			valor = simbolo.getValor(); //me quedo con el simbolo sin el sufijo
	            			contenidoTabla += "$"+simbolo.getLexema().replace(":", "$").replace(".","_").replace("+","$").replace("-","$") + " dw "+ valor+ "\n";
            			}else {
            				contenidoTabla += "$"+simbolo.getLexema().replace(":", "$").replace(".","_").replace("+","$").replace("-","$") + " dw ? \n";
                		}
	            		break;
            		case "DOUBLE":
            			if(!simbolo.getValor().isEmpty()) {
	    	            	valor = simbolo.getValor();
	            			contenidoTabla += "$"+simbolo.getLexema().replace(":", "$").replace(".","_").replace("+","$").replace("-","$") + " dq "+valor +" \n";
            			}else {
            				contenidoTabla += "$"+simbolo.getLexema().replace(":", "$").replace(".","_").replace("+","$").replace("-","$") + " dq ? \n";
                		}
	            		break;
            		default:
            			if (!simbolo.getTipo().isEmpty()) {
            				Simbolo sim = TablaDeSimbolos.obtenerSimbolo(simbolo.getTipo());          				
            				if(sim.getUso().equals("PAIR") ) {
            					if (sim.getTipo().equals("INTEGER")) {
            						if(!simbolo.getValor().isEmpty()) {
            	            			valor = simbolo.getValor(); //me quedo con el simbolo sin el sufijo
            	            			contenidoTabla += "$"+simbolo.getLexema().replace(":", "$").replace(".","_").replace("+","$").replace("-","$") + " dw "+ valor+ "\n";
                        			}else {
                        				contenidoTabla += "$"+simbolo.getLexema().replace(":", "$").replace(".","_").replace("+","$").replace("-","$") + " dw 0, 0 \n";
                            		}          						
            					}else if (sim.getTipo().equals("DOUBLE")) {
            						if(!simbolo.getValor().isEmpty()) {
            	    	            	valor = simbolo.getValor();
            	            			contenidoTabla += "$"+simbolo.getLexema().replace(":", "$").replace(".","_").replace("+","$").replace("-","$") + " dq "+valor +" \n";
                        			}else {
                        				contenidoTabla += "$"+simbolo.getLexema().replace(":", "$").replace(".","_").replace("+","$").replace("-","$") + " dq 0.0, 0.0 \n";
                            		}
            						
            					}
            					
            				}else if(sim.getUso().equals("SUBTIPO")) {
            					if (sim.getTipo().equals("INTEGER")) {
            						if(!simbolo.getValor().isEmpty()) {
            	            			valor = simbolo.getValor(); //me quedo con el simbolo sin el sufijo
            	            			contenidoTabla += "$"+simbolo.getLexema().replace(":", "$").replace(".","_").replace("+","$").replace("-","$") + " dw "+ valor+ "\n";
                        			}else {
                        				contenidoTabla += "$"+simbolo.getLexema().replace(":", "$").replace(".","_").replace("+","$").replace("-","$") + " dw ? \n";
                            		}          						
            					}else if (sim.getTipo().equals("DOUBLE")) {
            						if(!simbolo.getValor().isEmpty()) {
            	    	            	valor = simbolo.getValor();
            	            			contenidoTabla += "$"+simbolo.getLexema().replace(":", "$").replace(".","_").replace("+","$").replace("-","$") + " dq "+valor +" \n";
                        			}else {
                        				contenidoTabla += "$"+simbolo.getLexema().replace(":", "$").replace(".","_").replace("+","$").replace("-","$") + " dq ? \n";
                            		}
            						
            					}
            					
            					
            					
            				}
            				
            				
            				
            				
            			}
            			break;
	            	}
            		break;            	  
            	case "cadena":
            		contenidoTabla += simbolo.getLexema().replace("[", "").replace("]", "").replace(" ","_").replace(".","_")+ " db \"" + simbolo.getLexema() +"\" , 0 \n";
            		break;
            	default:
            		if(simbolo.getLexema().contains("@aux")) {
            			switch(simbolo.getTipo()) {
            			case "INTEGER":
            				contenidoTabla += simbolo.getLexema() + " dw ? \n";
                    		break;
            			case "DOUBLE":
            				contenidoTabla += simbolo.getLexema() + " dq ? \n";
                    		break;           			
            			}
            		}
            		break;
            }
            }
		//System.out.println("TABLA PASADA A ASSEMBLER: ");
		//System.out.println(contenidoTabla);
		
    }
	
	public void generar() {
				codigo+= ".386 \n";
				codigo+=".model flat, stdcall \n";
				codigo+= "option casemap :none \n";
				codigo+= "include \\masm32\\include\\windows.inc\r\n"
						+ "include \\masm32\\include\\kernel32.inc\r\n"
						+ "include \\masm32\\include\\user32.inc\r\n"
						+ "includelib \\masm32\\lib\\kernel32.lib\r\n"
						+ "includelib \\masm32\\lib\\user32.lib \n";
				
				codigo+=".data \n";
				
				codigo+= contenidoTabla;
				
				codigo += ".code\n";
		        
		        //codigo += codigoFunciones;
		        
				codigo += AssemblerFuncs;
				
		        codigo += "main:\n";
		        codigo += assemblerMain;
		        codigo += "invoke ExitProcess, 0 \n";
		        
		        //etiquetas de errores
		        //division por cero
		        codigo += "errorDivisionPorCero: \n";
		        codigo += "invoke MessageBox, NULL, addr $errorDivisionPorCero , addr $errorDivisionPorCero , MB_OK \n";
		        codigo += "invoke ExitProcess, 0 \n";
		        
		        
		        //overflow double
		        codigo += "errorOverflowSumaDouble: \n";
		        codigo += "invoke MessageBox, NULL, addr $errorOverflowSumaDouble , addr $errorOverflowSumaDouble , MB_OK \n";
		        codigo += "invoke ExitProcess, 0 \n";
		        
		        codigo += "errorOverRangoSubtipoMenor: \n";
		        codigo += "invoke MessageBox, NULL, addr $errorOverRangoSubtipoMenor , addr $errorOverRangoSubtipoMenor , MB_OK \n";
		        codigo += "invoke ExitProcess, 0 \n";
		        codigo += "errorOverRangoSubtipoMayor: \n";
		        codigo += "invoke MessageBox, NULL, addr $errorOverRangoSubtipoMayor , addr $errorOverRangoSubtipoMayor , MB_OK \n";
		        codigo += "invoke ExitProcess, 0 \n";
		        
		        codigo += "end main";
		        	
		        //System.out.println(codigo);	
    			FileWriter writer;
				try {
					writer = new FileWriter(FILE_PATH, true);
					writer.write(codigo);
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		        //generarAssemblerFuncs();
				
		    
	}
	
	public void generarraiznull() {
		codigo+= ".386 \n";
		codigo+=".model flat, stdcall \n";
		codigo+= "option casemap :none \n";
		codigo+= "include \\masm32\\include\\windows.inc\r\n"
				+ "include \\masm32\\include\\kernel32.inc\r\n"
				+ "include \\masm32\\include\\user32.inc\r\n"
				+ "includelib \\masm32\\lib\\kernel32.lib\r\n"
				+ "includelib \\masm32\\lib\\user32.lib \n";
		
		codigo+=".data \n";
		
		codigo+= contenidoTabla;
		
		codigo += ".code\n";
        
        //codigo += codigoFunciones;
        
		//codigo += AssemblerFuncs;
		
        codigo += "main:\n";
        //codigo += "";
        codigo += "invoke ExitProcess, 0 \n";
        
        //etiquetas de errores
        //division por cero
        codigo += "errorDivisionPorCero: \n";
        codigo += "invoke MessageBox, NULL, addr $errorDivisionPorCero , addr $errorDivisionPorCero , MB_OK \n";
        codigo += "invoke ExitProcess, 0 \n";
        
        
        //overflow double
        codigo += "errorOverflowSumaDouble: \n";
        codigo += "invoke MessageBox, NULL, addr $errorOverflowSumaDouble , addr $errorOverflowSumaDouble , MB_OK \n";
        codigo += "invoke ExitProcess, 0 \n";
        
        codigo += "errorOverRangoSubtipoMenor: \n";
        codigo += "invoke MessageBox, NULL, addr $errorOverRangoSubtipoMenor , addr $errorOverRangoSubtipoMenor , MB_OK \n";
        codigo += "invoke ExitProcess, 0 \n";
        codigo += "errorOverRangoSubtipoMayor: \n";
        codigo += "invoke MessageBox, NULL, addr $errorOverRangoSubtipoMayor , addr $errorOverRangoSubtipoMayor , MB_OK \n";
        codigo += "invoke ExitProcess, 0 \n";
        
        codigo += "end main";
        	
        //System.out.println(codigo);	
		FileWriter writer;
		try {
			writer = new FileWriter(FILE_PATH, true);
			writer.write(codigo);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        //generarAssemblerFuncs();
		
    
}
	
	public String generarAssemblerFuncs() throws IOException {
		String salida ="";
		ArrayList<NodoControl> listaFuncs = Parser.get_arboles();
		for(NodoControl x : listaFuncs) {
			salida += x.getIzq().getAssembler();
		}
		return salida;
	}
	
	
}