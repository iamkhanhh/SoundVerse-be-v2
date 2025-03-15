package com.TLU.SoundVerse.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserResponse {
  Integer id;

  String username;

  String email;

  String gender;

  String country;

  String profilePicImage;

  String fullName;
  
  String dob;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  LocalDateTime createdAt;
}
