package br.com.api.educa.userservice.services;

import br.com.api.educa.userservice.db.UserRepository;
import br.com.api.educa.userservice.exception.FlowException;
import br.com.api.educa.userservice.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Log4j2
public class LogoutService implements LogoutHandler {

    @Value("${jwt.secret}")
    private String secret;

    private final UserRepository userRepository;
    private final Utils utils;

    public LogoutService(UserRepository userRepository,
                         Utils utils) {
        this.userRepository = userRepository;
        this.utils = utils;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("LogoutService.logout - entering");
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            var token = authHeader.replace("Bearer ", "");
            var login = utils.verifyToken(token, secret);
            var user = userRepository.findByLogin(login);
            if (user.isEmpty())
                throw new FlowException(
                        HttpStatus.BAD_REQUEST,
                        LocalDateTime.now(),
                        "This login doesn't exists"
                );
            SecurityContextHolder.clearContext();
        }
        log.info("LogoutService.logout - finishing");
    }
}
