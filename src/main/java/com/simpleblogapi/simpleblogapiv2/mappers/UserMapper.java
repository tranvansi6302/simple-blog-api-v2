package com.simpleblogapi.simpleblogapiv2.mappers;

import com.simpleblogapi.simpleblogapiv2.dtos.requests.LogoutRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.UserCreateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.UserUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.UserResponse;
import com.simpleblogapi.simpleblogapiv2.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "fullname", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    void toUser(@MappingTarget User user, UserUpdateRequest request);
}
