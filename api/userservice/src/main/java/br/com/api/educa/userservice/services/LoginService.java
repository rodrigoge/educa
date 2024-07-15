package br.com.api.educa.userservice.services;

import br.com.api.educa.userservice.db.Users;
import br.com.api.educa.userservice.dto.LoginRequest;
import br.com.api.educa.userservice.dto.LoginResponse;
import br.com.api.educa.userservice.utils.Utils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Log4j2
public class LoginService {


    @Value("${jwt.secret}")
    private String secret;

    private final AuthenticationManager authenticationManager;
    private final Utils utils;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager, Utils utils) {
        this.authenticationManager = authenticationManager;
        this.utils = utils;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        log.info("LoginService.login - entering flow");
        var tokenAuthentication = new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password());
        var authManager = authenticationManager.authenticate(tokenAuthentication);
        var user = (Users) authManager.getPrincipal();
        var token = createToken(user.getLogin());
        var loginResponse = new LoginResponse(
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                token
        );
        log.info("LoginService.login - finishing flow");
        return loginResponse;
    }

    public String createToken(String login) {
        log.info("TokenService.createToken - entering flow");
        var instantNow = Instant.now();
        var tenMinutesInSeconds = 10 * 60;
        var expiresAt = instantNow.plus(tenMinutesInSeconds, ChronoUnit.SECONDS);
        var token = JWT
                .create()
                .withClaim("login", login)
                .withSubject(login)
                .withIssuedAt(instantNow)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC256(secret));
        log.info("TokenService.createToken - finishing flow");
        return token;
    }

    public String verifyToken(String token) {
        log.info("TokenService.verifyToken - entering flow");
        var validatedToken = utils.verifyToken(token, secret);
        log.info("TokenService.verifyToken - finishing flow");
        return validatedToken;
    }
}
