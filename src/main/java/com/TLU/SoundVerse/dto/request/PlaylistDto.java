package com.TLU.SoundVerse.dto.request;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PlaylistDto {
    private String title;

    private String description;

    private String thumbnail;

    private Integer userId;

    private LocalDateTime createdAt;

    private Integer isDeleted;
}
