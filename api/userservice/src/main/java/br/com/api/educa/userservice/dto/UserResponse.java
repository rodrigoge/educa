package br.com.api.educa.userservice.dto;

import br.com.api.educa.userservice.db.UserProfileTypeEnum;

import java.util.Set;

public record UserResponse(
        String name,
        String email,
        String login,
        UserProfileTypeEnum profile,
        Set<String> preferences,
        String bio
) {
}
