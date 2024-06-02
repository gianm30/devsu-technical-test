package com.gian.carrasco.reto.tecnico.validators;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumbersValidator {
    public static boolean isInteger(String s) {
        return s != null && s.matches("\\d+");
    }

    public static boolean isGreaterThanZero(Integer numero) {
        return Optional.ofNullable(numero).filter(x -> x > 0).isPresent();
    }
}
