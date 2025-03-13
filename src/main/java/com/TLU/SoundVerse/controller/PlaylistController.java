package com.TLU.SoundVerse.controller;

import com.TLU.SoundVerse.dto.request.MusicOfPlaylistDto;
import com.TLU.SoundVerse.dto.request.PlaylistDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.entity.Playlist;
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
    public ResponseEntity<ApiResponse<List<Playlist>>> getUserPlaylists(HttpServletRequest request) {
        ApiResponse<List<Playlist>> response = playlistService.getUserPlaylists(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    
    @PostMapping
    public ResponseEntity<ApiResponse<Playlist>> createPlaylist(
            HttpServletRequest request, @RequestBody PlaylistDto dto) {
        ApiResponse<Playlist> response = playlistService.createPlaylist(request, dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

   
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<ApiResponse<String>> deletePlaylist(
            HttpServletRequest request, @PathVariable Integer playlistId) {
        ApiResponse<String> response = playlistService.deletePlaylist(request, playlistId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

   
    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<ApiResponse<List<MusicOfPlaylistDto>>> getMusicsInPlaylist(
            @PathVariable Integer playlistId) {
        ApiResponse<List<MusicOfPlaylistDto>> response = playlistService.getMusicsInPlaylist(playlistId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    
    @PostMapping("/songs")
    public ResponseEntity<ApiResponse<String>> addMusicToPlaylist(@RequestBody MusicOfPlaylistDto dto) {
        ApiResponse<String> response = playlistService.addMusicToPlaylist(dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

 
    @DeleteMapping("/songs")
    public ResponseEntity<ApiResponse<String>> removeMusicFromPlaylist(@RequestBody MusicOfPlaylistDto dto) {
        ApiResponse<String> response = playlistService.removeMusicFromPlaylist(dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
