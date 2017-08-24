package pe.com.hatcc.ms.posts.service;

import pe.com.hatcc.ms.posts.domain.Variable;
import pe.com.hatcc.ms.posts.repository.VariableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Variable.
 */
@Service
public class VariableService {

    private final Logger log = LoggerFactory.getLogger(VariableService.class);
    
    @Inject
    private VariableRepository variableRepository;

    /**
     * Save a variable.
     *
     * @param variable the entity to save
     * @return the persisted entity
     */
    public Variable save(Variable variable) {
        log.debug("Request to save Variable : {}", variable);
        Variable result = variableRepository.save(variable);
        return result;
    }

    /**
     *  Get all the variables.
     *  
     *  @return the list of entities
     */
    public List<Variable> findAll() {
        log.debug("Request to get all Variables");
        List<Variable> result = variableRepository.findAll();

        return result;
    }

    /**
     *  Get one variable by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Variable findOne(String id) {
        log.debug("Request to get Variable : {}", id);
        Variable variable = variableRepository.findOne(id);
        return variable;
    }

    /**
     *  Delete the  variable by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Variable : {}", id);
        variableRepository.delete(id);
    }
}
