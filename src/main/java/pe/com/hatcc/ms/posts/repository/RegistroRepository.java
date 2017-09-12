package pe.com.hatcc.ms.posts.repository;

import pe.com.hatcc.ms.posts.domain.Registro;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Spring Data MongoDB repository for the Registro entity.
 */
@SuppressWarnings("unused")
public interface RegistroRepository extends MongoRepository<Registro,String> {
	
	
	@Query("{ 'paciente' : ?0 }")
	public List<Registro> findAllByPaciente(String paciente);
	
	//@Query("{ paciente: { $ne: ?0 } }")
	@Query("{ 'fechahora' :{ '$lt' : ?1 }, 'eliminado' : false }")
	public List<Registro> findAllAccordingToPaciente(String paciente,ZonedDateTime fechaHora,Pageable pageable);
	
	
	@Query(value="{ 'suscritos' : { '$all' : ['?0']}, 'comentarios':{$elemMatch: { 'paciente' : { $ne: '?0' }}} }",fields="{id:1,comentarios:1,pensamiento:1}")
	public List<Registro> findAllAccordingToPacienteSuscrito(String paciente,Sort sort);
	
}
