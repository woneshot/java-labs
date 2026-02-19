package com.server.interfaces;

import java.util.List;

public interface Service<T> {
    void create(T entity);
    T getById(int id);
    List<T> getAll();
    void update(T entity);
    void delete(int id);
}