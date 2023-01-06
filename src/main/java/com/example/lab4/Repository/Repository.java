package com.example.lab4.Repository;

import com.example.lab4.Domain.Entity;

import java.util.List;
import java.util.Optional;

public interface Repository<ID, E extends Entity<ID>> {
    Iterable<E> findAll();
    Optional<E> findOne(ID id);
    Optional<E> save(E entity);
    Optional<E> delete(ID id);
    Optional<E> update(E entity);

    E findByUsernameAndPassword(String userName, String password);

    E findByUsers(Long ID1, Long ID2);

    List<E> getAllAsList();




}
