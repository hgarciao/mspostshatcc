package pe.com.hatcc.ms.posts.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * A Registro.
 */

@Document(collection = "registro")
public class Registro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("fechahora")
    private ZonedDateTime fechahora;

    @Field("pensamiento")
    private String pensamiento;

    @Field("post")
    private String post;

    @Field("paciente")
    private String paciente;
    
    @Field("campos")
    private List<Campo> campos;
    
    @Field("comentarios")
    private List<Comentario> comentarios;
    
    //Nuevos
    
    @Field("oculto")
    private boolean oculto;
    
    @Field("eliminado")
    private boolean eliminado;
    
    @Field("suscritos")
    private List<String> suscritos ;
    
    @Field("fechahoraUpdate")
    private ZonedDateTime fechahoraUpdate;
    
    @Field("notificar")
    private boolean notificar;

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getFechahora() {
        return fechahora;
    }

    public Registro fechahora(ZonedDateTime fechahora) {
        this.fechahora = fechahora;
        return this;
    }

    public void setFechahora(ZonedDateTime fechahora) {
        this.fechahora = fechahora;
    }

    public String getPensamiento() {
        return pensamiento;
    }

    public Registro pensamiento(String pensamiento) {
        this.pensamiento = pensamiento;
        return this;
    }

    public void setPensamiento(String pensamiento) {
        this.pensamiento = pensamiento;
    }

    public String getPost() {
        return post;
    }

    public Registro post(String post) {
        this.post = post;
        return this;
    }

    public void setPost(String post) {
        this.post = post;
    }
    
    public String getPaciente() {
		return paciente;
	}

	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	public List<Campo> getCampos() {
		return campos;
	}

	public void setCampos(List<Campo> campos) {
		this.campos = campos;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Registro registro = (Registro) o;
        if(registro.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, registro.id);
    }
	

    public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	@Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Registro{" +
            "id=" + id +
            ", fechahora='" + fechahora + "'" +
            ", pensamiento='" + pensamiento + "'" +
            ", post='" + post + "'" +
            ", idpaciente='" + paciente + "'" +
            '}';
    }

	public boolean isOculto() {
		return oculto;
	}

	public void setOculto(boolean oculto) {
		this.oculto = oculto;
	}

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	public List<String> getSuscritos() {
		return suscritos;
	}

	public void setSuscritos(List<String> suscritos) {
		this.suscritos = suscritos;
	}

	public ZonedDateTime getFechahoraUpdate() {
		return fechahoraUpdate;
	}

	public void setFechahoraUpdate(ZonedDateTime fechahoraUpdate) {
		this.fechahoraUpdate = fechahoraUpdate;
	}

	public boolean isNotificar() {
		return notificar;
	}

	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}
    
    
	
    
}
