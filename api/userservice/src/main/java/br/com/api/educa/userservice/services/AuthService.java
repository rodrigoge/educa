package br.com.api.educa.userservice.services;

import br.com.api.educa.userservice.db.UserRepository;
import br.com.api.educa.userservice.exception.FlowException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Log4j2
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("AuthService.loadUserByUsername - finding user by login");
        var userByLogin = userRepository.findByLogin(username);
        if (userByLogin.isEmpty())
            throw new FlowException(
                    HttpStatus.BAD_REQUEST,
                    LocalDateTime.now(),
                    "Login doesn't exists"
            );
        return userByLogin.get();
    }
}
