package com.TLU.SoundVerse.controller;

import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.entity.Artist;
import com.TLU.SoundVerse.service.ArtistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {
    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/top-followed")
    ApiResponse<List<Artist>> getTopFollowedArtists() {
        List<Artist> topArtists = artistService.getTopFollowedArtists();
        return new ApiResponse<>(200, "Get top followed artists successfully", "success", topArtists);
    }
}
