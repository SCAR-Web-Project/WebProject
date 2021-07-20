package com.SCAR.Exception;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class NotValidExceptionResponse extends ExceptionResponse{
    private final List<String> err;
}
