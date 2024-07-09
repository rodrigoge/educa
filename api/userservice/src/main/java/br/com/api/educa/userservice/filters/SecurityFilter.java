package br.com.api.educa.userservice.filters;

import br.com.api.educa.userservice.db.UserRepository;
import br.com.api.educa.userservice.exception.FlowException;
import br.com.api.educa.userservice.services.LoginService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Log4j2
public class SecurityFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final LoginService loginService;

    @Autowired
    public SecurityFilter(UserRepository userRepository, LoginService loginService) {
        this.userRepository = userRepository;
        this.loginService = loginService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("SecurityFilter.doFilterInternal - entering flow");
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            var token = authHeader.replace("Bearer ", "");
            var login = loginService.verifyToken(token);
            var user = userRepository.findByLogin(login);
            if (user.isEmpty())
                throw new FlowException(
                        HttpStatus.BAD_REQUEST,
                        LocalDateTime.now(),
                        "This login doesn't exists"
                );
            var authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.get().getAuthorities()
            );
            var context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request, response);
        log.info("SecurityFilter.doFilterInternal - finishing flow");
    }
}
