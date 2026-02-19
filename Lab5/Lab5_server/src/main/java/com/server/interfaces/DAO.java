package com.server.interfaces;

import java.util.List;

public interface DAO<T>
{

    void create(T entity);

    T findById(int id);

    List<T> findAll();

    void update(T entity);

    void delete(int id);
}
