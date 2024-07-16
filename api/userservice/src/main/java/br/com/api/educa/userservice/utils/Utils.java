package br.com.api.educa.userservice.utils;

import br.com.api.educa.userservice.db.Users;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class Utils {

    private final PasswordEncoder passwordEncoder;

    public Utils(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void encryptPassword(Users user) {
        log.info("Utils.encryptPassword - entering flow");
        var encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        log.info("Utils.encryptPassword - finishing flow");
    }

    public String getPasswordRegex() {
        log.info("Utils.getPasswordRegex - getting regex flow");
        return "^(?=(.*[a-zA-Z]){4,})(?=(.*[0-9]){4,}).*$";
    }

    public String verifyToken(String token, String secret) {
        log.info("TokenService.verifyToken - entering flow");
        var validatedToken = JWT
                .require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getSubject();
        log.info("TokenService.verifyToken - finishing flow");
        return validatedToken;
    }
}
