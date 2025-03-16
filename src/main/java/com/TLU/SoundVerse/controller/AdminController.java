package com.TLU.SoundVerse.controller;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.response.AlbumResponse;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.dto.response.MusicResponse;
import com.TLU.SoundVerse.entity.Album;
import com.TLU.SoundVerse.entity.Music;
import com.TLU.SoundVerse.service.AdminService;
import com.TLU.SoundVerse.service.AlbumService;
import com.TLU.SoundVerse.service.MusicService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    AdminService adminService;
    MusicService musicService;
    AlbumService albumService;

    @GetMapping("check")
    public ApiResponse<Boolean> checkAdmin(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");

        String role = (String) user.get("role");  

        ApiResponse<Boolean> apiResponse = new ApiResponse<Boolean>();

        apiResponse.setStatus("success");
        apiResponse.setMessage("Login successfilly");
        apiResponse.setData(role.equals("ADMIN"));
        return apiResponse;
    }

    @GetMapping("/statistics")
    public ApiResponse<Map<String, Integer>> getStatistics() {
        Map<String, Integer> stats = adminService.getStatistics();
        return new ApiResponse<>(200, "Get statistics successfully", "success", stats);
    }

    
    @GetMapping("/musics")
    public ApiResponse<List<MusicResponse>> getAllMusic() {
        List<MusicResponse> musicList = musicService.getAllMusic();
        return new ApiResponse<>(200, "Get all music successfully", "success", musicList);
    }

   
    @GetMapping("/albums")
    public ApiResponse<List<AlbumResponse>> getAllAlbums() {
        List<AlbumResponse> albumList = albumService. getAlbums();
        return new ApiResponse<>(200, "Get all albums successfully", "success", albumList);
    }

    
}