package br.com.api.educa.userservice.dto;

import java.util.List;

public record GetUsersResponse(
        List<UserResponse> users,
        Integer totalRecords
) {
}
