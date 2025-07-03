package br.com.prf.leitordeplacas.service.exception;

public class AppUserNotFoundException extends RuntimeException {

    public AppUserNotFoundException(Long id){ super("O usuário de ID [" + id + "] não foi encontrado"); }

    public AppUserNotFoundException(String username){ super("O usuário de e-mail [" + username + "] não foi encontrado"); }
}