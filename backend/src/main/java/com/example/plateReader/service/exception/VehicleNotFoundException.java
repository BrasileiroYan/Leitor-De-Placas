package com.example.plateReader.service.exception;

public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(String plate) {
        super("Veículo com placa [" + plate + "] não encontrado.");
    }
}
