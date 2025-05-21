package com.example.plateReader.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AppUserNotFoundException extends RuntimeException {

    public AppUserNotFoundException(Long id){ super("O usuário de ID [" + id + "] não foi encontrado"); }

    public AppUserNotFoundException(String username){ super("O usuário de e-mail [" + username + "] não foi encontrado"); }
}