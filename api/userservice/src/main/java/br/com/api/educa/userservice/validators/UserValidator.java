package br.com.api.educa.userservice.validators;

import br.com.api.educa.userservice.exception.FlowException;
import br.com.api.educa.userservice.utils.Utils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Component
@Log4j2
public class UserValidator {

    @Autowired
    private Utils utils;

    public void hasFieldValid(String field) {
        log.info("UserValidator.isFieldValid - entering flow");
        if (field.isEmpty()) throw new FlowException(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                "This field [" + field + "] cannot be empty"
        );
        log.info("UserValidator.isFieldValid - exiting flow");
    }

    public boolean hasPasswordValid(String password) {
        log.info("UserValidator.hasPasswordValid - entering flow");
        var pattern = Pattern.compile(utils.getPasswordRegex());
        var matcher = pattern.matcher(password);
        log.info("UserValidator.hasPasswordValid - exiting flow");
        return matcher.matches();
    }
}
