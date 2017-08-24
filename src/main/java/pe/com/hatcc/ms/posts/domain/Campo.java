package pe.com.hatcc.ms.posts.domain;

import java.io.Serializable;
import java.util.List;

/**
 * A Variable.
 */

public class Campo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nombreRegistroVariable;

	private List<Opcion> opciones;

	
	public String getNombreRegistroVariable() {
		return nombreRegistroVariable;
	}

	public void setNombreRegistroVariable(String nombreRegistroVariable) {
		this.nombreRegistroVariable = nombreRegistroVariable;
	}

	public List<Opcion> getOpciones() {
		return opciones;
	}

	public void setOpciones(List<Opcion> opciones) {
		this.opciones = opciones;
	}
	
	/*
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Campo variable = (Campo) o;
		if (variable.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, variable.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Variable{" + "id=" + id + ", idcontexto='" + idcontexto + "'" + ", nombre='" + nombre + "'"
				+ ", descripcion='" + descripcion + "'" + ", opciones='" + opciones + "'" + '}';
	}
	*/
}
