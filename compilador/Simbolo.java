package compilador;
import java.util.HashMap;

public class Simbolo {

    private String lexema;
    private int id;
    private String tipo;
    private String uso;
    private String ambito;
    private HashMap<String,String> atributosFuncion;


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


}
