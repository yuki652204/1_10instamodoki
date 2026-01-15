package com.example.blog.service;

import com.example.blog.models.User;
import com.example.blog.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Googleから情報を取得
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        // DBへの保存・更新ロジック
        userRepository.findByEmail(email)
            .map(existingUser -> {
                existingUser.setUsername(name);
                existingUser.setPicture(picture);
                return userRepository.save(existingUser);
            })
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setUsername(name);
                newUser.setEmail(email);
                newUser.setPicture(picture);
                newUser.setPassword(""); // OAuth2なのでパスワードは空
                newUser.setRole("ROLE_USER");
                return userRepository.save(newUser);
            });

        return oAuth2User;
    }
}