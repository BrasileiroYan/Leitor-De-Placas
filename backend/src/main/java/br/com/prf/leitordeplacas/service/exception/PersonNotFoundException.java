package br.com.prf.leitordeplacas.service.exception;

public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(String message) {
        super(message);
    }

    public PersonNotFoundException(Long id) {
        super("Pessoa com Id [" + id + "] não encontrada.");
    }
}
