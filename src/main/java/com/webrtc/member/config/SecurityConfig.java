package com.webrtc.member.config;

//import inplayer.login.filter.JWTAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF 보호 비활성화
                .authorizeRequests()
                .requestMatchers("/api/**").permitAll() // 로그인 관련 URL은 인증 없이 접근 허용
                .requestMatchers("/public/**").permitAll() // 공개 URL은 인증 없이 접근 허용
                .anyRequest().authenticated() // 나머지 요청은 인증이 필요
                .and()
//                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // JWT 인증 필터 추가
                .formLogin(); // 폼 로그인 설정

        return http.build();
    }

//    @Bean
//    public JWTAuthenticationFilter jwtAuthenticationFilter() {
//        return new JWTAuthenticationFilter(); // JWT 인증 필터 초기화
//    }
}