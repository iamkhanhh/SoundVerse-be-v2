package com.TLU.SoundVerse.controller;

import com.TLU.SoundVerse.dto.request.PlaylistDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.dto.request.CreateMusicDto;
import com.TLU.SoundVerse.service.PlaylistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlaylistDto>>> getUserPlaylists(HttpServletRequest request) {
        ApiResponse<List<PlaylistDto>> response = playlistService.getUserPlaylists(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PlaylistDto>> createPlaylist(
            HttpServletRequest request, @RequestBody PlaylistDto dto) {
        ApiResponse<PlaylistDto> response = playlistService.createPlaylist(request, dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<ApiResponse<String>> deletePlaylist(
            HttpServletRequest request, @PathVariable Integer playlistId) {
        ApiResponse<String> response = playlistService.deletePlaylist(request, playlistId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<ApiResponse<List<CreateMusicDto>>> getMusicsInPlaylist(
            @PathVariable Integer playlistId) {
        ApiResponse<List<CreateMusicDto>> response = playlistService.getMusicsInPlaylist(playlistId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/{playlistId}/songs/{musicId}")
    public ResponseEntity<ApiResponse<String>> addMusicToPlaylist(
            @PathVariable Integer playlistId, @PathVariable Integer musicId) {
        ApiResponse<String> response = playlistService.addMusicToPlaylist(playlistId, musicId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/{playlistId}/songs/{musicId}")
    public ResponseEntity<ApiResponse<String>> removeMusicFromPlaylist(
            @PathVariable Integer playlistId, @PathVariable Integer musicId) {
        ApiResponse<String> response = playlistService.removeMusicFromPlaylist(playlistId, musicId);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
