package br.com.api.educa.userservice.utils;

import br.com.api.educa.userservice.db.Users;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class Utils {

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
