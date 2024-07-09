package br.com.api.educa.userservice.api;

import br.com.api.educa.userservice.dto.UserRequest;
import br.com.api.educa.userservice.dto.UserResponse;
import br.com.api.educa.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
        log.info("UserController.deleteUser - starting flow");
        var response = userService.deleteUser(userId);
        log.info("UserController.deleteUser - finishing flow");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId,
                                                   @Valid @RequestBody UserRequest userRequest) {
        log.info("UserController.updateUser - starting flow");
        var response = userService.updateUser(userId, userRequest);
        log.info("UserController.updateUser - finishing flow");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
