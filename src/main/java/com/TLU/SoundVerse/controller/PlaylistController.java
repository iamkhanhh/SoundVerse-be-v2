package com.TLU.SoundVerse.controller;

import com.TLU.SoundVerse.dto.request.PlaylistDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.dto.response.PlaylistResponse;
import com.TLU.SoundVerse.dto.response.MusicResponse;
import com.TLU.SoundVerse.service.PlaylistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("playlist")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping
    public ApiResponse<List<PlaylistResponse>> getUserPlaylists(HttpServletRequest request) {
        List<PlaylistResponse> response = playlistService.getUserPlaylists(request);

        ApiResponse<List<PlaylistResponse>> apiResponse = new ApiResponse<List<PlaylistResponse>>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get Playlists successfilly");
        apiResponse.setData(response);
        return apiResponse;
    }

    @PostMapping
    public ApiResponse<PlaylistResponse> createPlaylist(
            HttpServletRequest request, @RequestBody PlaylistDto dto) {
        PlaylistResponse response = playlistService.createPlaylist(request, dto);

        ApiResponse<PlaylistResponse> apiResponse = new ApiResponse<PlaylistResponse>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Create Playlist successfilly");
        apiResponse.setData(response);
        return apiResponse;
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<ApiResponse<String>> deletePlaylist(
            HttpServletRequest request, @PathVariable Integer playlistId) {
        ApiResponse<String> response = playlistService.deletePlaylist(request, playlistId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<ApiResponse<List<MusicResponse>>> getMusicsInPlaylist(@PathVariable Integer playlistId) {
        List<MusicResponse>  playlistResponse = playlistService.getMusicsInPlaylist(playlistId);

        ApiResponse<List<MusicResponse>> response = ApiResponse.<List<MusicResponse>>builder()
            .code(200)
            .message("Successfully fetched playlist songs")
            .status("success")
            .data(playlistResponse)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
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
