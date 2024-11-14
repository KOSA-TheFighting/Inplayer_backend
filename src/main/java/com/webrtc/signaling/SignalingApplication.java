package com.webrtc.signaling;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.webrtc.signaling.repository")
@SpringBootApplication(scanBasePackages = {"com.webrtc"})
public class SignalingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SignalingApplication.class, args);
    }
}
