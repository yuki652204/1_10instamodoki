package com.example.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.blog.models.Follow;
import org.springframework.transaction.annotation.Transactional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    
    // 名前で存在チェック
    boolean existsByFollowerNameAndFollowingName(String followerName, String followingName);

    // 名前でフォロー解除
    @Transactional
    void deleteByFollowerNameAndFollowingName(String followerName, String followingName);

    // 名前でカウント（フォロー中）
    long countByFollowerName(String followerName);
    
    // 名前でカウント（フォロワー）
    long countByFollowingName(String followingName);
}