package br.com.api.educa.userservice.services;

import br.com.api.educa.userservice.db.UserRepository;
import br.com.api.educa.userservice.dto.UserRequest;
import br.com.api.educa.userservice.dto.UserResponse;
import br.com.api.educa.userservice.exception.FlowException;
import br.com.api.educa.userservice.mappers.UserMapper;
import br.com.api.educa.userservice.utils.Utils;
import br.com.api.educa.userservice.validators.UserValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Log4j2
public class UserService {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utils utils;

    public UserResponse createUser(UserRequest userRequest) {
        log.info("UserService.createUser - starting flow");
        validateUserFields(userRequest);
        log.info("Mapping request to valid object");
        var user = userMapper.toUsers(userRequest);
        log.info("Validating user password");
        if (!userValidator.hasPasswordValid(user.getPassword()))
            throw new FlowException(
                    HttpStatus.BAD_REQUEST,
                    LocalDateTime.now(),
                    "Password must have at least 4 letters and 4 numbers"
            );
        log.info("Encrypting user password");
        utils.encryptPassword(user);
        log.info("Saving user in database");
        var userSaved = userRepository.save(user);
        log.info("Mapping response to valid object");
        var userResponse = userMapper.toUserResponse(userSaved);
        log.info("UserService.createUser - exiting flow");
        return userResponse;
    }

    private void validateUserFields(UserRequest userRequest) {
        log.info("UserService.validateUserFields - entering flow");
        if (!userRequest.name().isEmpty()) userValidator.hasFieldValid(userRequest.name());
        if (!userRequest.email().isEmpty()) userValidator.hasFieldValid(userRequest.email());
        if (!userRequest.login().isEmpty()) userValidator.hasFieldValid(userRequest.login());
        if (!userRequest.password().isEmpty()) userValidator.hasFieldValid(userRequest.password());
        if (!userRequest.profile().name().isEmpty()) userValidator.hasFieldValid(userRequest.profile().name());
        log.info("UserService.validateUserFields - finishing flow");
    }

    public String deleteUser(UUID userId) {
        log.info("UserService.deleteUser - entering flow");
        var user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new FlowException(
                    HttpStatus.BAD_REQUEST,
                    LocalDateTime.now(),
                    "This user doesn't exists"
            );
        userRepository.deleteById(userId);
        log.info("UserService.deleteUser - finishing flow");
        return "User has been deleted successfully";
    }
}
