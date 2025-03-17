package com.TLU.SoundVerse.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.LocalDate;

import com.TLU.SoundVerse.enums.UserRole;
import com.TLU.SoundVerse.enums.UserStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
  Integer id;

  String username;

  String email;

  String gender;

  String country;

  String profilePicImage;

  String fullName;

  UserRole role;

  UserStatus status;
  
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  LocalDate dob;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  LocalDateTime createdAt;
}
