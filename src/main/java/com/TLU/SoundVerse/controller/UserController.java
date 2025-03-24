package com.TLU.SoundVerse.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.request.RegisterUserDto;
import com.TLU.SoundVerse.dto.request.UserUpdateDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.entity.User;
import com.TLU.SoundVerse.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
  UserService userService;

  @PostMapping
  ApiResponse<User> createUser(@RequestBody @Valid RegisterUserDto createUserDto) {
    ApiResponse<User> apiResponse = new ApiResponse<User>();

    apiResponse.setStatus("success");
    apiResponse.setMessage("Create user successfilly");
    apiResponse.setData(userService.create(createUserDto));
    return apiResponse;
  }

  @DeleteMapping("/{userId}")
  ApiResponse<Void> deletUser(@PathVariable String userId) {
    userService.deleteUser(userId);

    return ApiResponse.<Void>builder()
        .status("success")
        .message("User deleted successfully")
        .code(200)
        .build();
  }

  @PutMapping("/update")
  public ApiResponse<Void> updateUser(HttpServletRequest request, @RequestBody UserUpdateDto updateDto) {
    userService.updateUser(request, updateDto);
    return ApiResponse.<Void>builder()
        .status("success")
        .message("Update prfile successfully")
        .code(200)
        .build();
  }
}