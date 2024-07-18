package br.com.api.educa.courseservice.validators;

import br.com.api.educa.courseservice.exception.FlowException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Log4j2
public class CourseValidator {

    public void hasFieldValid(String field) {
        log.info("CourseValidator.isFieldValid - entering flow");
        if (field.isEmpty()) throw new FlowException(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                "This field [" + field + "] cannot be empty"
        );
        log.info("CourseValidator.isFieldValid - exiting flow");
    }
}
