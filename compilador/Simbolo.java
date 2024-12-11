package compilador;
import java.util.HashMap;

public class Simbolo {
    private boolean valorAsignado = false;
    private String lexema;
    private int id;
    private String tipo;
    private String uso;
    private String ambito;
    private HashMap<String,String> atributosFuncion;
//    private int indice1; //tipo pair
 //   private int indice2; //tipo pair
    private int rango1; //tipo subtipo
    private int rango2; //tipo subtipo
    private String tipoParametro = "" ;
    
    private String valor = "";

    public Simbolo(String lexema) {
        this.lexema = lexema;
        this.id=0;
        this.tipo = "";
        this.uso = "";
        this.ambito = "";
        this.atributosFuncion = new HashMap<>();

    }

    public Simbolo(String lexema, int id)
    {
        this.lexema = lexema;
        this.id=id;
        this.tipo = "";
        this.uso = "";
        this.ambito = "";
        this.atributosFuncion = new HashMap<>();
    }

    public void setValorAsignado(boolean asig) {
		this.valorAsignado = asig;
	}

    public boolean getValorAsignado(){
        return this.valorAsignado;
    }

    public void setAtributo(String key, String value){
        this.atributosFuncion.put(key, value);
    }

    public HashMap<String,String> getAtributos(){
        return this.atributosFuncion;
    }

    public void setFuncionAsignada(String funcionAsignada)
    {
        this.atributosFuncion.put("funcionAsignada", funcionAsignada);
    }

    public String getFuncionAsignada(){
        return this.atributosFuncion.get("funcionAsignada");
    }

    public String getNombreRetorno(){
        return this.atributosFuncion.get("nombreretorno");
    }

    public void setNombreRetorno(String nombreRetorno){
        this.atributosFuncion.put("nombreRetorno", nombreRetorno);
    }

    public String getRetorno()
    {
        return this.atributosFuncion.get("retorno");
    }

    public void setRetorno(String retorno){
        this.atributosFuncion.put("retorno", retorno);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipoVariable) {
        this.tipo = tipoVariable;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

	/*public int getIndice1() {
		return indice1;
	}

	public void setIndice1(int indice1) {
		this.indice1 = indice1;
	}

	public int getIndice2() {
		return indice2;
	}

	public void setIndice2(int indice2) {
		this.indice2 = indice2;
	}*/

	public int getRango1() {
		return rango1;
	}

	public void setRango1(int rango1) {
		this.rango1 = rango1;
	}

	public int getRango2() {
		return rango2;
	}

	public void setRango2(int rango2) {
		this.rango2 = rango2;
	}

	public String getTipoParametro() {
		return tipoParametro;
	}

	public void setTipoParametro(String tipoParametro) {
		this.tipoParametro = tipoParametro;
	}
   
	public String getValor()
	{
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}

}
