package com.SCAR.Exception;

import com.SCAR.Account.AccountNotFoundException;
import com.SCAR.Account.AccountNotValidException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class CustomizedGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                NotValidExceptionResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Arguments Not Valid")
                        .developerMessage(ex.getClass().getName())
                        .err(errorList)
                        .build(), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AccountNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomizedNotValidResponse> handleCustomizedNotValidException(AccountNotValidException ex, WebRequest webRequest) {
        return new ResponseEntity<>(
                CustomizedNotValidResponse.builder()
                        .CustomValidError(ex.getErrorList())
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Email Already Exists Or NickName Already Exists")
                        .developerMessage(ex.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST
        );
    }
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<ExceptionResponse> handleAllException(Exception ex, WebRequest webRequest) {
//
//        ExceptionResponse exceptionResponse =
//                new ExceptionResponse(LocalDateTime.now(), ex.getMessage(), webRequest.getDescription(false));
//
//        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    @ExceptionHandler(AccountNotFoundException.class)
//    public ResponseEntity<ExceptionResponse> handleAccountNotFoundException(Exception ex, WebRequest webRequest) {
//
//        ExceptionResponse exceptionResponse =
//                new ExceptionResponse(
//
//                );
//
//        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.NOT_FOUND);
//    }


}
