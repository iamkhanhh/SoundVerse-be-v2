package com.TLU.SoundVerse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.TLU.SoundVerse.entity.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    @Query(value = """
        SELECT a.* FROM artist a 
        JOIN user u ON a.user_id = u.id 
        WHERE LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """, nativeQuery = true)
    List<Artist> findArtistsByUserFullName(@Param("keyword") String keyword);
}