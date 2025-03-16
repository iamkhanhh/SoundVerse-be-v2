package com.TLU.SoundVerse.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public class PlaylistResponse {
    Integer id;

    String title;
    
    List<MusicResponse> songs;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDateTime createdAt;
}
