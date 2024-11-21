package GeneracionCodigoIntermedio;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import compilador.ConstantesCompilador;
import compilador.Simbolo;
import compilador.TablaDeSimbolos;

public class NodoComun extends ArbolSintactico {
		private String valorAssembler = "";
		
		public NodoComun(String lex,ArbolSintactico izq,ArbolSintactico der) {
			super(lex);
			setDer(der);
			setIzq(izq);
			this.valorAssembler = (lex); //seteo el valor del nodo
		}
		

		@Override
		public void recorrerArbol(String s) {
			System.out.print(s+"Lexama Nodo Comun: " + super.getLex() + "\n");
	        if (!(super.getIzq() == null)){
	            System.out.print(s+"Hijo Izquierdo: " + "\n");
	        	super.getIzq().recorrerArbol(s+"    ");
	        }
	        if (!(super.getDer() == null)){
	            System.out.print(s+"Hijo Derecho: "+ "\n");
	            super.getDer().recorrerArbol(s+"    ");
	        }
		}
		
		public void setHijoIzq(ArbolSintactico arbl) {
			setIzq(arbl);
		}
		
		public void setHijoDer(ArbolSintactico arbl) {
			setDer(arbl);
		}
		
		public ArbolSintactico getHijoIzq(){
			return super.getIzq();
		}
		
		public ArbolSintactico getHijoDer() {
			return super.getDer();
		}


		@Override
		public String getValorAssembler() {
			return valorAssembler;
		}
		
        public void setValorAssembler(String valor) {
        	setLexema(valor);
        }
		
