package pe.com.hatcc.ms.posts.service;

import pe.com.hatcc.ms.posts.domain.Contexto;
import pe.com.hatcc.ms.posts.repository.ContextoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Contexto.
 */
@Service
public class ContextoService {

    private final Logger log = LoggerFactory.getLogger(ContextoService.class);
    
    @Inject
    private ContextoRepository contextoRepository;

    /**
     * Save a contexto.
     *
     * @param contexto the entity to save
     * @return the persisted entity
     */
    public Contexto save(Contexto contexto) {
        log.debug("Request to save Contexto : {}", contexto);
        Contexto result = contextoRepository.save(contexto);
        return result;
    }

    /**
     *  Get all the contextos.
     *  
     *  @return the list of entities
     */
    public List<Contexto> findAll() {
        log.debug("Request to get all Contextos");
        List<Contexto> result = contextoRepository.findAll();

        return result;
    }

    /**
     *  Get one contexto by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Contexto findOne(String id) {
        log.debug("Request to get Contexto : {}", id);
        Contexto contexto = contextoRepository.findOne(id);
        return contexto;
    }

    /**
     *  Delete the  contexto by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Contexto : {}", id);
        contextoRepository.delete(id);
    }
}
