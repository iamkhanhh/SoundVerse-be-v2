package com.TLU.SoundVerse.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAlbumDto {
  String title;

  String description;
  
  String thumbnail;
}
