package com.example.plateReader.service.exception;

public class CrimeNotFoundException extends RuntimeException {

  public CrimeNotFoundException(String message) { super(message); }

  public CrimeNotFoundException(Long id, String plate) { super("O crime de ID [" + id + "] associado à placa [" + plate + "] não foi encontrado."); }
}
