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
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/login",       // ログイン画面自体は許可
                    "/error",
                    "/css/**",
                    "/js/**",
                    "/oauth2/**",
                    "/login/oauth2/**" ,
                    "/posts/**"
                ).permitAll()
                .anyRequest().authenticated() // これにより "/" もログインが必要になります
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/oauth2/authorization/google") // 未認証時に飛ばす先
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
                .defaultSuccessUrl("/", true) // ログイン成功後に "/" へ
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
            );

        return http.build();
    }
}