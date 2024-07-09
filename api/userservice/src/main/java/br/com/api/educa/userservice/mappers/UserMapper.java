package br.com.api.educa.userservice.mappers;

import br.com.api.educa.userservice.db.Users;
import br.com.api.educa.userservice.dto.UserRequest;
import br.com.api.educa.userservice.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    Users toUsers(UserRequest userRequest);

    UserResponse toUserResponse(Users users);
}
