package org.iss.Domain;

public interface Validator<T> {
    void validate(T entity) throws DomainException;
}
