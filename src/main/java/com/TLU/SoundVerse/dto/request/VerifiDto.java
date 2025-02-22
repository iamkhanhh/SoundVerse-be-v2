package com.TLU.SoundVerse.dto.request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifiDto {
    private String email;
    private String verificationCode;
}
