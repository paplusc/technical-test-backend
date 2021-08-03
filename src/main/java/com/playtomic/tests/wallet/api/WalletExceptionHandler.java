package com.playtomic.tests.wallet.api;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.playtomic.tests.wallet.WalletException;
import com.playtomic.tests.wallet.service.StripeServiceException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WalletExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
             MethodArgumentNotValidException ex,
             HttpHeaders headers,
             HttpStatus status,
             WebRequest request) {
    final List<String> errors = new ArrayList<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
               errors.add(error.getField() + " -> " + error.getDefaultMessage())
    );
    return new ResponseEntity<>(new WalletError(400, "Validation errors", errors), BAD_REQUEST);
  }

  @ExceptionHandler(WalletException.class)
  protected ResponseEntity<Object> handleEntityNotFound(
             WalletException ex) {
    final WalletError walletError = new WalletError(404, ex.getMessage(), null);
    return new ResponseEntity<>(walletError, NOT_FOUND);
  }

  @ExceptionHandler(StripeServiceException.class)
  protected  ResponseEntity<Object> handleStripeException(
             StripeServiceException ex){
    final WalletError walletError = new WalletError(500, "Internal Error", null);
    return new ResponseEntity<>(walletError, INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  protected ResponseEntity<Object> handleException(
             IllegalArgumentException ex) {
    final WalletError walletError = new WalletError(422, ex.getMessage(), null);
    return new ResponseEntity<>(walletError, UNPROCESSABLE_ENTITY);
  }
}
