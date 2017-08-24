package pe.com.hatcc.ms.posts.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Opcion.
 */

public class Opcion implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    
    private String valor;

    private String descripcion;
    
    private String idUsuario;
    
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValor() {
        return valor;
    }

    public Opcion valor(String valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Opcion descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

   public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	/* @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Opcion opcion = (Opcion) o;
        if(opcion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, opcion.id);
    }
    */
    @Override
    public String toString() {
        return "Opcion{" +
            ", valor='" + valor + "'" +
            ", descripcion='" + descripcion + "'" +
            '}';
    }
}
