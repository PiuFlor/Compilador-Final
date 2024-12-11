package GeneracionCodigoIntermedio;

import java.io.IOException;

import compilador.Simbolo;
import compilador.TablaDeSimbolos;

public class NodoControl extends ArbolSintactico {
	
	private String valorAssembler = "";
	
	public NodoControl(String lex,ArbolSintactico nodo) {
		super(lex);
		setIzq(nodo);
		valorAssembler = (lex); //seteo el valor del nodo
		
	}

	@Override
	public String getValorAssembler() {
		return valorAssembler;
	}
	
	@Override
	public void recorrerArbol(String identado) {
		System.out.print(identado+"Lexema Control: " +super.getLex()+ "\n");
		identado += "    ";
		super.getIzq().recorrerArbol(identado);
	}

	@Override
	public String getAssembler() throws IOException {
		String salida = "";
		//System.out.println("Nodo Control. Lexema: '" + getLex()+"'. Hijo: '" + getIzq().getLex()+"'");
		switch(this.getLex()) {
			case "Programa":
				ArbolSintactico arbIzq = super.getIzq();
				salida += arbIzq.getAssembler();
				break;
			case "SentenciaProg":
				if (getDer() != null) {
					salida += getIzq().getAssembler() + getDer().getAssembler();
				} else {
					salida += getIzq().getAssembler();
				}
            break;
			case "Condicion":
				salida += getIzq().getAssembler();
				break;
			case "then":
				salida += getIzq().getAssembler();
				break;
			case "else":
				salida+="JMP "+ArbolSintactico.apilarLabel()+"\n";
				String aux = ArbolSintactico.desapilarLabel() ;
				if(ArbolSintactico.pilaLabels.isEmpty()) {
					salida+= aux +": \n";
				}else {
					salida+= ArbolSintactico.desapilarLabel() +": \n";
					ArbolSintactico.pilaLabels.push(aux);
				}
				salida += getIzq().getAssembler();
				break;
			case "sentencia":
				salida += getIzq().getAssembler();
				String auxlabel = ArbolSintactico.desapilarLabel();
				salida += "JMP " + ArbolSintactico.desapilarLabel() +" \n";
				salida += auxlabel + ": \n";
				//ArbolSintactico.pilaLabels.push(auxlabel);
				break;
				
			
			case "OUTF":
				//Como en este caso se que solo voy a tener un hijo izquierdo, hago la operacion aca
				
				Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(this.getIzq().getLex());
					if (simbolo != null ) {
				        // Caso 1: Cadena
				        if (simbolo.getUso().equals("cadena")) {
				        	
				            salida += "invoke MessageBox, NULL, addr " +
				                      simbolo.getLexema().replace("[", "").replace("]", "").replace(" ", "_").replace(".", "_").replace(":", "") + ", addr " +
				                      simbolo.getLexema().replace("[", "").replace("]", "").replace(" ", "_").replace(".", "_").replace(":", "") + ", MB_OK \n";
				        }
				        // Caso 2: Identificador (Variable)
				        else if (simbolo.getUso().equals("identificador")) {
				        	String tipo ;
				        	if (!simbolo.getTipo().equals("INTEGER") && !simbolo.getTipo().equals("INTEGER")) {
				        		tipo = simbolo.getTipoParametro();
				        	}else {
				        		tipo = simbolo.getTipo();
				        	}
				        					            // Variable de tipo INTEGER
				            if (tipo.equals("INTEGER")) {
				                salida += "MOV AX, $" + simbolo.getLexema().replace(":", "$") + " \n";
				                salida += "invoke wsprintf, addr auxCadena, addr msg , AX \n"; // Convertir entero a cadena
				                salida += "invoke MessageBox, NULL, addr auxCadena, addr auxCadena, MB_OK \n";
				            }
				            // Variable de tipo DOUBLE
				            else if (tipo.equals("DOUBLE")) {
				               /* salida += "FLD $" + simbolo.getLexema().replace(":", "$") + " \n";
				                salida += "invoke wsprintf, addr $auxCadena ,addr msg2 ,"  + "@aux"+ ArbolSintactico.indiceAux +" \n"; // Convertir double a cadena
				                salida += "invoke MessageBox, NULL, addr $auxCadena, addr $auxCadena, MB_OK \n";*/
				              //Obtener la parte entera
					            salida += "FLD " +simbolo.getLexema().replace(":", "$") + " \n";  // Cargar el valor al coprocesado
					            salida += "FSTCW [fpu_cw]  \n"; //Guardar el control word
					            salida += "mov ax, [fpu_cw]  \n"; 
					            salida += "and ax, 0F3FFh   \n"; //Limpiar bits de control de redondeo
					            salida += "or ax, 0C00h  \n"; //Establecer redondeo hacia cero
					            salida += "mov [fpu_cw], ax \n"; 
					            salida += "fldcw [fpu_cw]  \n"; 
					            salida += "frndint   \n"; 
					            salida += "fistp [$partEnt]  \n"; 
					            //Obtener la parte decimal
					            salida += "FLD " + simbolo.getLexema().replace(":", "$") + " \n"; 
					            salida += "fisub [$partEnt]  \n"; 
					            salida += "fmul QWORD PTR [factor]   \n"; 
					            salida += "fistp [$partDec]   \n"; 
					            //Mostrar en MessageBox
					            salida += "invoke wsprintf, addr auxCadena ,addr msg2 ,[$partEnt], [$partDec] \n";  // Convertir a texto
					            salida += "invoke MessageBox, NULL, addr auxCadena, addr auxCadena, MB_OK \n";  // Mostrar
					        
				            }
				        }
					
				     
				    // Caso 3: Expresión Compleja
				    else {
				    	  // Obtener el ensamblador y tipo del valor calculado
				    	salida += getIzq().getAssembler();
				        String valorResultado = getIzq().getValorAssembler(); // Donde se almacenó el resultado	        	
				        Simbolo simbolo1 = TablaDeSimbolos.obtenerSimbolo(valorResultado);
				        if (valorResultado.contains("[")) {
							simbolo1 = TablaDeSimbolos.obtenerSimbolo(valorResultado.replace("[", "").replace("]", ""));
							valorResultado = "[$" + valorResultado.replace(":", "$").replace("[","");
						}else {
							simbolo1 = TablaDeSimbolos.obtenerSimbolo(valorResultado);
						}

				        // Verificar el tipo del resultado y generar el código de impresión
				        if (simbolo1.getTipo().equals("INTEGER") || simbolo1.getTipoParametro().equals("INTEGER")  ) {
				            salida += "MOV AX, " + valorResultado + " \n";  // Mover el valor al registro AX
				            salida += "invoke wsprintf, addr auxCadena, addr msg , AX \n";  // Convertir a texto
				            salida += "invoke MessageBox, NULL, addr auxCadena, addr auxCadena, MB_OK \n";  // Mostrar
				        } else if (simbolo1.getTipo().equals("DOUBLE") || simbolo1.getTipoParametro().equals("DOUBLE") ) {
				        	//Obtener la parte entera
				            salida += "FLD " + valorResultado + " \n";  // Cargar el valor al coprocesado
				            salida += "FSTCW [fpu_cw]  \n"; //Guardar el control word
				            salida += "mov ax, [fpu_cw]  \n"; 
				            salida += "and ax, 0F3FFh   \n"; //Limpiar bits de control de redondeo
				            salida += "or ax, 0C00h  \n"; //Establecer redondeo hacia cero
				            salida += "mov [fpu_cw], ax \n"; 
				            salida += "fldcw [fpu_cw]  \n"; 
				            salida += "frndint   \n"; 
				            salida += "fistp [$partEnt]  \n"; 
				            //Obtener la parte decimal
				            salida += "FLD " + valorResultado + " \n"; 
				            salida += "fisub [$partEnt]  \n"; 
				            salida += "fmul QWORD PTR [factor]   \n"; 
				            salida += "fistp [$partDec]   \n"; 
				            //Mostrar en MessageBox
				            salida += "invoke wsprintf, addr auxCadena ,addr msg2 ,[$partEnt], [$partDec] \n";  // Convertir a texto
				            salida += "invoke MessageBox, NULL, addr auxCadena, addr auxCadena, MB_OK \n";  // Mostrar
				        }
				    
				    }
				 
			    }
				
				break;
			case "Funcion":
				salida += getIzq().getAssembler();
				break;
			
			case "GOTO":
				String etiqueta = getIzq().getLex();
				salida += "JMP " + etiqueta +" \n";
				
				break;
			case "ETIQUETA_SALTO":
				String salto = getIzq().getLex();
				salida += salto +": \n";
				
				break;	
			case "CONVERSION":
				
				if (getIzq().getLex().equals("+") || getIzq().getLex().equals("-")||getIzq().getLex().equals("*") ||getIzq().getLex().equals("/") || getIzq().getLex().equals("IndicePair")) {
					
					salida += getIzq().getAssembler();
				}else {
					
				     String hijoIzq = getIzq().getValorAssembler().replace(":", "$");
				     System.out.println(hijoIzq);
				     if (hijoIzq.contains(".")) {
				    	 hijoIzq = hijoIzq.replace("-", "$");
				    	 if (hijoIzq.startsWith("$")){
				    	     hijoIzq =  hijoIzq.replace("d", "E").replace("+", "").replace("-", "$").replace(".", "_");
				    	 } else {
				    		 hijoIzq = "$" + hijoIzq.replace("d", "E").replace("+", "").replace("-", "$").replace(".", "_"); 
				    	 } 
				    	 salida += "FLD " + hijoIzq + "\n";
				     }else {
				     salida += "FILD " +"$"+ hijoIzq + "\n";
				     }
				    
				}
				break;
			 default:
                 salida += "";             
			break;
		}
		//System.out.println("ASSEMBLER: ");
		//System.out.println(salida);
		return salida;
	}
	
	
}