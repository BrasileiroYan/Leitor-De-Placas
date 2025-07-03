package br.com.prf.leitordeplacas.service.exception;

public class EmailSendingException extends RuntimeException {
  public EmailSendingException(String message) {
    super(message);
  }
}
