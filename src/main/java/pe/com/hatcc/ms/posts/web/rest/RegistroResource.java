package pe.com.hatcc.ms.posts.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pusher.rest.Pusher;

import pe.com.hatcc.ms.posts.domain.Comentario;
import pe.com.hatcc.ms.posts.domain.Notificacion;
import pe.com.hatcc.ms.posts.domain.Registro;
import pe.com.hatcc.ms.posts.service.RegistroService;
import pe.com.hatcc.ms.posts.web.rest.util.HeaderUtil;
import pe.com.hatcc.ms.posts.web.websocket.OutputMessage;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing Registro.
 */
@RestController
@RequestMapping("/api")
public class RegistroResource {

    private final Logger log = LoggerFactory.getLogger(RegistroResource.class);
        
    @Inject
    private RegistroService registroService;
    
    
    private final SimpMessagingTemplate messagingTemplate;
  
    
    public RegistroResource(SimpMessagingTemplate messagingTemplate) {
    	this.messagingTemplate = messagingTemplate;
	}

    /**
     * POST  /registros : Create a new registro.
     *
     * @param registro the registro to create
     * @return the ResponseEntity with status 201 (Created) and with body the new registro, or with status 400 (Bad Request) if the registro has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    
    @RequestMapping(value = "/registros",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Registro> createRegistro(@RequestBody @DateTimeFormat(pattern = "dd.MM.yyyy") Registro registro) throws URISyntaxException {
        log.debug("REST request to save Registro : {}", registro);
        if (registro.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("registro", "idexists", "A new registro cannot already have an ID")).body(null);
        }
        Instant now = Instant.now();
		ZoneId zoneId = ZoneId.of("-05:00");
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, zoneId);
		registro.setFechahora(zonedDateTime);
		registro.setFechahoraUpdate(zonedDateTime);
		registro.setOpUpdate("crear");
        Registro result = registroService.save(registro);
        /*Mandar notificaciones a todos los usuarios*/
        this.messagingTemplate.convertAndSend("/topic/registros", result);
        /**/
        
        return ResponseEntity.created(new URI("/api/registros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("registro", result.getId().toString()))
            .body(result);
    }
    

    /**
     * PUT  /registros : Updates an existing registro.
     *
     * @param registro the registro to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated registro,
     * or with status 400 (Bad Request) if the registro is not valid,
     * or with status 500 (Internal Server Error) if the registro couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/registros",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Registro> updateRegistro(@RequestBody Registro registro) throws URISyntaxException {
        log.debug("REST request to update Registro : {}", registro);
        if (registro.getId() == null) {
            return createRegistro(registro);
        }
        Registro result = registroService.save(registro);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("registro", registro.getId().toString()))
            .body(result);
    }
    
    
    /**
     * PUT  /registros : Updates an existing registro.
     *
     * @param registro the registro to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated registro,
     * or with status 400 (Bad Request) if the registro is not valid,
     * or with status 500 (Internal Server Error) if the registro couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/registros/{operacion}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Registro> updateRegistro(@RequestBody Registro registro,@PathVariable int operacion) throws URISyntaxException {
        log.debug("REST request to update Registro : {}", registro);
        if (registro.getId() == null) {
            return createRegistro(registro);
        }
        Instant now = Instant.now();
		ZoneId zoneId = ZoneId.of("-05:00");
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, zoneId);
		registro.setFechahoraUpdate(zonedDateTime);
        Registro result = null;
        switch (operacion) {
		//Eliminar comentario
        case 0:
        	registro.setOpUpdate("eliminar.comentario");
        	result = registroService.save(registro);
			break;
		//Realiza comentario
        case 1:	
			Optional<Comentario> matchingObjects = registro.getComentarios().stream().filter(c -> c.getId()==null ).findFirst();
    		Comentario comentario = matchingObjects.get();
    		now = Instant.now();
    		zoneId = ZoneId.of("-05:00");
    		zonedDateTime = ZonedDateTime.ofInstant(now, zoneId);
    		comentario.setFechaHora(zonedDateTime);
    		comentario.setId((new ObjectId()).toString());
    		registro.setOpUpdate("crear.comentario");
    		result = registroService.save(registro);
			break;
		//Eliminar registro
        case 2:	
        	registro.setOpUpdate("eliminar");
        	registro.setEliminado(true);
        	result = registroService.save(registro);
			break;
		
        //Ocultar registro
	    case 3:	
	    	registro.setOpUpdate("ocultar");
	    	registro.setOculto(true);
	    	result = registroService.save(registro);
			break;
		//Des-Ocultar registro
	    case 4:	
	    	registro.setOpUpdate("mostrar");
	    	registro.setOculto(false);
	    	result = registroService.save(registro);
			break;
		}
        
        /*Mandar notificaciones a todos los usuarios suscritos
         * por ahora solo al dueño*/
        this.messagingTemplate.convertAndSend("/topic/registros", result);
        /**/
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("registro", registro.getId().toString()))
            .body(result);
    }
    
    

    /**
     * GET  /registros : get all the registros.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of registros in body
     */
    @RequestMapping(value = "/registros",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Registro> getAllRegistros() {
        log.debug("REST request to get all Registros");
        return registroService.findAll();
    }
    
    /**/
    
    /**
     * GET  /registros : get all the registros.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of registros in body
     */
    @RequestMapping(value = "/registros/paciente/{username}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Registro> getRegistrosByPaciente(@PathVariable String username) {
        log.debug("REST request to get all Registros");
        return registroService.findAllByPaciente(username);
    }
    
    
    
    //WS PARA EL WALL
    
    /**
     * GET  /registros : get all the registros.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of registros in body
     */
    @RequestMapping(value = "/registros/pacientes/wall",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Registro> getAllRegistrosAccordingPaciente(@RequestBody Map<String, Object> parametros) {
        log.debug("REST request to get all Registros");
        return registroService.findAllAccordingToPaciente(parametros);
    }
    
    
    //WS PARA LAS NOTIFICACIONES
    
    /**
     * GET  /registros : get all the registros.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of registros in body
     */
    @RequestMapping(value = "/registros/pacientes/{username}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Registro> getAllRegistrosAccordingPacienteSuscrito(@PathVariable String username) {
        log.debug("REST request to get all Registros");
        return registroService.findAllAccordingToPacienteSuscrito(username);
    }
    
    
    
    /**
     * GET  /registros/:id : get the "id" registro.
     *
     * @param id the id of the registro to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the registro, or with status 404 (Not Found)
     */
    
    @RequestMapping(value = "/registros/{username}/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<Registro> getRegistroByPaciente(@PathVariable String username, @PathVariable String id) {
            log.debug("REST request to get Registro : {}", id);
            Registro registro = registroService.findOneByPaciente(id, username);
            return Optional.ofNullable(registro)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    
    
    
    /**
     * DELETE  /registros/:id : delete the "id" registro.
     *
     * @param id the id of the registro to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/registros/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRegistro(@PathVariable String id) {
        log.debug("REST request to delete Registro : {}", id);
        registroService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("registro", id.toString())).build();
    }
    
    
    
    
    

}
