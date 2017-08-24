package pe.com.hatcc.ms.posts.repository;

import pe.com.hatcc.ms.posts.domain.Contexto;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Contexto entity.
 */
@SuppressWarnings("unused")
public interface ContextoRepository extends MongoRepository<Contexto,String> {

}
