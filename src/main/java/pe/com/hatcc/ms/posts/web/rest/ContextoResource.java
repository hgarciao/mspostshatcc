package pe.com.hatcc.ms.posts.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.com.hatcc.ms.posts.domain.Contexto;
import pe.com.hatcc.ms.posts.service.ContextoService;
import pe.com.hatcc.ms.posts.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Contexto.
 */
@RestController
@RequestMapping("/api")
public class ContextoResource {

    private final Logger log = LoggerFactory.getLogger(ContextoResource.class);
        
    @Inject
    private ContextoService contextoService;

    /**
     * POST  /contextos : Create a new contexto.
     *
     * @param contexto the contexto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contexto, or with status 400 (Bad Request) if the contexto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contextos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contexto> createContexto(@RequestBody Contexto contexto) throws URISyntaxException {
        log.debug("REST request to save Contexto : {}", contexto);
        if (contexto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contexto", "idexists", "A new contexto cannot already have an ID")).body(null);
        }
        Contexto result = contextoService.save(contexto);
        return ResponseEntity.created(new URI("/api/contextos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contexto", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contextos : Updates an existing contexto.
     *
     * @param contexto the contexto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contexto,
     * or with status 400 (Bad Request) if the contexto is not valid,
     * or with status 500 (Internal Server Error) if the contexto couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contextos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contexto> updateContexto(@RequestBody Contexto contexto) throws URISyntaxException {
        log.debug("REST request to update Contexto : {}", contexto);
        if (contexto.getId() == null) {
            return createContexto(contexto);
        }
        Contexto result = contextoService.save(contexto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contexto", contexto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contextos : get all the contextos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contextos in body
     */
    @RequestMapping(value = "/contextos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Contexto> getAllContextos() {
        log.debug("REST request to get all Contextos");
        return contextoService.findAll();
    }

    /**
     * GET  /contextos/:id : get the "id" contexto.
     *
     * @param id the id of the contexto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contexto, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/contextos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contexto> getContexto(@PathVariable String id) {
        log.debug("REST request to get Contexto : {}", id);
        Contexto contexto = contextoService.findOne(id);
        return Optional.ofNullable(contexto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contextos/:id : delete the "id" contexto.
     *
     * @param id the id of the contexto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/contextos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteContexto(@PathVariable String id) {
        log.debug("REST request to delete Contexto : {}", id);
        contextoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contexto", id.toString())).build();
    }

}
