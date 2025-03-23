package com.TLU.SoundVerse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.service.LikeService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    
    @PostMapping("/{musicId}")
    public ApiResponse<String> likeMusic(@PathVariable Integer musicId, HttpServletRequest request) {
        likeService.isUserLiked(request, musicId);

        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Liked successfully!");
        apiResponse.setData("Liked successfully!");

        return apiResponse;
    }

  
    @GetMapping("/{musicId}")
    public ApiResponse<Boolean> checkLike(@PathVariable Integer musicId, HttpServletRequest request) {
        Boolean isLike = likeService.checkLike(request, musicId);

        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Check like successfully");
        apiResponse.setData(isLike);

        return apiResponse;
    }

    @DeleteMapping("/{musicId}")
    public ApiResponse<String> unlikeMusic(@PathVariable Integer musicId, HttpServletRequest request) {
        likeService.unlikeMusic(request, musicId);

        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Unliked successfully!");
        apiResponse.setData("Unliked successfully!");

        return apiResponse;
    }
}
