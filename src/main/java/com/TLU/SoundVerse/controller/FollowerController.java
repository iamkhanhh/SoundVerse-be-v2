package com.TLU.SoundVerse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.service.FollowService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("follow")
public class FollowerController {

    @Autowired
    private FollowService followService;

    @PostMapping("/{artistId}")
    public ApiResponse<String> followArtist(@PathVariable Integer artistId, HttpServletRequest request) {
        followService.isUserFollowed(request, artistId);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Followed successfully!");
        apiResponse.setData("Followed artist with ID: " + artistId);  
        return apiResponse;
    }

    @GetMapping("/{artistId}")
    public ApiResponse<Boolean> checkFollow(@PathVariable Integer artistId, HttpServletRequest request) {
        Boolean isFollow = followService.checkFollow(request, artistId);
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Check follow successfully");
        apiResponse.setData(isFollow);
        return apiResponse;
    }

    @DeleteMapping("/{artistId}")
    public ApiResponse<String> unfollowArtist(@PathVariable Integer artistId, HttpServletRequest request) {
        followService.unFollow(request, artistId);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Unfollowed successfully!");
        apiResponse.setData("Unfollowed artist with ID: " + artistId);  
        return apiResponse;
    }
}
