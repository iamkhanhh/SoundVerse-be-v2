package com.TLU.SoundVerse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.service.FollowService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("follow")
public class FollowerController {
 @Autowired
    private FollowService followService;

    @PostMapping("/{artistId}")
    public ResponseEntity<String> followArtist(@PathVariable Integer artistId, HttpServletRequest request) {
        System.out.println("dấdasda");
        followService.isUserFollowed(request, artistId);
        return ResponseEntity.ok("Followed successfully!");
    }

    @DeleteMapping("/{artistId}")
    public ResponseEntity<String> unfollowArtist(@PathVariable Integer artistId, HttpServletRequest request) {
        followService.unFollow(request, artistId);
        return ResponseEntity.ok("Unfollow successfully!");
    }
}
