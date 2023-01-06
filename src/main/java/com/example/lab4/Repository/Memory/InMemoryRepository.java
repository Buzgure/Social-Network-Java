package com.example.lab4.Repository.Memory;

import com.example.lab4.Domain.Entity;
import com.example.lab4.Domain.Validators.Validator;
import com.example.lab4.Repository.Repository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRepository <ID, E extends Entity<ID>> implements Repository<ID,E> {
    private Validator<E> validator;
    Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID,E>();
    }

    @Override
    public Iterable<E> findAll() {
        Set<E> allEntities = entities.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toSet());
        return allEntities;
    }

    @Override
    public Optional<E> findOne(ID id) {
        if (id == null){
            throw new IllegalArgumentException("id must not be null");
        }
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<E> delete(ID id) {
        if (id == null){
            throw new IllegalArgumentException("id must not be null");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        if(entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        validator.validate(entity);
        return Optional.ofNullable(entities.computeIfPresent(entity.getId(), (k,v) -> entity));
    }

    @Override
    public E findByUsernameAndPassword(String userName, String password) {
        return null;
    }

    @Override
    public E findByUsers(Long ID1, Long ID2) {
        return null;
    }

    @Override
    public List<E> getAllAsList() {
        return null;
    }
}
