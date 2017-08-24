package pe.com.hatcc.ms.posts.repository;

import pe.com.hatcc.ms.posts.domain.Variable;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Variable entity.
 */
@SuppressWarnings("unused")
public interface VariableRepository extends MongoRepository<Variable,String> {

}
