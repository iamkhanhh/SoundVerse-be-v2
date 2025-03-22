package com.TLU.SoundVerse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> likeMusic(@PathVariable Integer musicId, HttpServletRequest request) {
        likeService.isUserLiked(request, musicId);
        return ResponseEntity.ok("Liked successfully!");
    }

    @GetMapping("/{musicId}")
    public ApiResponse<Boolean> checkLike(@PathVariable Integer musicId, HttpServletRequest request) {
        Boolean isLike = likeService.checkLike(request, musicId);
        ApiResponse<Boolean> apiResponse = new ApiResponse<Boolean>();

        apiResponse.setStatus("success");
        apiResponse.setMessage("Check like successfilly");
        apiResponse.setData(isLike);
        return apiResponse;
    }

    @DeleteMapping("/{musicId}")
    public ResponseEntity<String> unlikeMusic(@PathVariable Integer musicId, HttpServletRequest request) {
        likeService.unlikeMusic(request, musicId);
        return ResponseEntity.ok("Unliked successfully!");
    }
}
