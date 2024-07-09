package br.com.api.educa.userservice.services;

import br.com.api.educa.userservice.db.UserRepository;
import br.com.api.educa.userservice.db.Users;
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
        log.info("UserService.createUser - validating user password");
        if (!userValidator.hasPasswordValid(user.getPassword()))
            throw new FlowException(
                    HttpStatus.BAD_REQUEST,
                    LocalDateTime.now(),
                    "Password must have at least 4 letters and 4 numbers"
            );
        log.info("UserService.createUser - encrypting user password");
        utils.encryptPassword(user);
        log.info("UserService.createUser - saving user in database");
        var userSaved = userRepository.save(user);
        log.info("UserService.createUser - mapping response to valid object");
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

    public UserResponse updateUser(UUID userId, UserRequest userRequest) {
        log.info("UserService.updateUser - entering flow");
        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new FlowException(
                    HttpStatus.BAD_REQUEST,
                    LocalDateTime.now(),
                    "This user doesn't exists"
            );
        validateUserFields(userRequest);
        var user = optionalUser.get();
        var userMapped = mappingUserUpdate(user, userRequest);
        log.info("UserService.updateUser - validating user password");
        if (!userValidator.hasPasswordValid(userMapped.getPassword()))
            throw new FlowException(
                    HttpStatus.BAD_REQUEST,
                    LocalDateTime.now(),
                    "Password must have at least 4 letters and 4 numbers"
            );
        log.info("UserService.updateUser - encrypting user password");
        utils.encryptPassword(userMapped);
        log.info("UserService.updateUser - saving user in database");
        var userSaved = userRepository.save(user);
        log.info("UserService.updateUser - mapping response to valid object");
        var userResponse = userMapper.toUserResponse(userSaved);
        log.info("UserService.updateUser - finishing flow");
        return userResponse;
    }

    private Users mappingUserUpdate(Users user, UserRequest userRequest) {
        log.info("UserService.mappingUserUpdate - entering flow");
        if (!userRequest.name().isEmpty()) user.setName(userRequest.name());
        if (!userRequest.email().isEmpty()) user.setEmail(userRequest.email());
        if (!userRequest.login().isEmpty()) user.setLogin(userRequest.login());
        if (!userRequest.password().isEmpty()) user.setPassword(userRequest.password());
        if (userRequest.profile() != null) user.setProfile(userRequest.profile());
        if (userRequest.preferences() != null) user.setPreferences(userRequest.preferences());
        log.info("UserService.mappingUserUpdate - finishing flow");
        return user;
    }
}
