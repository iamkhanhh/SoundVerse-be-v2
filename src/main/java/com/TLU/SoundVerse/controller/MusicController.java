package com.TLU.SoundVerse.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.request.CreateMusicDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.dto.response.MusicResponse;
import com.TLU.SoundVerse.service.MusicService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@RestController
@RequestMapping("music")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MusicController {
    MusicService musicService;

    @PostMapping
    public ApiResponse<MusicResponse> createMusic(HttpServletRequest request, @RequestBody CreateMusicDto createMusicDto) {
        @SuppressWarnings("unchecked")
        Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
        Integer id = Integer.parseInt(String.valueOf(user.get("id")));
        MusicResponse music = musicService.createMusic(createMusicDto, id);

        ApiResponse<MusicResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Upload Music successfully");
        apiResponse.setData(music);
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<MusicResponse>> getMusic(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
        Integer id = Integer.parseInt(String.valueOf(user.get("id")));
        List<MusicResponse> music = musicService.getPublishedMusicByArtistId(id);

        ApiResponse<List<MusicResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get Music successfully");
        apiResponse.setData(music);
        return apiResponse;
    }

    
    @GetMapping("/my-music")
    public ApiResponse<List<MusicResponse>> getPublishedMusic(HttpServletRequest request) {
        Integer userId = getUserIdFromRequest(request);
        List<MusicResponse> musicList = musicService.getPublishedMusicByArtistId(userId);
        ApiResponse<List<MusicResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get Published Music successfully");
        apiResponse.setData(musicList);
        return apiResponse;
    }

    @GetMapping("/my-pending")
    public ApiResponse<List<MusicResponse>> getPendingMusic(HttpServletRequest request) {
        Integer userId = getUserIdFromRequest(request);
        List<MusicResponse> musicList = musicService.getPendingMusicByArtistId(userId);

        ApiResponse<List<MusicResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get Pending Music successfully");
        apiResponse.setData(musicList);
        return apiResponse;
    }

    @GetMapping("/my-unpublish")
    public ApiResponse<List<MusicResponse>> getUnpublishMusic(HttpServletRequest request) {
        Integer userId = getUserIdFromRequest(request);
        List<MusicResponse> musicList = musicService.getUnpublishMusicByArtistId(userId);

        ApiResponse<List<MusicResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get unpublished Music successfully");
        apiResponse.setData(musicList);
        return apiResponse;
    }

    
    @GetMapping("/pending")
    public ApiResponse<List<MusicResponse>> getPendingMusic() {
        List<MusicResponse> musicList = musicService.getPendingMusic();

        ApiResponse<List<MusicResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get Pending Music successfully");
        apiResponse.setData(musicList);
        return apiResponse;
    }


    @PutMapping("/publish/{musicId}")
    public ApiResponse<MusicResponse> publishMusic(@PathVariable Integer musicId) throws MessagingException {
        MusicResponse musicResponse = musicService.publishMusic(musicId);

        ApiResponse<MusicResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Music published successfully");
        apiResponse.setData(musicResponse);
        return apiResponse;
    }


    @PutMapping("/refuse/{musicId}")
    public ApiResponse<MusicResponse> refuseMusic(@PathVariable Integer musicId) throws MessagingException {
        MusicResponse musicResponse = musicService.refuseMusic(musicId);

        ApiResponse<MusicResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Music refused successfully");
        apiResponse.setData(musicResponse);
        return apiResponse;
    }


    @PutMapping("/approve/{musicId}")
    public ApiResponse<MusicResponse> approveMusic(@PathVariable Integer musicId) throws MessagingException {
        MusicResponse musicResponse = musicService.approveMusic(musicId);

        ApiResponse<MusicResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Music approved successfully");
        apiResponse.setData(musicResponse);
        return apiResponse;
    }

    @DeleteMapping("/{musicId}")
    public ApiResponse<String> deleteMusic(@PathVariable Integer musicId) {
        musicService.deleteMusic(musicId);

        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Delete ALbum succesfully.");
        apiResponse.setData(null);

        return apiResponse;
    }

    private Integer getUserIdFromRequest(HttpServletRequest request) {
        Object userObj = request.getAttribute("user");
    
        if (userObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> user = (Map<String, Object>) userObj;
    
            Object idObj = user.get("id");
            if (idObj != null) {
                try {
                    return Integer.parseInt(idObj.toString());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }
 
    @GetMapping("/favorites")
    public ApiResponse<List<MusicResponse>> getFavoriteMusic(HttpServletRequest request) {
        Integer userId = getUserIdFromRequest(request);
        List<MusicResponse> favoriteMusic = musicService.getFavoriteMusic(userId);
        ApiResponse<List<MusicResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get Favorite Music successfully");
        apiResponse.setData(favoriteMusic);
        return apiResponse;
    }
}
