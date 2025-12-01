package com.pjatk.core.exception;

public class TicketCannotBeBookedException extends RuntimeException {
  public TicketCannotBeBookedException(String message) {
    super(message);
  }
}
