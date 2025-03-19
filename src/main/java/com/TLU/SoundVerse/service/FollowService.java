package com.TLU.SoundVerse.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.entity.Follower;
import com.TLU.SoundVerse.repository.FollowerRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class FollowService {
    @Autowired 
    FollowerRepository followerRepository;

    public boolean isUserFollowed (HttpServletRequest request, Integer artistId) {
        Integer userId = getUserIdFromRequest(request);
        if (userId == null) {
            throw new RuntimeException("User not found");
        }

        if(followerRepository.existsByUserIdAndArtistId(userId, artistId)){
            return true;
        }
        Follower follower = new Follower();
        follower.setUserId(userId);
        follower.setArtistId(artistId);
        followerRepository.save(follower);
        return false;
    }

    public void unFollow(HttpServletRequest request, Integer artistId){
        Integer userId = getUserIdFromRequest(request);
        if (userId == null) {
            throw new RuntimeException("User not found");
        }
        Follower follower = followerRepository.findByUserIdAndArtistId(userId, artistId).orElseThrow(() -> new RuntimeException("Follow not found"));

        followerRepository.delete(follower);
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

