package com.example.plateReader.service.exception;

public class CrimePlateNotFoundException extends RuntimeException {
  public CrimePlateNotFoundException(Long id, String plate) { super("O crime de ID [" + id + "] associado à placa [" + plate + "] não foi encontrado"); }
}
