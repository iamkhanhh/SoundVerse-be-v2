package com.TLU.SoundVerse.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    @Size(min = 3, message = "Username must be at least 3 characters")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private String gender;

    private String country;

    private String fullName;

    private String profilePicImage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dob;
}
