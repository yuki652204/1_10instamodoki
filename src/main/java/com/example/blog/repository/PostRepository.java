package com.example.blog.repository;

import com.example.blog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // もともと何かメソッドがあればここに残す
    
    // 今回追加する「いいね増量」メソッド
    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.likesCount = p.likesCount + 1 WHERE p.id = :id")
    void incrementLikes(@Param("id") Long id);
}