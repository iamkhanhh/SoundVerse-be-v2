package com.TLU.SoundVerse.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginDto {
  @Email()
  String email;

  @Size(min = 8, message = "Password must be at least 8 characters")
  String password;
}
