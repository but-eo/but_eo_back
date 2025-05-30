package org.example.but_eo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 정적 리소스: 팀 이미지 (/uploads/teams/**)
        registry.addResourceHandler("/uploads/teams/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/teams/");

        // 기존 프로필 이미지
        registry.addResourceHandler("/uploads/profiles/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/profiles/");

        // 경기장 이미지
        registry.addResourceHandler("/uploads/stadiums/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/stadiums/");


        // 기존 static 이미지 유지 (예: 게시판 테스트용)
        registry.addResourceHandler("/images/team/**")
                .addResourceLocations("file:src/main/resources/static/images/team/");
    }
}
