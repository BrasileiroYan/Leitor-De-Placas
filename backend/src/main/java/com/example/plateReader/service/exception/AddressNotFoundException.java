package com.example.plateReader.service.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(String message) {
        super(message);
    }

    public AddressNotFoundException(Long id) {
        super("O endereço com id [" + id + "] não foi encontrado.");
    }
}
