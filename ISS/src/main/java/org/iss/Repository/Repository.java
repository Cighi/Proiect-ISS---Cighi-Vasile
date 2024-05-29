package org.iss.Repository;


import org.iss.Domain.Identifiable;

import java.util.Collection;
import java.util.Optional;

public interface Repository<ID,E extends Identifiable<ID>> {
    /**
     * Returns an {@code Optional} containing the searched Entity if found
     * @param id - id of entity
     * @return {@code Optional<Entity>}
     */

    E findOne(ID id);

    /**
     * Returns all Entities as an {@code Iterable}
     * @return all Entities
     */
    Collection<E> findAll();

    /**
     * Adds an Entity to the data source and returns an {@code Optional} containing the Entity if added, empty otherwise
     * @param entity - entity to be added
     * @return {@code Optional<Entity>}
     */
    E add(E entity);

    /**
     * Deletes the Entity from the data source by its unique ID and returns an {@code Boolean} that if true if deleted, false otherwise
     * @param id - unique id to delete entity
     * @return Boolean
     */
    Boolean delete(ID id);

    /**
     * Updates the Entity found by entity's ID and returns the updated entity, null otherwise
     * @param entity - updated entity to be put in the data source
     * @return {@code Optional<Entity>}
     */
    E update(E entity);


}
