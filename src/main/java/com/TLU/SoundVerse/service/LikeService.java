package com.TLU.SoundVerse.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.entity.Like;
import com.TLU.SoundVerse.repository.LikeRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LikeService {
    @Autowired 
    private LikeRepository likeRepository;

    public boolean isUserLiked (HttpServletRequest request, Integer musicId) {
        Integer userId = getUserIdFromRequest(request);
        if (userId == null) {
            throw new RuntimeException("User not found");
        }

        if(likeRepository.existsByUserIdAndMusicId(userId, musicId)){
            return true;
        }
        Like like = new Like();
        like.setUserId(userId);
        like.setMusicId(musicId);
        likeRepository.save(like);
        return false;
    }

    public void unlikeMusic(HttpServletRequest request, Integer musicId){
        Integer userId = getUserIdFromRequest(request);
        if (userId == null) {
            throw new RuntimeException("User not found");
        }
        Like like = likeRepository.findByUserIdAndMusicId(userId, musicId).orElseThrow(() -> new RuntimeException("Like not found"));

        likeRepository.delete(like);
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
}
