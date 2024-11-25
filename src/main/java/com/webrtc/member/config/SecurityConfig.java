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
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .authorizeRequests(requests -> requests
                        .requestMatchers("/api/**").permitAll() // 로그인 관련 URL은 인증 없이 접근 허용
                        .requestMatchers("/public/**").permitAll() // 공개 URL은 인증 없이 접근 허용
                        .requestMatchers("/signaling/**").permitAll()
                        .anyRequest().authenticated());
//                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // JWT 인증 필터 추가
//                .formLogin(withDefaults()); // 폼 로그인 설정

        return http.build();
    }

//    @Bean
//    public JWTAuthenticationFilter jwtAuthenticationFilter() {
//        return new JWTAuthenticationFilter(); // JWT 인증 필터 초기화
//    }
}