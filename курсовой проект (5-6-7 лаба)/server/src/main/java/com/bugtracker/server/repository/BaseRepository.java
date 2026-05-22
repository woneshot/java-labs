package com.bugtracker.server.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {
    T save(T entity);
    T update(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    void delete(Long id);
}