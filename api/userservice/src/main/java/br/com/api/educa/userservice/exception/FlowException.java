package br.com.api.educa.userservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FlowException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final LocalDateTime dateTime;
    private final String message;
}
