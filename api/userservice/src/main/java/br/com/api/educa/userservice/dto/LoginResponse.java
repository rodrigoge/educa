package br.com.api.educa.userservice.dto;

public record LoginResponse(String name, String login, String email, String token) {
}
