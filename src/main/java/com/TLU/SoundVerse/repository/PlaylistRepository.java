package com.TLU.SoundVerse.repository;

import com.TLU.SoundVerse.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    List<Playlist> findByUserIdAndIsDeleted(Integer userId, Integer isDeleted);
}
