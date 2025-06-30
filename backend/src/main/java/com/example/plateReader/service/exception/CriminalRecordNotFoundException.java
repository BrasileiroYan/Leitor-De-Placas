package com.example.plateReader.service.exception;

public class CriminalRecordNotFoundException extends RuntimeException {

    public CriminalRecordNotFoundException(String message) {
        super(message);
    }

    public CriminalRecordNotFoundException(Long id) {
        super("Ficha Criminal com Id [" + id + "] não encontrada.");
    }
}
