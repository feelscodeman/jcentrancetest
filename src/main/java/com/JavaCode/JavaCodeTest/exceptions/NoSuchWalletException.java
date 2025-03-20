package com.JavaCode.JavaCodeTest.exceptions;

public class NoSuchWalletException extends RuntimeException {
  public NoSuchWalletException(String message) {
    super(message);
  }
}
