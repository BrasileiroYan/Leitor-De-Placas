package com.example.plateReader.service.exception;

public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(String plate) {
        super("Pessoa com a placa [" + plate + "] não encontrada.");
    }
}
