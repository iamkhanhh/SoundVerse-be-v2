package com.TLU.SoundVerse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{musicId}")
    public ResponseEntity<String> unlikeMusic(@PathVariable Integer musicId, HttpServletRequest request) {
        likeService.unlikeMusic(request, musicId);
        return ResponseEntity.ok("Unliked successfully!");
    }
}
