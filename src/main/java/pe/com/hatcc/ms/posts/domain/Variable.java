package pe.com.hatcc.ms.posts.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A Variable.
 */

@Document(collection = "variable")
public class Variable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Field("idcontexto")
	private String idcontexto;

	@Field("nombre")
	private String nombre;

	@Field("nombreRegistro")
	private String nombreRegistro;

	@Field("flagMultivalor")
	private boolean flagMultivalor;

	@Field("descripcion")
	private String descripcion;

	@Field("opciones")
	private List<Opcion> opciones;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdcontexto() {
		return idcontexto;
	}

	public Variable idcontexto(String idcontexto) {
		this.idcontexto = idcontexto;
		return this;
	}

	public void setIdcontexto(String idcontexto) {
		this.idcontexto = idcontexto;
	}

	public String getNombre() {
		return nombre;
	}

	public Variable nombre(String nombre) {
		this.nombre = nombre;
		return this;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Variable descripcion(String descripcion) {
		this.descripcion = descripcion;
		return this;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Opcion> getOpciones() {
		return opciones;
	}

	public void setOpciones(List<Opcion> opciones) {
		this.opciones = opciones;
	}

	public String getNombreRegistro() {
		return nombreRegistro;
	}

	public void setNombreRegistro(String nombreRegistro) {
		this.nombreRegistro = nombreRegistro;
	}

	public boolean  getFlagMultivalor() {
		return flagMultivalor;
	}

	public void setFlagMultivalor(boolean flagMultivalor) {
		this.flagMultivalor = flagMultivalor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Variable variable = (Variable) o;
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
}
