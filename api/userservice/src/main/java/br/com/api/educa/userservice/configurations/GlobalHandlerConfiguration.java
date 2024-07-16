package br.com.api.educa.userservice.configurations;

import br.com.api.educa.userservice.exception.FlowException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalHandlerConfiguration {

    public ResponseEntity<FlowException> handlerException(FlowException flowException) {
        return ResponseEntity
                .status(flowException.getHttpStatus())
                .body(new FlowException(
                        flowException.getHttpStatus(),
                        flowException.getDateTime(),
                        flowException.getMessage()
                ));
    }
}