		@Override
		public String getAssembler() throws IOException {
			
			String salida = "";
			String hijoIzq = "";
			String hijoDer = "";			
			String token = getLex();
			switch (token) {
				case "SentenciaArbol":
				if (getDer() != null) {
					salida += getIzq().getAssembler() + getDer().getAssembler();					
				} else {
					salida += getIzq().getAssembler();
				}
			break;	

			case "SentenciaProg":
					if (getDer() != null) {
						salida += getIzq().getAssembler() + getDer().getAssembler();
					} else {
						salida += getIzq().getAssembler();
					}
            break;
			case "Sentencias_fun":
				if (getDer() != null) {
					salida += getIzq().getAssembler() + getDer().getAssembler();
				} else {
					salida += getIzq().getAssembler();
				}
               break;
            case ":=":
				salida += getDer().getAssembler() + getIzq().getAssembler();
				hijoIzq = getIzq().getValorAssembler();
				hijoDer = getDer().getValorAssembler();
				String auxI = hijoIzq;
				String auxD = hijoDer;
				if (hijoIzq.contains("[")){
					hijoIzq = auxI.replace("[", "").replace("]","").replace("+", "").replace("4", "").replace("8", "");
				}
				if (hijoDer.contains("[")){
					hijoDer = auxD.replace("[", "").replace("]","").replace("+", "").replace("4", "").replace("8", "");
				}
				if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("INTEGER")){
					hijoIzq= auxI;
					hijoDer= auxD;
					hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
					salida+= "MOV AX , " + hijoDer + "\n"; 
					salida+= "MOV " + hijoIzq + ", AX"+ "\n";	
				}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("DOUBLE")) {
					if(TablaDeSimbolos.obtenerSimbolo(hijoDer).getTipo().equals("INTEGER")) {
					hijoIzq= auxI;
					hijoDer= auxD;
					hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
					
					salida+= "FILD " + hijoDer + "\n"; 
					salida+= "FSTP " + hijoIzq + "\n";
					}else if(TablaDeSimbolos.obtenerSimbolo(hijoDer).getTipo().equals("DOUBLE")){
						hijoIzq= auxI;
						hijoDer= auxD;
						hijoIzq= contieneAux(hijoIzq);
						hijoDer= contieneAux(hijoDer);
						hijoDer = hijoDer.replace("d", "E");
						salida+= "FLD " + hijoDer + "\n"; 
						salida+= "FSTP " + hijoIzq + "\n";
					}
				}else if (TablaDeSimbolos.obtenerSimbolo(hijoIzq).getUso().equals("identificador")) {
					String tipo = TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo();
					if (TablaDeSimbolos.obtenerSimbolo(tipo).getUso().equals("PAIR")) {
					
            		  if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("INTEGER")) {
            			hijoIzq= auxI;
    					hijoDer= auxD;
            			hijoIzq= contieneAux(hijoIzq);
    					hijoDer= contieneAux(hijoDer);    					
    					salida+= "MOV AX , " + hijoDer + "\n"; 
    					salida+= "MOV " + hijoIzq + ", AX"+ "\n";       		            		
                		
                	  }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("DOUBLE")){
                		hijoIzq= auxI;
    					hijoDer= auxD;
                		hijoIzq= contieneAux(hijoIzq);
    					hijoDer= contieneAux(hijoDer);
    					salida+= "FLD  " + hijoDer + "\n"; 
    					salida+= "FSTP " + hijoIzq + "\n";
                	  }
                		           	                           	
                	}else  {
                		Simbolo sim = TablaDeSimbolos.obtenerSimbolo(hijoIzq);
                		 if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("INTEGER")) {
                 			hijoIzq= auxI;
         					hijoDer= auxD;
                 			hijoIzq= contieneAux(hijoIzq);
         					hijoDer= contieneAux(hijoDer);    					
         					salida+= "MOV AX , " + hijoDer + "\n";  	 
         		
         					int rango1 = sim.getRango1();  
                    		salida +="MOV BX ,"+ rango1 +"\n";
                    		salida +="CMP AX , BX" +"\n";
                    		salida +="JL errorOverRangoSubtipoMenor "+"\n";
                    		int rango2 = sim.getRango2();
                    		salida +="MOV BX ,"+ rango2 +"\n";
                    		salida +="CMP AX , BX" +"\n";
                    		salida +="JG errorOverRangoSubtipoMayor "+"\n";
                    		
                    		salida+= "MOV " + hijoIzq + ", AX"+ "\n";
                     		
                     	  }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("DOUBLE")){
                     		hijoIzq= auxI;
         					hijoDer= auxD;
                     		hijoIzq= contieneAux(hijoIzq);
         					hijoDer= contieneAux(hijoDer);
         					salida+= "FLD  " + hijoDer + "\n"; 
         					
         					double rango1 = sim.getRango1();  
         				    salida += "FLD " + rango1 + "\n"; 
         				    salida += "FCOM \n";              
         				    salida += "FSTSW AX \n";          
         				    salida += "SAHF \n";              
         				    salida += "JB errorOverRangoSubtipoMenor \n"; 
         					 
         				    double rango2 = sim.getRango2();  
         				    salida += "FLD " + rango2 + "\n"; 
         				    salida += "FCOM \n";              
         				    salida += "FSTSW AX \n";          
         				    salida += "SAHF \n";              
         				    salida += "JA errorOverRangoSubtipoMayor \n";
         					
         					salida+= "FSTP " + hijoIzq + "\n";
                     	  }	
                	   
                	   }
					}
				break;

            case "*":  
            	salida += getIzq().getAssembler() +  getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();

            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("INTEGER")) {
            		hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV AX , "+hijoIzq+ "\n";
            		salida+= "MUL AX , " +hijoDer+ "\n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, ConstantesCompilador.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("INTEGER");
            		
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;            		            		
            		
            	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("DOUBLE")){
            		hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
            		ArbolSintactico.indiceAux++;
            		salida+= "FLD " +hijoIzq+ "\n";
        			salida+= "FMUL " +hijoDer+ "\n";
            		salida+= "FSTP @aux" +ArbolSintactico.indiceAux +"\n";
            		 
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, ConstantesCompilador.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("DOUBLE");
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;            	                           	
            	}
            	break;
            case "+":
            	salida += getIzq().getAssembler() +  getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	            	       
            	
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("INTEGER")) {
            		hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV AX , "+hijoIzq+ "\n";
            		salida+= "ADD AX , " +hijoDer+ "\n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, ConstantesCompilador.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("INTEGER");
            		
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;            		            		
            		
            	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("DOUBLE")){
            		hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
            		ArbolSintactico.indiceAux++;
            		salida+= "FLD " +hijoIzq+ "\n";
        			salida+= "FADD " +hijoDer+ "\n";
            		salida+= "FSTP @aux" +ArbolSintactico.indiceAux +"\n";
            		
            		//verifico overflow
            		salida += "FLD @aux"+ArbolSintactico.indiceAux +" \n";
            		salida += "FLD $constMaximoDouble \n";
            		salida += "FCOM \n";
            		salida += "FNSTSW AX \n";
            		salida += "SAHF \n";
            		salida += "JG errorOverflowSumaDouble \n";
            		  
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, ConstantesCompilador.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("DOUBLE");
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;            	                           	
            	}
            		
				
				break;
            case "-":
            	salida += getIzq().getAssembler() +  getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	            	       
            	
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("INTEGER")) {
            		hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV AX , "+hijoIzq+ "\n";
            		salida+= "SUB AX , " +hijoDer+ "\n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, ConstantesCompilador.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("INTEGER");
            		
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;            		            		
            		
            	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("DOUBLE")){
            		hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
            		ArbolSintactico.indiceAux++;
            		salida+= "FLD " +hijoIzq+ "\n";
        			salida+= "FSUB " +hijoDer+ "\n";
            		salida+= "FSTP @aux" +ArbolSintactico.indiceAux +"\n";

            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, ConstantesCompilador.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("DOUBLE");
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;            	                           	
            	}
            	break;
            	
            case "/":
            	salida += getIzq().getAssembler() +  getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	            	       
            	
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("INTEGER")) {
            		hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
            		ArbolSintactico.indiceAux++;
            		//controlo division por 0
            		salida += "CMP " + hijoDer + ", 0 \n";
                    salida += "JE errorDivisionPorCero\n";
                    salida+= "MOV AX , "+hijoIzq+ "\n";
            		salida+="XOR DX,DX \n";
            		salida+="MOV BX , "+hijoDer+" \n";
                    salida+= "DIV BX \n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, ConstantesCompilador.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("INTEGER");
            		
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;            		            		
            		
            	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("DOUBLE")){
            		hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
            		ArbolSintactico.indiceAux++;
            		
            		
            		salida += "FLD "+ hijoDer +" \n";
           		 	salida += "FTST \n";
           		 	salida += "FNSTSW AX \n"; //Almacena el estado de la palabra de estado de la FPU en AX
           		 	salida += "SAHF \n";     //Transfiere los flags de la FPU al registro de flags del procesador
           		 	salida += "JE errorDivisionPorCero \n";
                    salida += "FLD " +hijoIzq+" \n";
           		 	salida += "FDIV "+hijoDer+"\n";
           		    salida+= "FSTP @aux" +ArbolSintactico.indiceAux +"\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, ConstantesCompilador.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("DOUBLE");
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;            	                           	
            	}
				
            	break;
            case "MAYOR_IGUAL":   //>=
            	salida += getIzq().getAssembler() + getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	 auxI = hijoIzq;
				 auxD = hijoDer;
				if (hijoIzq.contains("[")){
					hijoIzq = auxI.replace("[", "").replace("]","").replace("+", "").replace("4", "").replace("8", "");
				}
				if (hijoDer.contains("[")){
					hijoDer = auxD.replace("[", "").replace("]","").replace("+", "").replace("4", "").replace("8", "");
				}
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("INTEGER")) {
            		hijoIzq= auxI;
					hijoDer= auxD;
					hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
					
					salida += "MOV AX , " + hijoIzq+"\n";
            		salida += "MOV $auxIntCompIzq , AX \n";  
            		salida += "MOV AX , " +hijoDer+"\n";
            		salida += "MOV $auxIntCompDer , AX \n";
                	
                	//menos menos
                 
            		salida += "MOV AX , $auxIntCompIzq \n";
                	salida += "CMP AX , $auxIntCompDer \n";
                	salida+= "JL " + ArbolSintactico.apilarLabel()+"\n";
              
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("DOUBLE")) {
                	
                	hijoIzq= auxI;
					hijoDer= auxD;
					hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
             		
					salida += "FLD " +hijoIzq+"\n";
         			salida += "FSTP $auxDoubleCompIzq \n";
           	
         			salida += "FLD " +hijoDer+"\n";
         			salida += "FSTP $auxDoubleCompDer \n";
         			
                	salida += "FLD $auxDoubleCompIzq \n";
                 	salida += "FCOM $auxDoubleCompDer \n";
                 	salida += "FNSTSWAX \n";
                    salida += "SAHF \n";
                    
                	salida+="JNGE "+ArbolSintactico.apilarLabel()+"\n";
                }else if (TablaDeSimbolos.obtenerSimbolo(hijoIzq).getUso().equals("identificador")) {
            		if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("INTEGER")) {
            			hijoIzq= auxI;
    					hijoDer= auxD;
    					hijoIzq= contieneAux(hijoIzq);
    					hijoDer= contieneAux(hijoDer);
    					
    					salida += "MOV AX , " + hijoIzq+"\n";
                		salida += "MOV $auxIntCompIzq , AX \n";  
                		salida += "MOV AX , " +hijoDer+"\n";
                		salida += "MOV $auxIntCompDer , AX \n";
                    	
                    	//menos menos
                     
                		salida += "MOV AX , $auxIntCompIzq \n";
                    	salida += "CMP AX , $auxIntCompDer \n";
                    	salida+= "JL " + ArbolSintactico.apilarLabel()+"\n";       		            		
                		
                	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("DOUBLE")){
                		hijoIzq= auxI;
    					hijoDer= auxD;
                    	hijoIzq= contieneAux(hijoIzq);
    					hijoDer= contieneAux(hijoDer);
                 		
    					salida += "FLD " +hijoIzq+"\n";
             			salida += "FSTP $auxDoubleCompIzq \n";
               	
             			salida += "FLD " +hijoDer+"\n";
             			salida += "FSTP $auxDoubleCompDer \n";
             			
                    	salida += "FLD $auxDoubleCompIzq \n";
                     	salida += "FCOM $auxDoubleCompDer \n";
                     	salida += "FNSTSWAX \n";
                        salida += "SAHF \n";
                        
                    	salida+="JNGE "+ArbolSintactico.apilarLabel()+"\n";
                	}
            		
                }
            	
                break;
                
            case ">":
            	salida += getIzq().getAssembler() + getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	 auxI = hijoIzq;
				 auxD = hijoDer;
				if (hijoIzq.contains("[")){
					hijoIzq = auxI.replace("[", "").replace("]","").replace("+", "").replace("4", "").replace("8", "");
				}
				if (hijoDer.contains("[")){
					hijoDer = auxD.replace("[", "").replace("]","").replace("+", "").replace("4", "").replace("8", "");
				}
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("INTEGER")) {
            		hijoIzq= auxI;
					hijoDer= auxD;
					hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
					
					salida += "MOV AX , " + hijoIzq+"\n";
            		salida += "MOV $auxIntCompIzq , AX \n";  
            		salida += "MOV AX , " +hijoDer+"\n";
            		salida += "MOV $auxIntCompDer , AX \n";                	
                 
            		salida += "MOV AX , $auxIntCompIzq \n";
                	salida += "CMP AX , $auxIntCompDer \n";
                	salida+= "JLE " + ArbolSintactico.apilarLabel()+"\n";
              
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("DOUBLE")) {
                	
                	hijoIzq= auxI;
					hijoDer= auxD;
					hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
             		
					salida += "FLD " +hijoIzq+"\n";
         			salida += "FSTP $auxDoubleCompIzq \n";
           	
         			salida += "FLD " +hijoDer+"\n";
         			salida += "FSTP $auxDoubleCompDer \n";
         			
                	salida += "FLD $auxDoubleCompIzq \n";
                 	salida += "FCOM $auxDoubleCompDer \n";
                 	salida += "FNSTSW AX \n";
                    salida += "SAHF \n";
                    
                	salida+="JBE "+ArbolSintactico.apilarLabel()+"\n";
                }else if (TablaDeSimbolos.obtenerSimbolo(hijoIzq).getUso().equals("identificador")) {
            		if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("INTEGER")) {
            			hijoIzq= auxI;
    					hijoDer= auxD;
    					hijoIzq= contieneAux(hijoIzq);
    					hijoDer= contieneAux(hijoDer);
    					
    					salida += "MOV AX , " + hijoIzq+"\n";
                		salida += "MOV $auxIntCompIzq , AX \n";  
                		salida += "MOV AX , " +hijoDer+"\n";
                		salida += "MOV $auxIntCompDer , AX \n";                	
                     
                		salida += "MOV AX , $auxIntCompIzq \n";
                    	salida += "CMP AX , $auxIntCompDer \n";
                    	salida+= "JLE " + ArbolSintactico.apilarLabel()+"\n";      		            		
                		
                	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("DOUBLE")){
                		hijoIzq= auxI;
    					hijoDer= auxD;
                    	hijoIzq= contieneAux(hijoIzq);
    					hijoDer= contieneAux(hijoDer);
                 		
    					salida += "FLD " +hijoIzq+"\n";
             			salida += "FSTP $auxDoubleCompIzq \n";
               	
             			salida += "FLD " +hijoDer+"\n";
             			salida += "FSTP $auxDoubleCompDer \n";
             			
                    	salida += "FLD $auxDoubleCompIzq \n";
                     	salida += "FCOM $auxDoubleCompDer \n";
                     	salida += "FNSTSW AX \n";
                        salida += "SAHF \n";
                        salida+="JBE "+ArbolSintactico.apilarLabel()+"\n";
                	}
            		
                }
            	
                break;
            	 	
            	
            case "MENOR_IGUAL":  //<=
            	salida += getIzq().getAssembler() + getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	 auxI = hijoIzq;
				 auxD = hijoDer;
				if (hijoIzq.contains("[")){
					hijoIzq = auxI.replace("[", "").replace("]","").replace("+", "").replace("4", "").replace("8", "");
				}
				if (hijoDer.contains("[")){
					hijoDer = auxD.replace("[", "").replace("]","").replace("+", "").replace("4", "").replace("8", "");
				}
            	
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("INTEGER")) {
            		hijoIzq= auxI;
					hijoDer= auxD;
                	hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
					
					salida += "MOV AX , " + hijoIzq+"\n";
            		salida += "MOV $auxIntCompIzq , AX \n";  
            		salida += "MOV AX , " +hijoDer+"\n";
            		salida += "MOV $auxIntCompDer , AX \n";
                	
                	//menos menos
                 
            		salida += "MOV AX , $auxIntCompIzq \n";
                	salida += "CMP AX , $auxIntCompDer \n";
                	salida+= "JG " + ArbolSintactico.apilarLabel()+"\n";
              
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("DOUBLE")) {
                	
                	hijoIzq= auxI;
					hijoDer= auxD;
                	hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
             		
					salida += "FLD " +hijoIzq+"\n";
         			salida += "FSTP $auxDoubleCompIzq \n";
           	
         			salida += "FLD " +hijoDer+"\n";
         			salida += "FSTP $auxDoubleCompDer \n";
         			
                	salida += "FLD $auxDoubleCompIzq \n";
                 	salida += "FCOM $auxDoubleCompDer \n";
                 	salida += "FNSTSW AX \n";
                    salida += "SAHF \n";
                    
                	salida+="JNLE "+ArbolSintactico.apilarLabel()+"\n";
                }else if (TablaDeSimbolos.obtenerSimbolo(hijoIzq).getUso().equals("identificador")) {
            		if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("INTEGER")) {
            			hijoIzq= auxI;
    					hijoDer= auxD;
    					hijoIzq= contieneAux(hijoIzq);
    					hijoDer= contieneAux(hijoDer);
    					
    					salida += "MOV AX , " + hijoIzq+"\n";
                		salida += "MOV $auxIntCompIzq , AX \n";  
                		salida += "MOV AX , " +hijoDer+"\n";
                		salida += "MOV $auxIntCompDer , AX \n";
                    	
                    	//menos menos
                     
                		salida += "MOV AX , $auxIntCompIzq \n";
                    	salida += "CMP AX , $auxIntCompDer \n";
                    	salida+= "JG " + ArbolSintactico.apilarLabel()+"\n";       		            		
                		
                	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("DOUBLE")){
                		hijoIzq= auxI;
    					hijoDer= auxD;
                    	hijoIzq= contieneAux(hijoIzq);
    					hijoDer= contieneAux(hijoDer);
                 		
    					salida += "FLD " +hijoIzq+"\n";
             			salida += "FSTP $auxDoubleCompIzq \n";
               	
             			salida += "FLD " +hijoDer+"\n";
             			salida += "FSTP $auxDoubleCompDer \n";
             			
                    	salida += "FLD $auxDoubleCompIzq \n";
                     	salida += "FCOM $auxDoubleCompDer \n";
                     	salida += "FNSTSW AX \n";
                        salida += "SAHF \n";
                        
                    	salida+="JNLE "+ArbolSintactico.apilarLabel()+"\n";
                	}
                }
            	
                break;
            case "<":
            	salida += getIzq().getAssembler() + getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	 auxI = hijoIzq;
				 auxD = hijoDer;
				if (hijoIzq.contains("[")){
					hijoIzq = auxI.replace("[", "").replace("]","").replace("+", "").replace("4", "").replace("8", "");
				}
				if (hijoDer.contains("[")){
					hijoDer = auxD.replace("[", "").replace("]","").replace("+", "").replace("4", "").replace("8", "");
				}
            	
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("INTEGER")) {
            		hijoIzq= auxI;
					hijoDer= auxD;
					hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
					
					salida += "MOV AX , " + hijoIzq+"\n";
            		salida += "MOV $auxIntCompIzq , AX \n";  
            		salida += "MOV AX , " +hijoDer+"\n";
            		salida += "MOV $auxIntCompDer , AX \n";
                	
                	//menos menos
                 
            		salida += "MOV AX , $auxIntCompIzq \n";
                	salida += "CMP AX , $auxIntCompDer \n";
                	salida+= "JAE " + ArbolSintactico.apilarLabel()+"\n";
              
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("DOUBLE")) {
                	
                	hijoIzq= auxI;
					hijoDer= auxD;
					hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
             		
					salida += "FLD " +hijoIzq+"\n";
         			salida += "FSTP $auxDoubleCompIzq \n";
           	
         			salida += "FLD " +hijoDer+"\n";
         			salida += "FSTP $auxDoubleCompDer \n";
         			
                	salida += "FLD $auxDoubleCompIzq \n";
                 	salida += "FCOM $auxDoubleCompDer \n";
                 	salida += "FNSTSW AX \n";
                    salida += "SAHF \n";
                    
                	salida+="JNL "+ArbolSintactico.apilarLabel()+"\n";
                }else if (TablaDeSimbolos.obtenerSimbolo(hijoIzq).getUso().equals("identificador")) {
            		if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("INTEGER")) {
            			hijoIzq= auxI;
    					hijoDer= auxD;
    					hijoIzq= contieneAux(hijoIzq);
    					hijoDer= contieneAux(hijoDer);
    					
    					salida += "MOV AX , " + hijoIzq+"\n";
                		salida += "MOV $auxIntCompIzq , AX \n";  
                		salida += "MOV AX , " +hijoDer+"\n";
                		salida += "MOV $auxIntCompDer , AX \n";
                    	
                    	//menos menos
                     
                		salida += "MOV AX , $auxIntCompIzq \n";
                    	salida += "CMP AX , $auxIntCompDer \n";
                    	salida+= "JAE " + ArbolSintactico.apilarLabel()+"\n";
                	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("DOUBLE")){
                		hijoIzq= auxI;
    					hijoDer= auxD;
                    	hijoIzq= contieneAux(hijoIzq);
    					hijoDer= contieneAux(hijoDer);
                 		
    					salida += "FLD " +hijoIzq+"\n";
             			salida += "FSTP $auxDoubleCompIzq \n";
               	
             			salida += "FLD " +hijoDer+"\n";
             			salida += "FSTP $auxDoubleCompDer \n";
             			
                    	salida += "FLD $auxDoubleCompIzq \n";
                     	salida += "FCOM $auxDoubleCompDer \n";
                     	salida += "FNSTSW AX \n";
                        salida += "SAHF \n";
                	}
            		
                }
            	break;
            case "=":
            	salida += getIzq().getAssembler() + getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();

            	 auxI = hijoIzq;
				 auxD = hijoDer;
				if (hijoIzq.contains("[")){
					hijoIzq = auxI.replace("[", "").replace("]","").replace("+", "").replace("4", "").replace("8", "");
				}
				if (hijoDer.contains("[")){
					hijoDer = auxD.replace("[", "").replace("]","").replace("+", "").replace("4", "").replace("8", "");
				}
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("INTEGER")) {
            		hijoIzq= auxI;
					hijoDer= auxD;
            		hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
					
					salida += "MOV AX , " + hijoIzq+"\n";
            		salida += "MOV $auxIntCompIzq , AX \n";  
            		salida += "MOV AX , " +hijoDer+"\n";
            		salida += "MOV $auxIntCompDer , AX \n";
                	
                	//menos menos
                 
            		salida += "MOV AX , $auxIntCompIzq \n";
                	salida += "CMP AX , $auxIntCompDer \n";
                	salida+= "JNE " + ArbolSintactico.apilarLabel()+"\n";
              
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("DOUBLE")) {
                	hijoIzq= auxI;
					hijoDer= auxD;
                	hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
             		
					salida += "FLD " +hijoIzq+"\n";
         			salida += "FSTP $auxDoubleCompIzq \n";
           	
         			salida += "FLD " +hijoDer+"\n";
         			salida += "FSTP $auxDoubleCompDer \n";
         			
                	salida += "FLD $auxDoubleCompIzq \n";
                 	salida += "FCOM $auxDoubleCompDer \n";
                 	salida += "FNSTSW AX \n";
                    salida += "SAHF \n";
                    
                	salida+="JNE "+ArbolSintactico.apilarLabel()+"\n";
                }else if (TablaDeSimbolos.obtenerSimbolo(hijoIzq).getUso().equals("identificador")) {
            		if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("INTEGER")) {
            			hijoIzq= auxI;
    					hijoDer= auxD;
    					hijoIzq= contieneAux(hijoIzq);
    					hijoDer= contieneAux(hijoDer);
    					
    					salida += "MOV AX , " + hijoIzq+"\n";
                		salida += "MOV $auxIntCompIzq , AX \n";  
                		salida += "MOV AX , " +hijoDer+"\n";
                		salida += "MOV $auxIntCompDer , AX \n";
                    	
                    	//menos menos
                     
                		salida += "MOV AX , $auxIntCompIzq \n";
                    	salida += "CMP AX , $auxIntCompDer \n";
                    	salida+= "JNE " + ArbolSintactico.apilarLabel()+"\n";        		            		
                		
                	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("DOUBLE")){
                		hijoIzq= auxI;
    					hijoDer= auxD;
                    	hijoIzq= contieneAux(hijoIzq);
    					hijoDer= contieneAux(hijoDer);
                 		
    					salida += "FLD " +hijoIzq+"\n";
             			salida += "FSTP $auxDoubleCompIzq \n";
               	
             			salida += "FLD " +hijoDer+"\n";
             			salida += "FSTP $auxDoubleCompDer \n";
             			
                    	salida += "FLD $auxDoubleCompIzq \n";
                     	salida += "FCOM $auxDoubleCompDer \n";
                     	salida += "FNSTSW AX \n";
                        salida += "SAHF \n";
                        
                    	salida+="JNE "+ArbolSintactico.apilarLabel()+"\n";
                	}
            		
                }
            	break;
            case "DIST":   //!=
            	salida += getIzq().getAssembler() + getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	 auxI = hijoIzq;
				 auxD = hijoDer;
				if (hijoIzq.contains("[")){
					hijoIzq = auxI.replace("[", "").replace("]","").replace("+", "").replace("4", "").replace("8", "");
				}
				if (hijoDer.contains("[")){
					hijoDer = auxD.replace("[", "").replace("]","").replace("+", "").replace("4", "").replace("8", "");
				}
            	
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("INTEGER")) {
            		hijoIzq= auxI;
					hijoDer= auxD;
            		hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
					
					salida += "MOV AX , " + hijoIzq+"\n";
            		salida += "MOV $auxIntCompIzq , AX \n";  
            		salida += "MOV AX , " +hijoDer+"\n";
            		salida += "MOV $auxIntCompDer , AX \n";
                	
                	//menos menos
                 
            		salida += "MOV AX , $auxIntCompIzq \n";
                	salida += "CMP AX , $auxIntCompDer \n";                	
                	salida+= "JE " + ArbolSintactico.apilarLabel()+"\n";
              
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("DOUBLE")) {
                	hijoIzq= auxI;
					hijoDer= auxD;
                	hijoIzq= contieneAux(hijoIzq);
					hijoDer= contieneAux(hijoDer);
             		
					salida += "FLD " +hijoIzq+"\n";
         			salida += "FSTP $auxDoubleCompIzq \n";
           	
         			salida += "FLD " +hijoDer+"\n";
         			salida += "FSTP $auxDoubleCompDer \n";
         			
                	salida += "FLD $auxDoubleCompIzq \n";
                 	salida += "FCOM $auxDoubleCompDer \n";
                 	salida += "FNSTSW AX \n";
                    salida += "SAHF \n";
                    
                	salida+="JE "+ArbolSintactico.apilarLabel()+"\n";
                } else if (TablaDeSimbolos.obtenerSimbolo(hijoIzq).getUso().equals("identificador")) {
            		if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("INTEGER")) {
                		hijoIzq= auxI;
    					hijoDer= auxD;
                		hijoIzq= contieneAux(hijoIzq);
    					hijoDer= contieneAux(hijoDer);
    					
    					salida += "MOV AX , " + hijoIzq+"\n";
                		salida += "MOV $auxIntCompIzq , AX \n";  
                		salida += "MOV AX , " +hijoDer+"\n";
                		salida += "MOV $auxIntCompDer , AX \n";
                    	
                    	//menos menos
                     
                		salida += "MOV AX , $auxIntCompIzq \n";
                    	salida += "CMP AX , $auxIntCompDer \n";                	
                    	salida+= "JE " + ArbolSintactico.apilarLabel()+"\n";
                	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("DOUBLE")){
                		hijoIzq= auxI;
    					hijoDer= auxD;
    					hijoIzq= contieneAux(hijoIzq);
    					hijoDer= contieneAux(hijoDer);
                 		
    					salida += "FLD " +hijoIzq+"\n";
             			salida += "FSTP $auxDoubleCompIzq \n";
               	
             			salida += "FLD " +hijoDer+"\n";
             			salida += "FSTP $auxDoubleCompDer \n";
             			
                    	salida += "FLD $auxDoubleCompIzq \n";
                     	salida += "FCOM $auxDoubleCompDer \n";
                     	salida += "FNSTSW AX \n";
                        salida += "SAHF \n";
                	}
                }
            	break;
            case "IF":
				salida += getIzq().getAssembler() + getDer().getAssembler();
				salida+=ArbolSintactico.desapilarLabel()+":"+" \n";
				break;
            case "cuerpo":
            	if(getDer() != null) {
        			salida += getIzq().getAssembler() + getDer().getAssembler();
        		}else {
        			salida += getIzq().getAssembler();
        		}
            	break;
            case "Sentencia_Dentro_IF":
            	if (getDer() != null) {
					salida += getIzq().getAssembler() + getDer().getAssembler();
				} else {
					salida += getIzq().getAssembler();
				}

				
				break;
            case "WHILE":
            	salida += ArbolSintactico.apilarLabel() +": \n";//apilo label
            	
            	salida += getIzq().getAssembler() + getDer().getAssembler();//izq condicion, der cuerpo while
            	break;
            case "Cuerpo_Fun":
            	 hijoIzq = getIzq().getLex();
            	 salida += hijoIzq  + ": \n";
            	 salida += getDer().getAssembler();
            	
            	break;
            case "ejecucion funcion":
            	salida += getDer().getAssembler();
            	hijoIzq= getIzq().getLex();
            	hijoDer= getDer().getLex();
				hijoDer= contieneAux(hijoDer);
				salida += "MOV AX ,"+ hijoDer +"\n";
				salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
        		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
        		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, ConstantesCompilador.ID);
        		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("INTEGER");
        		valorAssembler = "@aux"+ArbolSintactico.indiceAux;    
        		
            	salida += "PUSH  @aux" +ArbolSintactico.indiceAux+ "\n";
            	salida += "CALL " + hijoIzq +" \n";
            	
            	break;
            case "RET":  
            	salida += getIzq().getAssembler();
            	hijoIzq = getIzq().getLex();
            	hijoIzq = contieneAux(hijoIzq);
            	salida +="MOV AX , "+ hijoIzq +" \n"  ;
            	salida += "ret  \n";
            	break;
            case "IndicePair":
            	hijoIzq = getIzq().getLex();
            	
            	hijoDer = getDer().getLex();
            	
            	if (TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("INTEGER"))
            	      if (hijoDer.equals("1")) {
            		    hijoIzq = "["+ hijoIzq + "]" ;
            	      }else {
            		   hijoIzq = "["+ hijoIzq +"+"+ 4 +"]";
            	}else {
            		if (TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipoParametro().equals("DOUBLE"))
              	      if (hijoDer.equals("1")) {
              		    hijoIzq = "["+ hijoIzq + "]" ;
              	      }else {
              		   hijoIzq = "["+ hijoIzq +"+"+ 8 +"]";
              	      }
            	}            	
        		
        		valorAssembler = hijoIzq;  
            	break;
          
            default:
                              
			break;
		}

		return salida ; 
	}

	public String contieneAux(String hijoIzq){
		
		if(!hijoIzq.contains("@")) {	
		  if (hijoIzq.contains("[")) {
			hijoIzq ="["+"$"+hijoIzq.replace(":", "$").replace(".","_").replace("-","$").replace("[","").replace("]","") +"]";			
		  }else {
			  hijoIzq ="$"+hijoIzq.replace(":", "$").replace(".","_").replace("+","").replace("-","$"); 
		  }
		}
			
		return hijoIzq;
	}
}