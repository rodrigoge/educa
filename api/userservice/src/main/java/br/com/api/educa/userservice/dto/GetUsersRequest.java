package br.com.api.educa.userservice.dto;

import br.com.api.educa.userservice.db.OrderByEnum;
import br.com.api.educa.userservice.db.SortByEnum;
import br.com.api.educa.userservice.db.UserProfileTypeEnum;

import java.util.Set;

public record GetUsersRequest(
        String name,
        String email,
        String login,
        UserProfileTypeEnum profile,
        Set<String> preferences,
        Integer offset,
        Integer limit,
        SortByEnum sort,
        OrderByEnum order
) {
}
