package br.com.api.educa.userservice.api;

import br.com.api.educa.userservice.dto.LoginRequest;
import br.com.api.educa.userservice.dto.LoginResponse;
import br.com.api.educa.userservice.services.LoginService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Log4j2
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("LoginController.login - starting flow");
        var response = loginService.login(loginRequest);
        log.info("LoginController.login - finishing flow");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
