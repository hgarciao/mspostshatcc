package pe.com.hatcc.ms.posts.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * A Variable.
 */

public class Comentario implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String paciente;

	private String contenido;
	
	private ZonedDateTime fechaHora;

	public String getPaciente() {
		return paciente;
	}

	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ZonedDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(ZonedDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	

	

}
