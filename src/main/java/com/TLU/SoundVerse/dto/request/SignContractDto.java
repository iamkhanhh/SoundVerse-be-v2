package com.TLU.SoundVerse.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignContractDto {
  String contractNumber;

  String contractDate;

  String username;

  String address;

  String phone;

  String email;

  String signature;
}
