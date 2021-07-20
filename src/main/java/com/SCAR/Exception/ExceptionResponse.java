package com.SCAR.Exception;

import lombok.*;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionResponse {
    private String title;
    private Integer status;
    private LocalDateTime timestamp;
    private String developerMessage;
}
