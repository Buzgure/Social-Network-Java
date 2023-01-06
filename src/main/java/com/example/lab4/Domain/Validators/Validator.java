package com.example.lab4.Domain.Validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
