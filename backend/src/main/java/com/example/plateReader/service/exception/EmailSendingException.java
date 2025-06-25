package com.example.plateReader.service.exception;

public class EmailSendingException extends RuntimeException {
  public EmailSendingException(String message) {
    super(message);
  }
}
