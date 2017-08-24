package pe.com.hatcc.ms.posts.web.rest;

import com.codahale.metrics.annotation.Timed;
import pe.com.hatcc.ms.posts.domain.Variable;
import pe.com.hatcc.ms.posts.service.VariableService;
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
 * REST controller for managing Variable.
 */
@RestController
@RequestMapping("/api")
public class VariableResource {

    private final Logger log = LoggerFactory.getLogger(VariableResource.class);
        
    @Inject
    private VariableService variableService;

    /**
     * POST  /variables : Create a new variable.
     *
     * @param variable the variable to create
     * @return the ResponseEntity with status 201 (Created) and with body the new variable, or with status 400 (Bad Request) if the variable has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/variables",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Variable> createVariable(@RequestBody Variable variable) throws URISyntaxException {
        log.debug("REST request to save Variable : {}", variable);
        if (variable.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("variable", "idexists", "A new variable cannot already have an ID")).body(null);
        }
        Variable result = variableService.save(variable);
        return ResponseEntity.created(new URI("/api/variables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("variable", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /variables : Updates an existing variable.
     *
     * @param variable the variable to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated variable,
     * or with status 400 (Bad Request) if the variable is not valid,
     * or with status 500 (Internal Server Error) if the variable couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/variables",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Variable> updateVariable(@RequestBody Variable variable) throws URISyntaxException {
        log.debug("REST request to update Variable : {}", variable);
        if (variable.getId() == null) {
            return createVariable(variable);
        }
        Variable result = variableService.save(variable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("variable", variable.getId().toString()))
            .body(result);
    }

    /**
     * GET  /variables : get all the variables.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of variables in body
     */
    @RequestMapping(value = "/variables",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Variable> getAllVariables() {
        log.debug("REST request to get all Variables");
        return variableService.findAll();
    }

    /**
     * GET  /variables/:id : get the "id" variable.
     *
     * @param id the id of the variable to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the variable, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/variables/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Variable> getVariable(@PathVariable String id) {
        log.debug("REST request to get Variable : {}", id);
        Variable variable = variableService.findOne(id);
        return Optional.ofNullable(variable)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /variables/:id : delete the "id" variable.
     *
     * @param id the id of the variable to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/variables/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVariable(@PathVariable String id) {
        log.debug("REST request to delete Variable : {}", id);
        variableService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("variable", id.toString())).build();
    }

}
