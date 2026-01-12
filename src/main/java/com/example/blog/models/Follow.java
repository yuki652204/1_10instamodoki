package com.example.blog.models;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "follows")
@Data
@NoArgsConstructor
public class Follow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String followerName; // フォローした人
    private String followingName; // フォローされた人
    private LocalDateTime createdAt = LocalDateTime.now();

    // 手動でのGetter/Setter（念のためLombokに頼らず定義）
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFollowerName() { return followerName; }
    public void setFollowerName(String followerName) { this.followerName = followerName; }

    public String getFollowingName() { return followingName; }
    public void setFollowingName(String followingName) { this.followingName = followingName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}