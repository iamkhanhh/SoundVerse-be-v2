package com.TLU.SoundVerse.controller;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.request.UserUpdateDto;
import com.TLU.SoundVerse.dto.response.AlbumResponse;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.dto.response.MusicResponse;
import com.TLU.SoundVerse.dto.response.UserResponse;
import com.TLU.SoundVerse.service.AdminService;
import com.TLU.SoundVerse.service.AlbumService;
import com.TLU.SoundVerse.service.MusicService;
import com.TLU.SoundVerse.service.UserService;

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
    UserService userService;

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

    @GetMapping("/stats")
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
        List<AlbumResponse> albumList = albumService.getAlbums();
        return new ApiResponse<>(200, "Get all albums successfully", "success", albumList);
    }

    @GetMapping("/users")
    ApiResponse<List<UserResponse>> getUsers() {
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<List<UserResponse>>();

        apiResponse.setStatus("success");
        apiResponse.setMessage("List users successfilly");
        apiResponse.setData(userService.getUsers());
    return apiResponse;
  }

  
  @PutMapping("/update")
  public ApiResponse<Void> updateUser(HttpServletRequest request, @RequestBody UserUpdateDto updateDto) {
    Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
    Integer id = Integer.parseInt(String.valueOf(user.get("id")));
    userService.updateUser(id, updateDto);
    return ApiResponse.<Void>builder()
        .status("success")
        .message("Update prfile successfully")
        .code(200)
        .build();
  }
}