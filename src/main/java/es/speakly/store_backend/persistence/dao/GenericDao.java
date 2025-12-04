package es.speakly.store_backend.persistence.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    List<T> findAll(int pageNumber, int pageSize);
    Optional<T> findById(Long id);
    T insert(T entity);
    T update(T entity);
    void deleteById(Long id);
    long count();
}