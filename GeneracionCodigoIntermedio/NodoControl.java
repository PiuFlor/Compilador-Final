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
				if(simbolo.getUso().equals("cadena")) {
					salida+= "invoke MessageBox, NULL, addr " + simbolo.getLexema().replace("[", "").replace("]", "").replace(" ","_").replace(".","_")  +", addr " +simbolo.getLexema().replace("[", "").replace("]", "").replace(".","_").replace(" ","_") +", MB_OK \n";
					//salida+= "invoke ExitProcess, 0 \n";
				}else if(simbolo.getUso().equals("identificador")){
					salida += "MOV EAX , $"+simbolo.getLexema().replace(":", "$")+" \n";
					salida += "invoke MessageBox, NULL, addr $" + simbolo.getLexema().replace("#", "$").replace(".","_")+", addr EAX , MB_OK \n";
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
		}
		//System.out.println("ASSEMBLER: ");
		//System.out.println(salida);
		return salida;
	}
	
	
}