package com.TLU.SoundVerse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.TLU.SoundVerse.dto.request.CreateUserDto;
import com.TLU.SoundVerse.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(target = "id", ignore = true)
    User toUser(CreateUserDto request);

    // void updateUser(@MappingTarget User user, UpdateUserDto updateUserDto);
}
