package com.SCAR.Account;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class AccountNotValidException extends RuntimeException{
    private final List<String> errorList;
    public AccountNotValidException(List<String> errList, String m) {
        super(m);
        this.errorList = errList;
    }
}
