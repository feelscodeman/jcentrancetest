package com.JavaCode.JavaCodeTest.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CentralizedXsHandler {

    @ExceptionHandler(NoSuchWalletException.class)
    public ResponseEntity<String> exceptionNoSuchWalletExceptionHandler(NoSuchWalletException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public ResponseEntity<String> exceptionNotEnoughBalanceExceptionHandler(NotEnoughBalanceException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<String> exceptionUnsupportedOperationExceptionHandler(UnsupportedOperationException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }
}
