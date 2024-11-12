package com.webrtc.signaling;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.webrtc.signaling.repository")
public class SignalingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SignalingApplication.class, args);
    }
}
