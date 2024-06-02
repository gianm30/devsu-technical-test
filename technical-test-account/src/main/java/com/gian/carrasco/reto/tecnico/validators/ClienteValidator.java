package com.gian.carrasco.reto.tecnico.validators;

import com.gian.carrasco.reto.tecnico.exception.InvalidIdException;
import com.gian.carrasco.reto.tecnico.exception.IdMustNotBeSendException;
import com.gian.carrasco.reto.tecnico.vo.CuentaRqVO;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class ClienteValidator {
    public static Mono<CuentaRqVO> validateId(CuentaRqVO vo, String id) {
        if(NumbersValidator.isInteger(id)) {
            vo.setId(Integer.parseInt(id));
            return Mono.just(vo);
        }
        return Mono.error(InvalidIdException::new);
    }

    public static Mono<Integer> validateId(String id) {
        if(NumbersValidator.isInteger(id))
            return Mono.just(Integer.parseInt(id));
        return Mono.error(InvalidIdException::new);
    }

    public static Mono<CuentaRqVO> validateIfIdExists(CuentaRqVO vo) {
        Optional<Integer> optional = Optional.ofNullable(vo).map(CuentaRqVO::getId);
        if(optional.isPresent())
            return Mono.error(IdMustNotBeSendException::new);
        return Mono.just(vo);
    }
}