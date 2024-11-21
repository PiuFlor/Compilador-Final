package GeneracionCodigoIntermedio;


public class NodoHoja extends ArbolSintactico {
	
	private String valorAssembler ="";
	
	public NodoHoja(String lex) {
		super(lex);
		valorAssembler = (lex); //seteo el valor del nodo
		
	}
	
	@Override
	public String getValorAssembler() {
		// TODO Auto-generated method stub
		if(getLex().equals("RET")) {
			return "ret \n";
		}
		return valorAssembler;
	}
	
	@Override
	public void recorrerArbol(String s) {
		System.out.print(s);
        System.out.print("Lexema Nodo Hoja: " + super.getLex()+ "\n");

	}


	@Override
	public String getAssembler() {
		return "";//super.getLex();
	}


}