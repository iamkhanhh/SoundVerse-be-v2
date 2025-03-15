package com.TLU.SoundVerse.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.TLU.SoundVerse.dto.request.RegisterUserDto;
import com.TLU.SoundVerse.dto.response.UserResponse;
import com.TLU.SoundVerse.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "profilePicImage", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "verificationCode", ignore = true)
    @Mapping(target = "verificationCodeExpiresAt", ignore = true)
    User toUser(RegisterUserDto request);
    // void updateUser(@MappingTarget User user, UpdateUserDto updateUserDto);

    UserResponse toUserResponse(User user);
}
