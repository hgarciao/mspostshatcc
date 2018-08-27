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
	
	@Query("{'$or':[ { 'fechahora' :{ '$lt' : ?1 }, 'eliminado' : false , 'oculto': false}, { 'fechahora' :{ '$lt' : ?1 }, 'eliminado' : false , 'oculto': true , 'paciente': '?0'} ] }")
	public List<Registro> findAllAccordingToPaciente(String paciente,ZonedDateTime fechaHora,Pageable pageable);
	
	@Query("{ 'fechahora' :{ '$lt' : ?1 }, 'eliminado' : false , 'oculto': false , 'paciente':'?0' }")
	public List<Registro> findAllAccordingToPacienteByPaciente(String pacienteFiltro,ZonedDateTime fechaHora,Pageable pageable);
	
	@Query("{'$or':[ {'id':'?0','eliminado' : false , 'oculto': false }, {'id':'?0','eliminado' : false , 'oculto': true, paciente:'?1'} ] }")
	public Registro findOneByPaciente(String id, String username);
	
	@Query(value="{ 'suscritos' : { '$all' : ['?0']}, 'comentarios':{$elemMatch: { 'paciente' : { $ne: '?0' }}} }",fields="{id:1,comentarios:1,pensamiento:1}")
	public List<Registro> findAllAccordingToPacienteSuscrito(String paciente,Sort sort);
	
	@Query("{'$or':[ { 'paciente' : '?0',  'campos': { $elemMatch: { 'opciones': { $elemMatch: { 'valor': {$regex : ?1, $options: 'i'} } } } } }, "
			+ "{ 'paciente' : ?0,  'pensamiento': {$regex : ?1, $options: 'i'} } ] }")
	public List<Registro> findAllByPacienteFilterPagination(String paciente,String Filter,Pageable pageable);
	
	@Query("{'$and':[ {'paciente':'?0','fechahora' :{ $gte : ?2} }, {'paciente':'?0','fechahora' :{ $lte : ?1} } ] }")
	public List<Registro> findAllByPacienteFilterPagination(String paciente,ZonedDateTime FilterTop,ZonedDateTime FilterBottom,Pageable pageable);
	
	@Query("{'$and':[ {'$and':[ {'paciente':'?0','fechahora' :{ $gte : ?3} }, {'paciente':'?0','fechahora' :{ $lte : ?2} } ] }, "
			+ "{'$or':[ { 'paciente' : '?0',  'campos': { $elemMatch: { 'opciones': { $elemMatch: { 'valor': {$regex : ?1, $options: 'i'} } } } } }," + 
			"{ 'paciente' : ?0,  'pensamiento': {$regex : ?1, $options: 'i'} } ] }]}")
	public List<Registro> findAllByPacienteFilterPagination(String paciente,String filter,ZonedDateTime FilterTop,ZonedDateTime FilterBottom,Pageable pageable);
	
}
