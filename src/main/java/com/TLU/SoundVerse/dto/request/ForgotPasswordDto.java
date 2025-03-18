package com.TLU.SoundVerse.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ForgotPasswordDto {
    private String email;

    private String newPassword;

}
