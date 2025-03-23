package com.TLU.SoundVerse.controller;

import com.TLU.SoundVerse.dto.request.PlaylistDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.dto.response.PlaylistResponse;
import com.TLU.SoundVerse.service.PlaylistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

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

        ApiResponse<List<PlaylistResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get Playlists successfully");
        apiResponse.setData(response);
        return apiResponse;
    }

    
    @PostMapping
    public ApiResponse<PlaylistResponse> createPlaylist(
            HttpServletRequest request, @RequestBody PlaylistDto dto) {
        PlaylistResponse response = playlistService.createPlaylist(request, dto);

        ApiResponse<PlaylistResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Create Playlist successfully");
        apiResponse.setData(response);
        return apiResponse;
    }

    
    @DeleteMapping("/{playlistId}")
    public ApiResponse<String> deletePlaylist(
            HttpServletRequest request, @PathVariable Integer playlistId) {
        ApiResponse<String> response = playlistService.deletePlaylist(request, playlistId);
        return response; 
    }

    
    @GetMapping("/{playlistId}")
    public ApiResponse<PlaylistResponse> getPlaylistById(@PathVariable Integer playlistId) {
        PlaylistResponse playlistResponse = playlistService.getPlaylistById(playlistId);

        ApiResponse<PlaylistResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Successfully fetched playlist songs");
        apiResponse.setData(playlistResponse);
        return apiResponse;
    }

    
    @PostMapping("/{playlistId}/songs/{musicId}")
    public ApiResponse<String> addMusicToPlaylist(
            @PathVariable Integer playlistId, @PathVariable Integer musicId) {
        ApiResponse<String> response = playlistService.addMusicToPlaylist(playlistId, musicId);
        return response; 
    }

    
    @DeleteMapping("/{playlistId}/songs/{musicId}")
    public ApiResponse<String> removeMusicFromPlaylist(
            @PathVariable Integer playlistId, @PathVariable Integer musicId) {
        ApiResponse<String> response = playlistService.removeMusicFromPlaylist(playlistId, musicId);
        return response; 
    }
}
