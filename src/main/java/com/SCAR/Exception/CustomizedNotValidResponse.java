package com.SCAR.Exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class CustomizedNotValidResponse extends ExceptionResponse{
    private final List<String> CustomValidError;
}
