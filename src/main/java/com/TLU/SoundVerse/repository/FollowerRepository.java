package com.TLU.SoundVerse.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.TLU.SoundVerse.entity.Follower;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Integer> {
    List<Follower> findByUserId(Integer userId);
}
