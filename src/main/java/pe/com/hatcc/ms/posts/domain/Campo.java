package pe.com.hatcc.ms.posts.domain;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Variable.
 */

public class Campo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nombreRegistroVariable;

	private List<Opcion> opciones;
	
	private String tipo;
	
	private String nombrePost;
	
	private int nivel;

	
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombrePost() {
		return nombrePost;
	}

	public void setNombrePost(String nombrePost) {
		this.nombrePost = nombrePost;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
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
