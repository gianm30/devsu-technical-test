package com.gian.carrasco.reto.tecnico.validators;

import com.gian.carrasco.reto.tecnico.exception.MissingFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

@Component
public class ValidationHandler {
    private final Validator validator;

    public ValidationHandler(@Autowired Validator validator) {
        this.validator = validator;
    }

    public <T> Mono<T> validateBody(T body) {
        BindingResult errors = new BeanPropertyBindingResult(body, body.getClass().getName());
        validator.validate(body, errors);

        if(errors.hasErrors())
            return Mono.error(new MissingFieldException(errors));
        else
            return Mono.just(body);
    }
}