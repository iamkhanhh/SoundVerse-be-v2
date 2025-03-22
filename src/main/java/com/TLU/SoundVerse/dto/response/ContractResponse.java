package com.TLU.SoundVerse.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractResponse {
  String contractNumber;

  String contractDate;

  String username;

  String address;

  String phone;

  String email;

  String signature;
}
