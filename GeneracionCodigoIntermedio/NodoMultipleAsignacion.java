package GeneracionCodigoIntermedio;

import java.io.IOException;
import java.util.List;

public class NodoMultipleAsignacion extends ArbolSintactico{
	
	 private List<ArbolSintactico> asignaciones; // Lista de subárboles de asignación 
	 private String asignacionMul;

		

		public NodoMultipleAsignacion(String asignacionMul, List<ArbolSintactico> asignaciones) {
			super("AsignacionM"); // Etiqueta para el nodo de asignación múltiple
			this.asignacionMul = asignacionMul;
			this.asignaciones = asignaciones;
		}

		public List<ArbolSintactico> getAsignaciones() {
			return asignaciones;
		}

		@Override
		public void recorrerArbol(String indent) {
			System.out.println(indent + "Nodo de Asignación Múltiple:" + getLex());
			for (ArbolSintactico asignacion : asignaciones) {
				asignacion.recorrerArbol(indent + "    " );
			}
		}

		@Override
		public String getValorAssembler() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getAssembler() throws IOException {
			String salida="";
			for (ArbolSintactico asignacion : asignaciones) {
	            salida +=asignacion.getAssembler();
	        }
			return salida;
		}

		public String getAsignacionMul() {
			return asignacionMul;
		}
	
		public void setAsignacionMul(String asignacionMul) {
			this.asignacionMul = asignacionMul;
		}


}