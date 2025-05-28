package org.example.but_eo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.net.http.WebSocket;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // WebSocketMessageBrokerConfigurer WebSocket을 쓸 때, 메시지 경로, 구독 채널, 엔드포인트 등을 설정하게 해주는 역할

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/user", "/all"); // /user로 시작하면 알람 발송

        registry.setApplicationDestinationPrefixes("/app"); // /app으로 시작하는 stomp메세지의 경로는 @Controller @MessageMapping 메서드로 라우팅
        System.out.println("WebSocket 메시지 브로커 설정 완료");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // StompEndpointRegistry = Stomp 엔드포인트를 등록할 때 사용하는 클래스
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
        System.out.println("Websocket 엔드포인트 /ws 등록 완료");
    }
}
