package pe.com.hatcc.ms.posts.service;

import pe.com.hatcc.ms.posts.domain.Comentario;
import pe.com.hatcc.ms.posts.domain.Registro;
import pe.com.hatcc.ms.posts.repository.RegistroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import com.mongodb.Mongo;
import com.mongodb.operation.DistinctOperation;

import javax.inject.Inject;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Registro.
 */
@Service
public class RegistroService {

    private final Logger log = LoggerFactory.getLogger(RegistroService.class);
    
    @Inject
    private RegistroRepository registroRepository;
    
    @Autowired
    private MongoOperations mongoOperations;


    /**
     * Save a registro.
     *
     * @param registro the entity to save
     * @return the persisted entity
     */
    public Registro save(Registro registro) {
        log.debug("Request to save Registro : {}", registro);
        Registro result = registroRepository.save(registro);
        return result;
    }

    /**
     *  Get all the registros.
     *  
     *  @return the list of entities
     */
    public List<Registro> findAll() {
        log.debug("Request to get all Registros");
        List<Registro> result = registroRepository.findAll();

        return result;
    }

    
    /**
     *  Get all the registros.
     *  
     *  @return the list of entities
     */
    
    public List<Registro> findAllByPaciente(String paciente) {
        log.debug("Request to get all Registros");
        List<Registro> result = registroRepository.findAllByPaciente(paciente);
        return result;
    }   
    
    
    /**
     *  Get all the registros.
     *  
     *  @return the list of entities
     */
    
    public List<Registro> findAllAccordingToPaciente(String paciente,int page,int pagesize) {
        log.debug("Request to get all Registros");
        List<Registro> result = registroRepository.findAllAccordingToPaciente(paciente,new PageRequest(page, pagesize,new Sort(Sort.Direction.DESC, "fechahora")));
        return result;
    }   
    
    
    /**
     *  Get all the registros.
     *  
     *  @return the list of entities
     */
    
    public List<Registro> findAllAccordingToPacienteSuscrito(String paciente) {
        /*log.debug("Request to get all Registros");
        List<Registro> result = registroRepository.findAllAccordingToPacienteSuscrito(paciente,new Sort(Sort.Direction.DESC, "fechahoraUpdate"));
        System.out.println("template : " + mongoTemplate);*/
    	List suscrito =  new ArrayList<String>();
    	suscrito.add(paciente);
    	//LIMITAR A 10
    	 List<AggregationOperation> list = new ArrayList<AggregationOperation>();
    	 	list.add(Aggregation.match(Criteria.where("suscritos").all(suscrito)));
    	    list.add(Aggregation.unwind("comentarios"));
    	    list.add(Aggregation.match(Criteria.where("comentarios.paciente").ne(paciente)));
    	    list.add(Aggregation.sort(new Sort(Sort.Direction.DESC,"comentarios.paciente")));
    	    list.add(Aggregation.sort(new Sort(Sort.Direction.DESC,"comentarios.fechaHora")));
    	    list.add(Aggregation.group("fechahoraUpdate","pensamiento","opUpdate").push("comentarios").as("comentarios").first("id").as("post"));
    	    list.add(Aggregation.project("pensamiento", "comentarios","fechahoraUpdate","opUpdate","post"));
    	    list.add(Aggregation.sort(new Sort(Sort.Direction.ASC,"fechahoraUpdate")));
    	    TypedAggregation<Registro> agg = Aggregation.newAggregation(Registro.class, list);
    	    Comparator<ZonedDateTime> comparator = Comparator.comparing(
    	            zdt -> zdt.truncatedTo(ChronoUnit.MINUTES));
    	    
    	    List<Registro>  results =mongoOperations.aggregate(agg, Registro.class, Registro.class).getMappedResults();
    	    results.forEach((r)-> r.setComentarios( new ArrayList(r.getComentarios().stream().collect(Collectors.groupingBy(Comentario::getPaciente,Collectors
    	    		.collectingAndThen(Collectors.reducing((Comentario d1, Comentario d2) -> comparator.compare(d1.getFechaHora(),d2.getFechaHora())>0 ? d1 : d2), (Optional<Comentario> com)-> com.get()))).values())));
    	    return results;
    }  
    
    
    /**
     *  Get one registro by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Registro findOne(String id) {
        log.debug("Request to get Registro : {}", id);
        Registro registro = registroRepository.findOne(id);
        return registro;
    }

    /**
     *  Delete the  registro by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Registro : {}", id);
        registroRepository.delete(id);
    }
    
    
    
    
    
    
    
    
    
}
