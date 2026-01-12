package com.example.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blog.models.Follow;
import com.example.blog.repository.FollowRepository;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;

    // 名前（String）でフォロー中（Following）の数を取得するように変更
    public long getFollowingCount(String name) {
        return followRepository.countByFollowerName(name);
    }

    // 名前（String）でフォロワー（Followers）の数を取得するように変更
    public long getFollowerCount(String name) {
        return followRepository.countByFollowingName(name);
    }

    @Transactional
    public void follow(String followerName, String followingName) {
        // 同じ名前同士なら何もしない
        if (followerName.equals(followingName)) return;
        
        // 名前ベースのメソッドに変更
        boolean alreadyFollowed = followRepository.existsByFollowerNameAndFollowingName(followerName, followingName);
        
        if (!alreadyFollowed) {
            Follow follow = new Follow();
            // 引数で受け取った名前を正しくセット
            follow.setFollowerName(followerName);
            follow.setFollowingName(followingName);
            followRepository.save(follow);
        }
    }
}