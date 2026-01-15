package com.example.blog.repository;

import com.example.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // これが必要です

public interface UserRepository extends JpaRepository<User, Long> {
    // この一行が「undefined」と言われている原因です。正確に記述してください。
    Optional<User> findByEmail(String email);
}