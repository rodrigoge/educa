package br.com.api.educa.userservice.api;

import br.com.api.educa.userservice.dto.UserRequest;
import br.com.api.educa.userservice.dto.UserResponse;
import br.com.api.educa.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Log4j2
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("UserController.createUser - starting flow");
        var response = userService.createUser(userRequest);
        log.info("UserController.createUser - finishing flow");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
