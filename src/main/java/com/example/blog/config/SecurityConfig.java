package com.example.blog.config;


import com.example.blog.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. CustomOAuth2UserService を注入できるようにします
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // H2 Console等を使う場合は一旦disableにすることが多いです
            .authorizeHttpRequests(auth -> auth
            		// 1. ここが重要：ログインページ自体へのアクセスを「全員許可」にする
                    .requestMatchers("/login", "/css/**", "/js/**", "/error").permitAll()
                    // 2. それ以外はすべて認証が必要
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
//                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService) // ここを修正
                ) // ここに閉じ括弧が必要でした
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
            );
        
        return http.build();
    }
}