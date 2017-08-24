package pe.com.hatcc.ms.posts.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * A Variable.
 */

public class Notificacion implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int tipo;
	private String paciente;
	private String descripcion;
	
	
	
	public Notificacion(int tipo, String paciente, String descripcion) {
		super();
		this.tipo = tipo;
		this.paciente = paciente;
		this.descripcion = descripcion;
	}
	
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public String getPaciente() {
		return paciente;
	}
	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	
}
