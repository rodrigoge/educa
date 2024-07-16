package br.com.api.educa.userservice.dto;

import br.com.api.educa.userservice.db.UserProfileTypeEnum;

import java.util.Set;

public record UserRequest(
        String name,
        String email,
        String login,
        String password,
        UserProfileTypeEnum profile,
        Set<String> preferences,
        String bio
) {
}
