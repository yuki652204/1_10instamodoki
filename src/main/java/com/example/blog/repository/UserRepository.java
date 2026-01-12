package com.example.blog.repository;

import com.example.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // ユーザー名でユーザーを探す（ログイン時に使用）
    Optional<User> findByUsername(String username);
}