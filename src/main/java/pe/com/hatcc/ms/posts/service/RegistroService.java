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

import com.amazonaws.services.autoscaling.model.Filter;
import com.mongodb.Mongo;
import com.mongodb.operation.DistinctOperation;

import javax.inject.Inject;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    
    public List<Registro> findAllAccordingToPaciente(Map parametros) {
        log.debug("Request to get all Registros");
        List<Registro> result;
        if(parametros.get("pacientefiltro")==null){
        	 ZonedDateTime dateTime = parametros.get("fechahora")==null?ZonedDateTime.parse("2500-01-01T10:15:30+01:00[Europe/Paris]"):ZonedDateTime.parse((String)parametros.get("fechahora"));
             result = registroRepository.findAllAccordingToPaciente((String)parametros.get("paciente"),
             		dateTime,new PageRequest(0, (int)parametros.get("pagesize"),new Sort(Sort.Direction.DESC, "fechahora")));
        }else{
        	ZonedDateTime dateTime = parametros.get("fechahora")==null?ZonedDateTime.parse("2500-01-01T10:15:30+01:00[Europe/Paris]"):ZonedDateTime.parse((String)parametros.get("fechahora"));
            result = registroRepository.findAllAccordingToPacienteByPaciente((String)parametros.get("pacientefiltro"),
            		dateTime,new PageRequest(0, (int)parametros.get("pagesize"),new Sort(Sort.Direction.DESC, "fechahora")));
        }
       
        
        return result;
    }  
    
    
    /**
     *  Get all the registros of one pacient for his own registries page (only visible for him).
     *  
     *  @return the list of entities
     *  Paciente - obligatorio
     *  parametros.get("pacient");
     *  Tamaño de pagina - obligatorio
     *  parametros.get("pagesize");
     *  Filtro Texto - Default vacio 
     *  parametros.get("filter");
     *  Ordenar por - Default fecha-hora
     *  parametros.get("orderby");
     *  Numero de pagina - Default pagina 1
     *  parametros.get("pagenumber");
     */
    
    public List<Registro> findAllByPacienteFilterPagination(Map parametros) {
        log.debug("Request to get all Registros");
        List<Registro> result =  new ArrayList<Registro>();
        
        
        if ( parametros.get("pacient")==null | parametros.get("pagesize")==null ) {
        	//Retorna vacio porque esos campos son mandatorios
        	return result;
        }else {
        	 String pacient = (String) parametros.get("pacient");
        	 int pagesize = ((Integer)parametros.get("pagesize")).intValue();
        	 String orderby = parametros.get("orderby")==null | ((String)parametros.get("orderby")).isEmpty() ?"fechahora": (String)parametros.get("orderby");
        	 String pagenumber = parametros.get("pagenumber")==null | ((String)parametros.get("pagenumber")).isEmpty() ?"1": (String)parametros.get("pagenumber");
        	
        	 String filter  = parametros.get("filter")==null ? "": (String)parametros.get("filter");
        	 String date = parametros.get("date")==null? "": (String)parametros.get("date");
        	 
        	 
        	 if(!date.isEmpty() && !filter.isEmpty()) {
        		 System.out.println("Ambos filtros");
        		 ZonedDateTime dateTimeBottom = ZonedDateTime.parse(date);
        		 ZonedDateTime dateTimeTop = dateTimeBottom.plus(1,ChronoUnit.DAYS);
        		 result = registroRepository.findAllByPacienteFilterPagination(
        				 pacient,
        				 filter,
        				 dateTimeTop,
        				 dateTimeBottom,
        				 new PageRequest(Integer.parseInt(pagenumber)-1, //numero de pagina
        						 		pagesize, // tamaño de pagina
        						 		new Sort(Sort.Direction.DESC, orderby))); // order by
        	 }else{
        		 if(date.isEmpty()){
        			 System.out.println("Filtro Normal");
        			 result = registroRepository.findAllByPacienteFilterPagination(
            				 pacient,
            				 filter,
            				 new PageRequest(Integer.parseInt(pagenumber)-1, //numero de pagina
            						 		pagesize, // tamaño de pagina
            						 		new Sort(Sort.Direction.DESC, orderby))); // order by 
        		 }else 
        		 if(filter.isEmpty()) {
        			 System.out.println("Filtro Fecha");
        			 ZonedDateTime dateTimeBottom = ZonedDateTime.parse(date);
            		 ZonedDateTime dateTimeTop = dateTimeBottom.plus(1,ChronoUnit.DAYS);
            		 result = registroRepository.findAllByPacienteFilterPagination(
            				 pacient,
            				 dateTimeTop,
            				 dateTimeBottom,
            				 new PageRequest(Integer.parseInt(pagenumber)-1, //numero de pagina
            						 		pagesize, // tamaño de pagina
            						 		new Sort(Sort.Direction.DESC, orderby))); // order by
        		 }
        	 }	
        	return result;
        } 
    } 
    
    
    
    /**
     *  Get all the registros.
     *  
     *  @return the list of entities
     */
    
    public List<Registro> findAllAccordingToPacienteSuscrito(String paciente) {
    	List suscrito =  new ArrayList<String>();
    	suscrito.add(paciente);
    	List<AggregationOperation> list = new ArrayList<AggregationOperation>();
    	 	list.add(Aggregation.match(Criteria.where("suscritos").all(suscrito)));
    	 	Criteria criteria = new Criteria();
    	 	list.add(Aggregation.match(criteria.orOperator(Criteria.where("eliminado").is(false).andOperator(Criteria.where("oculto").is(false))
    	 			,Criteria.where("eliminado").is(false).andOperator(Criteria.where("oculto").is(true).andOperator(Criteria.where("paciente").is(paciente))))));    	 	
    	    list.add(Aggregation.unwind("comentarios"));
    	    list.add(Aggregation.match(Criteria.where("comentarios.paciente").ne(paciente)));
    	    list.add(Aggregation.sort(new Sort(Sort.Direction.DESC,"comentarios.paciente")));
    	    list.add(Aggregation.sort(new Sort(Sort.Direction.DESC,"comentarios.fechaHora")));
    	    list.add(Aggregation.group("fechahoraUpdate","pensamiento","opUpdate","pacienteUpdate").push("comentarios").as("comentarios").first("id").as("post"));
    	    list.add(Aggregation.project("pensamiento", "comentarios","fechahoraUpdate","opUpdate","post","pacienteUpdate"));
    	    list.add(Aggregation.sort(new Sort(Sort.Direction.DESC,"fechahoraUpdate")));
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
    
  
    public Registro findOneByPaciente(String id, String username) {
        log.debug("Request to get Registro : {}", id);
        Registro registro = registroRepository.findOneByPaciente(id, username);
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
